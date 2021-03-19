package MakeUs.Moira.service.project;

import MakeUs.Moira.advice.exception.ProjectException;
import MakeUs.Moira.controller.project.dto.ProjectApplyRequestDTO;
import MakeUs.Moira.controller.project.dto.ProjectApplysResponseDTO;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.ProjectRepo;
import MakeUs.Moira.domain.project.projectApply.*;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserHistory;
import MakeUs.Moira.domain.user.UserHistoryRepo;
import MakeUs.Moira.domain.user.UserRepo;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectApplyService {

    private final UserRepo userRepo;
    private final UserHistoryRepo userHistoryRepo;
    private final ProjectRepo projectRepo;
    private final ProjectApplyRepo projectApplyRepo;


    @Transactional
    public void applyProject(ProjectApplyRequestDTO projectApplyRequestDTO, Long userId){
        User userEntity = getValidUser(userId);
        Project projectEntity = getValidProject(projectApplyRequestDTO.getProjectId());

        // ProjectApply 생성
        ProjectApply projectApplyEntity = ProjectApply.builder()
                .applicant(userEntity)
                .userPosition(userEntity.getUserPosition())
                .projectApplyStatus(ProjectApplyStatus.APPLY)
                .projectDetail(projectEntity.getProjectDetail())
                .build();
        projectApplyEntity = projectApplyRepo.save(projectApplyEntity);

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
        for(UserPortfolioType userPortfolioType : projectApplyRequestDTO.getUserPortfolioTypeList()) {
            switch (userPortfolioType) {
                case SCHOOL:
                    for (UserSchool userSchool : userEntity.getUserPortfolio().getUserSchoolList()) {
                        projectApplyEntity.addOptionalApplyInfo(
                                OptionalApplyInfo.builder()
                                        .userPortfolioType(UserPortfolioType.SCHOOL)
                                        .UserSelectedPortfolioId(userSchool.getId())
                                        .projectApply(projectApplyEntity)
                                        .build()
                        );
                    }
                case CAREER:
                    for (UserCareer userCareer : userEntity.getUserPortfolio().getUserCareerList()) {
                        projectApplyEntity.addOptionalApplyInfo(
                                OptionalApplyInfo.builder()
                                        .userPortfolioType(UserPortfolioType.CAREER)
                                        .UserSelectedPortfolioId(userCareer.getId())
                                        .projectApply(projectApplyEntity)
                                        .build()
                        );
                    }
                case LICENSE:
                    for (UserLicense userLicense : userEntity.getUserPortfolio().getUserLicenseList()) {
                        projectApplyEntity.addOptionalApplyInfo(
                                OptionalApplyInfo.builder()
                                        .userPortfolioType(UserPortfolioType.LICENSE)
                                        .UserSelectedPortfolioId(userLicense.getId())
                                        .projectApply(projectApplyEntity)
                                        .build()
                        );
                    }
                case AWARD:
                    for (UserAward userAward : userEntity.getUserPortfolio().getUserAwardList()) {
                        projectApplyEntity.addOptionalApplyInfo(
                                OptionalApplyInfo.builder()
                                        .userPortfolioType(UserPortfolioType.AWARD)
                                        .UserSelectedPortfolioId(userAward.getId())
                                        .projectApply(projectApplyEntity)
                                        .build()
                        );
                    }
                case LINK:
                    for (UserLink userLink : userEntity.getUserPortfolio().getUserLinkList()) {
                        projectApplyEntity.addOptionalApplyInfo(
                                OptionalApplyInfo.builder()
                                        .userPortfolioType(UserPortfolioType.LINK)
                                        .UserSelectedPortfolioId(userLink.getId())
                                        .projectApply(projectApplyEntity)
                                        .build()
                        );
                    }
            }
        }
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
    public void cancelApplyProject(Long projectApplyId, Long userId) {
        User userEntity = getValidUser(userId);
        ProjectApply projectApply = projectApplyRepo.findById(projectApplyId)
                .orElseThrow(() -> new ProjectException("존재하지 않는 프로젝트 지원 ID"));
        if(projectApply.getApplicant().getId() != userId){
            throw new ProjectException("권한이 없는 유저");
        }
        projectApplyRepo.delete(projectApply);
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
}
