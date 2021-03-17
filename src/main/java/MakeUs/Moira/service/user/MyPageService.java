package MakeUs.Moira.service.user;

import MakeUs.Moira.advice.exception.InvalidUserIdException;

import MakeUs.Moira.controller.user.dto.AppliedProjectInfoResponseDto;
import MakeUs.Moira.controller.user.dto.LikedProjectResponseDto;
import MakeUs.Moira.controller.user.dto.MyPageResponseDto;

import MakeUs.Moira.controller.user.dto.WrittenProjectInfoResponseDto;
import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.ProjectLike;
import MakeUs.Moira.domain.project.projectApply.ProjectApply;
import MakeUs.Moira.domain.project.projectApply.ProjectApplyRepo;
import MakeUs.Moira.domain.user.*;
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


    public MyPageResponseDto getMyPage(Long userId) {

        User userEntity = userRepo.findById(userId)
                                  .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));

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

        return new MyPageResponseDto(userEntity, writtenPostCount, likedPostCount, appliedPostCount);
    }


    public List<WrittenProjectInfoResponseDto> getWrittenProjectList(Long userId) {

        UserHistory userHistoryEntity = userHistoryRepo.findByUserId(userId)
                                                       .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));

        List<Project> writtenProjectList = userHistoryEntity.getUserProjects()
                                                            .stream()
                                                            .filter(userProject -> userProject.getRoleType() == UserProjectRoleType.LEADER)
                                                            .map(UserProject::getProject)
                                                            .collect(Collectors.toList());

        return writtenProjectList.stream()
                                 .map(WrittenProjectInfoResponseDto::new)
                                 .collect(Collectors.toList());
    }


    public List<AppliedProjectInfoResponseDto> getAppliedProjectList(Long userId) {

        List<ProjectApply> projectApplyList = projectApplyRepo.findAllByApplicant_Id(userId);
        List<Project> appliedProjectList = projectApplyList.stream()
                                                           .map(projectApply -> projectApply.getProjectDetail()
                                                                                            .getProject())
                                                           .collect(Collectors.toList());
        return appliedProjectList.stream()
                                 .map(AppliedProjectInfoResponseDto::new)
                                 .collect(Collectors.toList());
    }


    public List<LikedProjectResponseDto> getLikedProjectList(Long userId, String positionCategory,
                                                             String sortKeyword) {

        UserHistory userHistoryEntity = userHistoryRepo.findByUserId(userId)
                                                       .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));

        String positionCategoryFilter = positionCategoryFilterParsing(positionCategory);
        List<Project> likedProjectFilteredList = userHistoryEntity.getProjectLikes()
                                                                  .stream()
                                                                  .map(ProjectLike::getProject)
                                                                  .filter(likedProject -> likedProject.isRecruitingPositionCategory(positionCategoryFilter))
                                                                  .collect(Collectors.toList());
        sortByKeyword(sortKeyword, likedProjectFilteredList);
        return likedProjectFilteredList.stream()
                                       .map(LikedProjectResponseDto::new)
                                       .collect(Collectors.toList());
    }

    private String positionCategoryFilterParsing(String positionCategoryFilter) {
        switch (positionCategoryFilter) {
            case "develop":
                positionCategoryFilter = "개발자";
                break;
            case "director":
                positionCategoryFilter = "기획자";
                break;
            case "designer":
                positionCategoryFilter = "디자이너";
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 positionCategory");
        }
        return positionCategoryFilter;
    }

    private void sortByKeyword(String sortKeyword, List<Project> likedProjectFilteredList) {
        if (sortKeyword.equals("date")) {
            likedProjectFilteredList.sort(Comparator.comparing(AuditorEntity::getCreatedDate));
        } else if (sortKeyword.equals("hit")) {
            likedProjectFilteredList.sort(Comparator.comparing(Project::getHitCount));
        }
    }
}
