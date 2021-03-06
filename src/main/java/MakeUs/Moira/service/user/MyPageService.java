package MakeUs.Moira.service.user;

import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;

import MakeUs.Moira.controller.user.dto.myPage.*;
import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.ProjectLike;
import MakeUs.Moira.domain.project.projectApply.ProjectApply;
import MakeUs.Moira.domain.project.projectApply.ProjectApplyRepo;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.userPool.UserPool;
import MakeUs.Moira.domain.userPool.UserPoolLike;
import MakeUs.Moira.domain.userPool.UserPoolRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserRepo         userRepo;
    private final UserHistoryRepo  userHistoryRepo;
    private final ProjectApplyRepo projectApplyRepo;
    private final UserPoolRepo     userPoolRepo;


    public MyPageResponseDto getMyPage(Long userId) {

        User userEntity = userRepo.findById(userId)
                                  .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));

        int writtenPostCount = (int) userEntity.getUserHistory()
                                               .getUserProjects()
                                               .stream()
                                               .filter(userProject -> userProject.getRoleType() == UserProjectRoleType.LEADER)
                                               .count();

        int likedPostCount = 0;
        likedPostCount += userEntity.getUserHistory()
                                    .getProjectLikes()
                                    .size();
        likedPostCount += userEntity.getUserHistory()
                                    .getUserPoolLikes()
                                    .size();

        int appliedPostCount = projectApplyRepo.countByApplicant_Id(userId);

        return new MyPageResponseDto(userEntity, writtenPostCount, appliedPostCount, likedPostCount);
    }


    public List<WrittenProjectInfoResponseDto> getWrittenProjectList(Long userId) {

        UserHistory userHistoryEntity = userHistoryRepo.findByUserId(userId)
                                                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));

        List<Project> writtenProjectList = userHistoryEntity.getUserProjects()
                                                            .stream()
                                                            .filter(userProject -> userProject.getRoleType().equals(UserProjectRoleType.LEADER) )
                                                            .map(UserProject::getProject)
                                                            .collect(Collectors.toList());

        return writtenProjectList.stream()
                                 .map(WrittenProjectInfoResponseDto::new)
                                 .collect(Collectors.toList());
    }


    public List<AppliedProjectInfoResponseDto> getAppliedProjectList(Long userId) {
        List<ProjectApply> projectApplyList = projectApplyRepo.findAllByApplicant_Id(userId);
        return projectApplyList.stream()
                               .map(AppliedProjectInfoResponseDto::new)
                               .collect(Collectors.toList());
    }


    public List<LikedProjectResponseDto> getLikedProjectList(Long userId, String positionCategory, String sortKeyword) {

        UserHistory userHistoryEntity = userHistoryRepo.findByUserId(userId)
                                                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));

        List<Project> likedProjectFilteredList = userHistoryEntity.getProjectLikes()
                                                                  .stream()
                                                                  .map(ProjectLike::getProject)
                                                                  .filter(likedProject -> likedProject.isRecruitingPositionCategory(positionCategory))
                                                                  .collect(Collectors.toList());
        sortProjectByKeyword(sortKeyword, likedProjectFilteredList);
        return likedProjectFilteredList.stream()
                                       .map(LikedProjectResponseDto::new)
                                       .collect(Collectors.toList());
    }


    public List<LikedUserPoolResponseDto> getLikedUserPool(Long userId, String positionCategory, String sortKeyword) {

        UserHistory userHistoryEntity = getUserEntity(userId).getUserHistory();

        List<UserPool> likedUserPoolFilteredList = userHistoryEntity.getUserPoolLikes()
                                                                    .stream()
                                                                    .map(UserPoolLike::getUserPool)
                                                                    .filter(likedUserPool -> likedUserPool.isDesiredPositionCategory(positionCategory))
                                                                    .collect(Collectors.toList());
        sortUserPoolByKeyword(sortKeyword, likedUserPoolFilteredList);
        return likedUserPoolFilteredList.stream()
                                        .map(LikedUserPoolResponseDto::new)
                                        .collect(Collectors.toList());
    }

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }

    private void sortProjectByKeyword(String sortKeyword, List<Project> likedProjectFilteredList) {
        switch (sortKeyword) {
            case "date":
                likedProjectFilteredList.sort(Comparator.comparing(AuditorEntity::getCreatedDate)
                                                        .reversed());
                break;
            case "hit":
                likedProjectFilteredList.sort(Comparator.comparing(Project::getHitCount)
                                                        .reversed());
                break;
            case "like":
                likedProjectFilteredList.sort(Comparator.comparing(Project::getLikeCount)
                                                        .reversed());
                break;
            default:
                throw new CustomException(ErrorCode.INVALID_SORT);
        }
    }

    private void sortUserPoolByKeyword(String sortKeyword, List<UserPool> likedUserPoolFilteredList) {
        switch (sortKeyword) {
            case "date":
                likedUserPoolFilteredList.sort(Comparator.comparing(AuditorEntity::getCreatedDate)
                                                         .reversed());
                break;
            case "hit":
                likedUserPoolFilteredList.sort(Comparator.comparing(UserPool::getHitCount)
                                                         .reversed());
                break;
            case "like":
                likedUserPoolFilteredList.sort(Comparator.comparing(UserPool::getLikeCount)
                                                         .reversed());
                break;
        }
    }

}
