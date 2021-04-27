package MakeUs.Moira.service.projectApply;

import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.projectApply.dto.ProjectApplicantsResponseDTO;
import MakeUs.Moira.controller.projectApply.dto.ProjectApplyRequestDTO;
import MakeUs.Moira.controller.projectApply.dto.ProjectApplyResponseDTO;
import MakeUs.Moira.controller.myPage.dto.HashtagResponseDto;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.ProjectRepo;
import MakeUs.Moira.domain.project.projectApply.*;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.userPortfolio.UserPortfolioType;
import MakeUs.Moira.domain.userPortfolio.userAward.UserAward;
import MakeUs.Moira.domain.userPortfolio.userAward.UserAwardRepo;
import MakeUs.Moira.domain.userPortfolio.userCareer.UserCareer;
import MakeUs.Moira.domain.userPortfolio.userCareer.UserCareerRepo;
import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicense;
import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicenseRepo;
import MakeUs.Moira.domain.userPortfolio.userLink.UserLink;
import MakeUs.Moira.domain.userPortfolio.userLink.UserLinkRepo;
import MakeUs.Moira.domain.userPortfolio.userSchool.UserSchool;
import MakeUs.Moira.controller.userPortfolio.userAward.dto.UserAwardResponseDto;
import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerResponseDto;
import MakeUs.Moira.controller.userPortfolio.userLicense.dto.UserLicenseResponseDto;
import MakeUs.Moira.controller.userPortfolio.userLink.dto.UserLinkResponseDto;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.UserSchoolResponseDto;
import MakeUs.Moira.domain.userPortfolio.userSchool.UserSchoolRepo;
import MakeUs.Moira.domain.alarm.AlarmType;
import MakeUs.Moira.service.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectApplyService {

    private final UserRepo         userRepo;
    private final UserHistoryRepo  userHistoryRepo;
    private final UserProjectRepo  userProjectRepo;
    private final ProjectRepo      projectRepo;
    private final ProjectApplyRepo projectApplyRepo;
    private final UserSchoolRepo   userSchoolRepo;
    private final UserCareerRepo   userCareerRepo;
    private final UserLicenseRepo  userLicenseRepo;
    private final UserAwardRepo    userAwardRepo;
    private final UserLinkRepo     userLinkRepo;
    private final AlarmService     alarmService;


    @Transactional
    public void applyProject(ProjectApplyRequestDTO projectApplyRequestDTO, Long userId) {
        User userEntity = getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = getValidProject(projectApplyRequestDTO.getProjectId());

        // 이미 지원한 유저인지 확인
        projectEntity.getProjectDetail()
                     .getProjectApplyList()
                     .forEach(projectApplyEntity -> checkAlreadyProjectApplicant(projectApplyEntity, userId));

        // 이미 가입된 유저인지 확인
        if (isProjectUser(userHistoryEntity.getId(), projectEntity.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        // ProjectApply 생성
        ProjectApply projectApplyEntity = ProjectApply.builder()
                                                      .applicant(userEntity)
                                                      .userPosition(userEntity.getUserPosition())
                                                      .projectApplyStatus(ProjectApplyStatus.USER_APPLIED)
                                                      .projectDetail(projectEntity.getProjectDetail())
                                                      .build();
        projectApplyRepo.save(projectApplyEntity);

        // 선택 사항 추가
        addOptionalApplyInfo(projectApplyRequestDTO, userEntity, projectApplyEntity);

        // Project에 ProjectApply 추가
        projectEntity.getProjectDetail()
                     .addProjectApply(projectApplyEntity);

        // 알람 생성
        alarmService.saveProjectApply(
                userEntity.getNickname() + "님이 " + getProjectTitle(projectApplyEntity.getProjectDetail()
                                                                                     .getProject()
                                                                                     .getProjectTitle()) + "에 지원했습니다.",
                AlarmType.APPLY_ANSWER,
                projectApplyEntity.getId(),
                getLeader(projectEntity).getId(),
                projectApplyEntity.getProjectDetail()
                                  .getProject()
                                  .getProjectImageUrl()
        );

    }


    @Transactional
    public ProjectApplyResponseDTO getApplyProject(Long projectApplyId, Long userId) {
        ProjectApply projectApplyEntity = getValidProjectApply(projectApplyId);
        User userEntity = projectApplyEntity.getApplicant();
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = projectApplyEntity.getProjectDetail()
                                                  .getProject();

        // 이미 가입된 유저인지 확인
        if(isExistUserProject(projectApplyEntity.getApplicant().getId(), projectEntity.getId())){
            // 조회하는 사람이 같은 팀원인지 확인
            checkProjectTeammate(userHistoryEntity.getId(), projectEntity.getId());
        } else {
            // 팀의 리더가 아닐경우
            if (!isProjectLeader(userHistoryEntity.getId(), projectEntity.getId())) {
                // 자신의 지원서를 보는 것이 아닐경우
                if (!userId.equals(userEntity.getId())) {
                    throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
                }
            }
        }

        // 해시태그
        List<HashtagResponseDto> hashtagResponseDtoList = projectEntity.getProjectHashtagList()
                                                                       .stream()
                                                                       .map(HashtagResponseDto::new)
                                                                       .collect(Collectors.toList());

        // 포트폴리오
        List<UserSchoolResponseDto> userSchoolResponseDtoList = new ArrayList<>();
        List<UserCareerResponseDto> userCareerResponseDtoList = new ArrayList<>();
        List<UserLicenseResponseDto> userLicenseResponseDtoList = new ArrayList<>();
        List<UserAwardResponseDto> userAwardResponseDtoList = new ArrayList<>();
        List<UserLinkResponseDto> userLinkResponseDtoList = new ArrayList<>();
        setUserPortfolio(projectApplyEntity, userSchoolResponseDtoList, userCareerResponseDtoList, userLicenseResponseDtoList, userAwardResponseDtoList, userLinkResponseDtoList);

        return ProjectApplyResponseDTO.builder()
                                      .userId(userEntity.getId())
                                      .nickname(userEntity.getNickname())
                                      .imageUrl(userEntity.getProfileImage())
                                      .shortIntroduction(userEntity.getShortIntroduction())
                                      .userSchoolResponseDtoList(userSchoolResponseDtoList)
                                      .userCareerResponseDtoList(userCareerResponseDtoList)
                                      .userLicenseResponseDtoList(userLicenseResponseDtoList)
                                      .userAwardResponseDtoList(userAwardResponseDtoList)
                                      .userLinkResponseDtoList(userLinkResponseDtoList)
                                      .hashtagResponseDtoList(hashtagResponseDtoList)
                                      .build();
    }


    @Transactional
    public void changeProjectApplyStatus(Long projectApplyId, Long userId, ProjectApplyStatus status){
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        ProjectApply projectApplyEntity = getValidProjectApply(projectApplyId);
        Project projectEntity = projectApplyEntity.getProjectDetail()
                                                  .getProject();
        switch (status) {
            case APPLY_REJECTED:
                // 팀장인지 확인
                if (!isProjectLeader(userHistoryEntity.getId(), projectEntity.getId())) {
                    throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
                }
                // 유효한 상태 변경인지 검증
                checkValidProjectApplyStatus(projectApplyEntity.getProjectApplyStatus(), ProjectApplyStatus.USER_APPLIED);
                // 알람 생성
                alarmService.saveProjectApply(
                        getProjectTitle(projectApplyEntity.getProjectDetail()
                                                          .getProject()
                                                          .getProjectTitle()) + "에 안타깝게 합류하지 못했습니다.",
                        AlarmType.APPLY,
                        projectApplyId,
                        projectApplyEntity.getApplicant()
                                          .getId(),
                        projectApplyEntity.getProjectDetail()
                                          .getProject()
                                          .getProjectImageUrl()
                );

                break;

            case TEAM_INVITED:
                // 팀장인지 확인
                if (!isProjectLeader(userHistoryEntity.getId(), projectEntity.getId())) {
                    throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
                }
                // 유효한 상태 변경인지 검증
                checkValidProjectApplyStatus(projectApplyEntity.getProjectApplyStatus(), ProjectApplyStatus.USER_APPLIED);

                // UserProject 생성
                if (!isExistUserProject(projectApplyEntity.getApplicant().getId(), projectEntity.getId())) {
                    UserProject userProjectEntity = UserProject.builder()
                                                               .project(projectEntity)
                                                               .userProjectStatus(UserProjectStatus.PROGRESS)
                                                               .userHistory(projectApplyEntity.getApplicant().getUserHistory())
                                                               .roleType(UserProjectRoleType.MEMBER)
                                                               .userPosition(projectApplyEntity.getUserPosition())
                                                               .build();
                    // UserProject 양방향 추가
                    projectEntity.addUserProjectList(userProjectEntity);
                    projectApplyEntity.getApplicant().getUserHistory().addUserProject(userProjectEntity);

                    // 지원자 참여횟수 증가
                    projectApplyEntity.getApplicant().getUserHistory().addParticipationCount();

                    // 리더 참여횟수 증가
                    if (projectEntity.getUserProjectList()
                                     .size() == 2) {
                        userHistoryEntity.addParticipationCount();
                    }

                    // 알람 생성
                    alarmService.saveProjectApply(
                            getProjectTitle(projectApplyEntity.getProjectDetail()
                                                              .getProject()
                                                              .getProjectTitle()) + "에 합류하게 되었습니다.",
                            AlarmType.INVITE_ANSWER,
                            projectApplyId,
                            projectApplyEntity.getApplicant()
                                              .getId(),
                            projectApplyEntity.getProjectDetail().getProject().getProjectImageUrl()
                    );
                } else {
                    throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER);
                }
                break;



                // 알람 생성
                //alarmService.saveProjectApply(
                //        getProjectTitle(projectApplyEntity.getProjectDetail()
                //                                         .getProject()
                //                                          .getProjectTitle()) + "에 합류하게 되었습니다.",
                //        AlarmType.INVITE_ANSWER,
                //        projectApplyId,
                //        projectApplyEntity.getApplicant()
                //                          .getId(),
                //        projectApplyEntity.getProjectDetail().getProject().getProjectImageUrl()
                //);

            case USER_ACCEPTED:
                // 본인의 지원서인지 검증
                checkProjectApplicant(projectApplyEntity, userId);
                // 유효한 상태 변경인지 검증
                checkValidProjectApplyStatus(projectApplyEntity.getProjectApplyStatus(), ProjectApplyStatus.TEAM_INVITED);
                // UserProject 생성
                if (!isExistUserProject(userHistoryEntity.getId(), projectEntity.getId())) {
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

                    // 지원자 참여횟수 증가
                    userHistoryEntity.addParticipationCount();

                    User leader = getLeader(projectEntity);

                    // 리더 참여횟수 증가
                    if (projectEntity.getUserProjectList()
                                     .size() == 2) {
                        leader.getUserHistory()
                              .addParticipationCount();
                    }
                    // 알람 생성
                    alarmService.saveProjectApply(
                            projectApplyEntity.getApplicant()
                                              .getNickname() + "님이 " + getProjectTitle(projectApplyEntity.getProjectDetail()
                                                                                                         .getProject()
                                                                                                         .getProjectTitle()) + "에 합류하게 되었습니다.",
                            AlarmType.APPLY,
                            projectApplyId,
                            leader.getId(),
                            projectApplyEntity.getProjectDetail().getProject().getProjectImageUrl()
                    );
                } else {
                    throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER);
                }
                break;

            case USER_REJECTED:
                // 본인의 지원서인지 검증
                checkProjectApplicant(projectApplyEntity, userId);
                // 유효한 상태 변경인지 검증
                checkValidProjectApplyStatus(projectApplyEntity.getProjectApplyStatus(), ProjectApplyStatus.TEAM_INVITED);
                // 알람 생성
                User leader = getLeader(projectEntity);
                alarmService.saveProjectApply(
                        projectApplyEntity.getApplicant()
                                          .getNickname() + "님이 " + getProjectTitle(projectApplyEntity.getProjectDetail()
                                                                                                     .getProject()
                                                                                                     .getProjectTitle()) + "에 안타깝게 합류하지 못했습니다.",
                        AlarmType.APPLY,
                        projectApplyId,
                        leader.getId(),
                        projectApplyEntity.getProjectDetail().getProject().getProjectImageUrl()
                );
                break;
        }

        // ProjectApply 상태 변경
        projectApplyEntity.updateProjectApplyStatus(status);
    }


    @Transactional
    public void cancelApplyProject(Long projectApplyId, Long userId) {
        ProjectApply projectApplyEntity = getValidProjectApply(projectApplyId);
        ProjectDetail projectDetailEntity = projectApplyEntity.getProjectDetail();

        // 본인의 지원서가 아닐 경우
        if (!projectApplyEntity.getApplicant()
                               .getId()
                               .equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        projectDetailEntity.removeProjectApply(projectApplyEntity);
        projectApplyRepo.delete(projectApplyEntity);
    }


    @Transactional
    public List<ProjectApplicantsResponseDTO> getProjectApplicants(Long projectId, Long userId) {
        Project projectEntity = getValidProject(projectId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        if (!isProjectLeader(userHistoryEntity.getId(), projectId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        return projectEntity.getProjectDetail()
                            .getProjectApplyList()
                            .stream()
                            .map(projectApply -> ProjectApplicantsResponseDTO.builder()
                                                                             .userId(projectApply.getApplicant().getId())
                                                                             .projectApplyId(projectApply.getId())
                                                                             .nickname(projectApply.getApplicant()
                                                                                                   .getNickname())
                                                                             .imageUrl(projectApply.getApplicant()
                                                                                                   .getProfileImage())
                                                                             .position(projectApply.getUserPosition()
                                                                                                   .getPositionName())
                                                                             .shortIntroduction(projectApply.getApplicant()
                                                                                                            .getShortIntroduction())
                                                                             .build()
                            )
                            .collect(Collectors.toList());
    }


    public void addOptionalApplyInfo(ProjectApplyRequestDTO projectApplyRequestDTO,
                                     User userEntity,
                                     ProjectApply projectApplyEntity)
    {
        if(projectApplyRequestDTO.getUserPortfolioTypeList() != null) {
            for (UserPortfolioType userPortfolioType : projectApplyRequestDTO.getUserPortfolioTypeList()) {
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
    }


    private String getTime(LocalDateTime localDateTime) {
        String time;
        if (ChronoUnit.YEARS.between(localDateTime, LocalDateTime.now()) >= 1) {
            time = Long.toString(ChronoUnit.YEARS.between(localDateTime, LocalDateTime.now())) + "년 전";
        } else if (ChronoUnit.MONTHS.between(localDateTime, LocalDateTime.now()) >= 1) {
            time = Long.toString(ChronoUnit.MONTHS.between(localDateTime, LocalDateTime.now())) + "개월 전";
        } else if (ChronoUnit.DAYS.between(localDateTime, LocalDateTime.now()) >= 1) {
            time = Long.toString(ChronoUnit.DAYS.between(localDateTime, LocalDateTime.now())) + "일 전";
        } else if (ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now()) >= 1) {
            time = Long.toString(ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now())) + "시간 전";
        } else if (ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now()) >= 1) {
            time = Long.toString(ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now())) + "분 전";
        } else {
            time = "방금 전";
        }
        return time;
    }


    private User getValidUser(Long userId) {
        User userEntity = userRepo.findById(userId)
                                  .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
        return userEntity;
    }


    private UserHistory getValidUserHistory(Long userId) {
        UserHistory userHistoryEntity = userHistoryRepo.findByUserId(userId)
                                                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
        return userHistoryEntity;
    }


    private Project getValidProject(Long projectId) {
        Project projectEntity = projectRepo.findById(projectId)
                                           .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT));
        return projectEntity;
    }


    private ProjectApply getValidProjectApply(Long projectApplyId) {
        ProjectApply projectApplyEntity = projectApplyRepo.findById(projectApplyId)
                                                          .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT_APPLY));
        return projectApplyEntity;
    }


    private UserSchool getValidUserSchool(Long userSchoolId) {
        UserSchool userSchoolEntity = userSchoolRepo.findById(userSchoolId)
                                                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT_APPLY_OPTIONAL_INFO));
        return userSchoolEntity;
    }


    private UserCareer getValidUserCareer(Long userCareerId) {
        UserCareer userCareerEntity = userCareerRepo.findById(userCareerId)
                                                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT_APPLY_OPTIONAL_INFO));
        return userCareerEntity;
    }


    private UserLicense getValidUserLicense(Long userLicenseId) {
        UserLicense userLicenseEntity = userLicenseRepo.findById(userLicenseId)
                                                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT_APPLY_OPTIONAL_INFO));
        return userLicenseEntity;
    }


    private UserAward getValidUserAward(Long userAwardId) {
        UserAward userAwardEntity = userAwardRepo.findById(userAwardId)
                                                 .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT_APPLY_OPTIONAL_INFO));
        return userAwardEntity;
    }


    private UserLink getValidUserLink(Long userLinkId) {
        UserLink userLinkEntity = userLinkRepo.findById(userLinkId)
                                              .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT_APPLY_OPTIONAL_INFO));
        return userLinkEntity;
    }


    private boolean isProjectUser(Long userHistoryId, Long projectId) {
        Optional<UserProject> optionalUserProjectEntity = userProjectRepo.findByUserHistoryIdAndProjectId(userHistoryId, projectId);
        return optionalUserProjectEntity.isPresent();
    }


    private boolean isProjectLeader(Long userHistoryId, Long projectId) {
        Optional<UserProject> optionalUserProjectEntity = userProjectRepo.findByUserHistoryIdAndProjectId(userHistoryId, projectId);
        if (!optionalUserProjectEntity.isPresent()) {
            return false;
        } else {
            UserProject userProjectEntity = optionalUserProjectEntity.get();
            return userProjectEntity.getRoleType() == UserProjectRoleType.LEADER;
        }
    }

    private void checkAlreadyProjectApplicant(ProjectApply projectApplyEntity, Long userId) {
        if (projectApplyEntity.getApplicant()
                              .getId()
                              .equals(userId)) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_PROJECT_APPLICANT);
        }
    }


    private void checkProjectTeammate(Long userHistoryId, Long projectId) {
        UserProject UserProjectEntity = userProjectRepo.findByUserHistoryIdAndProjectId(userHistoryId, projectId)
                                                       .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_USER));
    }


    private void checkProjectApplicant(ProjectApply projectApplyEntity, Long userId) {
        if (!projectApplyEntity.getApplicant()
                               .getId()
                               .equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
    }


    private boolean isExistUserProject(Long userHistoryId, Long projectId) {
        return userProjectRepo.findByUserHistoryIdAndProjectId(userHistoryId, projectId)
                              .isPresent();
    }


    private void checkValidProjectApplyStatus(ProjectApplyStatus actualStatus, ProjectApplyStatus expectedStatus) {
        if (actualStatus != expectedStatus) {
            throw new CustomException(ErrorCode.INVALID_PROJECT_APPLY_STATUS_CHANGE);
        }
    }


    private void setUserPortfolio(ProjectApply projectApplyEntity,
                                  List<UserSchoolResponseDto> userSchoolResponseDtoList,
                                  List<UserCareerResponseDto> userCareerResponseDtoList,
                                  List<UserLicenseResponseDto> userLicenseResponseDtoList,
                                  List<UserAwardResponseDto> userAwardResponseDtoList,
                                  List<UserLinkResponseDto> userLinkResponseDtoList)
    {
        projectApplyEntity.getOptionalApplyInfoList()
                          .forEach(optionalApplyInfo -> {
                              switch (optionalApplyInfo.getUserPortfolioType()) {
                                  case SCHOOL:
                                      userSchoolResponseDtoList.add(
                                              new UserSchoolResponseDto(getValidUserSchool(optionalApplyInfo.getUserSelectedPortfolioId()))
                                      );
                                      break;
                                  case CAREER:
                                      userCareerResponseDtoList.add(
                                              new UserCareerResponseDto(getValidUserCareer(optionalApplyInfo.getUserSelectedPortfolioId()))
                                      );
                                      break;
                                  case LICENSE:
                                      userLicenseResponseDtoList.add(
                                              new UserLicenseResponseDto(getValidUserLicense(optionalApplyInfo.getUserSelectedPortfolioId()))
                                      );
                                      break;
                                  case AWARD:
                                      userAwardResponseDtoList.add(
                                              new UserAwardResponseDto(getValidUserAward(optionalApplyInfo.getUserSelectedPortfolioId()))
                                      );
                                      break;
                                  case LINK:
                                      userLinkResponseDtoList.add(
                                              new UserLinkResponseDto(getValidUserLink(optionalApplyInfo.getUserSelectedPortfolioId()))
                                      );
                                      break;
                              }
                          });
    }


    private String getProjectTitle(String projectTitle) {
        if (projectTitle.length() > 10) {
            projectTitle = projectTitle.substring(0, 10) + "...";
        }
        return projectTitle;
    }


    private User getLeader(Project projectEntity) {
        return projectEntity.getUserProjectList()
                            .stream()
                            .filter(userProject -> userProject.getRoleType() == UserProjectRoleType.LEADER)
                            .findFirst()
                            .orElseThrow(() -> new CustomException(ErrorCode.NON_EXIST_PROJECT_LEADER))
                            .getUserHistory()
                            .getUser();
    }
}