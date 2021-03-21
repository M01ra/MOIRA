package MakeUs.Moira.service.project;

import MakeUs.Moira.advice.exception.ProjectException;
import MakeUs.Moira.controller.project.dto.ProjectApplicantsResponseDTO;
import MakeUs.Moira.controller.project.dto.ProjectApplyRequestDTO;
import MakeUs.Moira.controller.project.dto.ProjectApplysResponseDTO;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.ProjectRepo;
import MakeUs.Moira.domain.project.projectApply.*;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.userPortfolio.UserPortfolioType;
import MakeUs.Moira.domain.userPortfolio.userAward.UserAward;
import MakeUs.Moira.domain.userPortfolio.userCarrer.UserCareer;
import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicense;
import MakeUs.Moira.domain.userPortfolio.userLink.UserLink;
import MakeUs.Moira.domain.userPortfolio.userSchool.UserSchool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectApplyService {

    private final UserRepo userRepo;
    private final UserHistoryRepo userHistoryRepo;
    private final UserProjectRepo userProjectRepo;
    private final ProjectRepo projectRepo;
    private final ProjectApplyRepo projectApplyRepo;


    @Transactional
    public void applyProject(ProjectApplyRequestDTO projectApplyRequestDTO, Long userId){
        User userEntity = getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = getValidProject(projectApplyRequestDTO.getProjectId());

        // 이미 지원한 유저인지 확인
        projectEntity.getProjectDetail().getProjectApplyList()
                        .forEach(projectApplyEntity -> checkProjectApplicant(projectApplyEntity, userId));

        // 프로젝트 팀장인지 확인
        if(isProjectLeader(userHistoryEntity.getId(), projectEntity.getId())){
            throw new ProjectException("권한이 없는 유저");
        }

        // ProjectApply 생성
        ProjectApply projectApplyEntity = ProjectApply.builder()
                .applicant(userEntity)
                .userPosition(userEntity.getUserPosition())
                .projectApplyStatus(ProjectApplyStatus.APPLY)
                .projectDetail(projectEntity.getProjectDetail())
                .build();
        projectApplyRepo.save(projectApplyEntity);

        // 지원 답변 추가
        for(String answer : projectApplyRequestDTO.getAnswerList()){
            projectApplyEntity.addProjectApplyAnswer(
                    ProjectApplyAnswer.builder()
                            .answer(answer)
                            .projectApply(projectApplyEntity)
                            .build()
            );
        }

        // 선택 사항 추가
        addOptionalApplyInfo(projectApplyRequestDTO, userEntity, projectApplyEntity);

        // Project에 ProjectApply 추가
        projectEntity.getProjectDetail().addProjectApply(projectApplyEntity);
    }


    @Transactional
    public List<ProjectApplysResponseDTO> getApplyProjects(Long userId){
        List<ProjectApply> projectApplyList = projectApplyRepo.findAllByApplicantId(userId);
        return projectApplyList
                .stream()
                .map(projectApply -> {
                            Project projectEntity = getValidProject(projectApply.getProjectDetail().getProject().getId());
                            return ProjectApplysResponseDTO.builder()
                                    .projectApplyId(projectApply.getId())
                                    .projectId(projectEntity.getId())
                                    .hitCount(projectEntity.getHitCount())
                                    .imageUrl(projectEntity.getProjectImageUrl())
                                    .title(projectEntity.getProjectTitle())
                                    .time(getTime(projectApply.getCreatedDate()))
                                    .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void changeProjectApplyStatus(Long projectApplyId, Long userId, ProjectApplyStatus status){
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        ProjectApply projectApplyEntity = getValidProjectApply(projectApplyId);
        Project projectEntity = projectApplyEntity.getProjectDetail().getProject();
        switch (status){
            case ACCEPT: {
                // 본인의 지원서인지 검증
                checkProjectApplicant(projectApplyEntity, userId);

                // UserProject 생성
                if(!isExistUserProject(userHistoryEntity.getId(), projectEntity.getId())){
                    UserProject userProjectEntity = UserProject.builder()
                                                               .project(projectEntity)
                                                               .userProjectStatus(UserProjectStatus.PROGRESS)
                                                               .userHistory(userHistoryEntity)
                                                               .roleType(UserProjectRoleType.MEMBER)
                                                               .userPosition(projectApplyEntity.getUserPosition())
                                                               .build();
                    // UserProject 양방향 추가
                    projectEntity.addUserProjectList(userProjectEntity);
                    userHistoryEntity.addUserProject(userProjectEntity);
                }
                else{
                    throw new ProjectException("이미 가입된 유저");
                }
            } break;
            case REJECT: checkProjectApplicant(projectApplyEntity, userId); break;
            case INVITE: {
                if(!isProjectLeader(userHistoryEntity.getId(), projectApplyId)){
                    throw new ProjectException("권한이 없는 유저");
                }
                break;
            }
        }

        // ProjectApply 상태 변경
        projectApplyEntity.updateProjectApplyStatus(status);
    }


    @Transactional
    public void cancelApplyProject(Long projectApplyId, Long userId) {
        ProjectApply projectApplyEntity = getValidProjectApply(projectApplyId);
        ProjectDetail projectDetailEntity = projectApplyEntity.getProjectDetail();

        // 본인의 지원서가 아닐 경우
        if(!projectApplyEntity.getApplicant().getId().equals(userId)){
            throw new ProjectException("권한이 없는 유저");
        }

        projectDetailEntity.removeProjectApply(projectApplyEntity);
        projectApplyRepo.delete(projectApplyEntity);
    }


    @Transactional
    public List<ProjectApplicantsResponseDTO> getProjectApplicants(Long projectId, Long userId){
        Project projectEntity = getValidProject(projectId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        if(!isProjectLeader(userHistoryEntity.getId(), projectId)){
            throw new ProjectException("권한이 없는 유저");
        }

        return projectEntity.getProjectDetail().getProjectApplyList()
                            .stream()
                            .map(projectApply -> ProjectApplicantsResponseDTO.builder()
                                                                             .projectApplyId(projectApply.getId())
                                                                             .nickname(projectApply.getApplicant().getNickname())
                                                                             .imageUrl(projectApply.getApplicant().getProfileImage())
                                                                             .position(projectApply.getUserPosition().getPositionName())
                                                                             .build()
                            )
                            .collect(Collectors.toList());
    }


    public void addOptionalApplyInfo(ProjectApplyRequestDTO projectApplyRequestDTO, User userEntity, ProjectApply projectApplyEntity){
        for(UserPortfolioType userPortfolioType : projectApplyRequestDTO.getUserPortfolioTypeList()) {
            switch (userPortfolioType) {
                case SCHOOL:
                    for (UserSchool userSchool : userEntity.getUserPortfolio()
                                                           .getUserSchoolList()) {
                        projectApplyEntity.addOptionalApplyInfo(
                                OptionalApplyInfo.builder()
                                                 .userPortfolioType(UserPortfolioType.SCHOOL)
                                                 .userSelectedPortfolioId(userSchool.getId())
                                                 .projectApply(projectApplyEntity)
                                                 .build()
                        );
                    }
                    break;
                case CAREER:
                    for (UserCareer userCareer : userEntity.getUserPortfolio()
                                                           .getUserCareerList()) {
                        projectApplyEntity.addOptionalApplyInfo(
                                OptionalApplyInfo.builder()
                                                 .userPortfolioType(UserPortfolioType.CAREER)
                                                 .userSelectedPortfolioId(userCareer.getId())
                                                 .projectApply(projectApplyEntity)
                                                 .build()
                        );
                    }
                    break;
                case LICENSE:
                    for (UserLicense userLicense : userEntity.getUserPortfolio()
                                                             .getUserLicenseList()) {
                        projectApplyEntity.addOptionalApplyInfo(
                                OptionalApplyInfo.builder()
                                                 .userPortfolioType(UserPortfolioType.LICENSE)
                                                 .userSelectedPortfolioId(userLicense.getId())
                                                 .projectApply(projectApplyEntity)
                                                 .build()
                        );
                    }
                    break;
                case AWARD:
                    for (UserAward userAward : userEntity.getUserPortfolio()
                                                         .getUserAwardList()) {
                        projectApplyEntity.addOptionalApplyInfo(
                                OptionalApplyInfo.builder()
                                                 .userPortfolioType(UserPortfolioType.AWARD)
                                                 .userSelectedPortfolioId(userAward.getId())
                                                 .projectApply(projectApplyEntity)
                                                 .build()
                        );
                    }
                    break;
                case LINK:
                    for (UserLink userLink : userEntity.getUserPortfolio()
                                                       .getUserLinkList()) {
                        projectApplyEntity.addOptionalApplyInfo(
                                OptionalApplyInfo.builder()
                                                 .userPortfolioType(UserPortfolioType.LINK)
                                                 .userSelectedPortfolioId(userLink.getId())
                                                 .projectApply(projectApplyEntity)
                                                 .build()
                        );
                    }
            }
        }
    }


    private String getTime(LocalDateTime localDateTime){
        String time;
        if(ChronoUnit.YEARS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.YEARS.between(localDateTime, LocalDateTime.now())) + "년 전";
        }
        else if(ChronoUnit.MONTHS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.MONTHS.between(localDateTime, LocalDateTime.now())) + "개월 전";
        }
        else if(ChronoUnit.DAYS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.DAYS.between(localDateTime, LocalDateTime.now())) + "일 전";
        }
        else if(ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now())) + "시간 전";
        }
        else if(ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now())) + "분 전";
        }
        else {
            time = "방금 전";
        }
        return time;
    }


    private User getValidUser(Long userId){
        User userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new ProjectException("유효하지 않는 유저"));
        return userEntity;
    }


    private UserHistory getValidUserHistory(Long userId){
        UserHistory userHistoryEntity = userHistoryRepo.findByUserId(userId)
                .orElseThrow(() -> new ProjectException("유효하지 않는 유저"));
        return userHistoryEntity;
    }


    private Project getValidProject(Long projectId){
        Project projectEntity = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectException("존재하지 않은 프로젝트 ID"));
        return projectEntity;
    }


    private ProjectApply getValidProjectApply(Long projectApplyId){
        ProjectApply projectApplyEntity = projectApplyRepo.findById(projectApplyId)
                                           .orElseThrow(() -> new ProjectException("존재하지 않은 지원서 ID"));
        return projectApplyEntity;
    }


    private boolean isProjectLeader(Long userHistoryId, Long projectId){
        Optional<UserProject> optionalUserProjectEntity = userProjectRepo.findByUserHistoryIdAndProjectId(userHistoryId, projectId);
        if(!optionalUserProjectEntity.isPresent()){
            return false;
        }
        else{
            UserProject userProjectEntity = optionalUserProjectEntity.get();
            return userProjectEntity.getRoleType() == UserProjectRoleType.LEADER;
        }
    }


    private void checkProjectApplicant(ProjectApply projectApplyEntity, Long userId) {
        if (projectApplyEntity.getApplicant().getId().equals(userId)) {
            throw new ProjectException("권한이 없는 유저");
        }
    }


    private boolean isExistUserProject(Long userHistoryId, Long projectId){
        return userProjectRepo.findByUserHistoryIdAndProjectId(userHistoryId, projectId).isPresent();
    }
}
