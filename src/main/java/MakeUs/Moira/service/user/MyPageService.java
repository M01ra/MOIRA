package MakeUs.Moira.service.user;

import MakeUs.Moira.advice.exception.InvalidUserIdException;

import MakeUs.Moira.controller.user.dto.MyPageResponseDto;

import MakeUs.Moira.controller.user.dto.WrittenProjectInfoResponseDto;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.projectApply.ProjectApplyRepo;
import MakeUs.Moira.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserRepo userRepo;
    private final UserHistoryRepo userHistoryRepo;
    private final ProjectApplyRepo projectApplyRepo;

    public MyPageResponseDto getMyPage(Long userId) {

        User userEntity = userRepo.findById(userId)
                                  .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));

        UserHistory userHistoryEntity = userEntity.getUserHistory();

        int writtenPostCount = (int) userHistoryEntity.getUserProjects()
                                                      .stream()
                                                      .filter(userProject -> userProject.getRoleType() == UserProjectRoleType.LEADER)
                                                      .count();

        int likedPostCount = 0;
        likedPostCount += userHistoryEntity.getProjectLikes()
                                           .size();
        likedPostCount += userHistoryEntity.getUserPoolLikes()
                                           .size();

        int appliedPostCount = projectApplyRepo.countByApplicant_Id(userId);


        return MyPageResponseDto.builder()
                                .nickname(userEntity.getNickname())
                                .positionName(userEntity.getUserPosition()
                                                        .getPositionName())
                                .shortIntroduction(userEntity.getShortIntroduction())
                                .profileImageUrl(userEntity.getProfileImage())
                                .writtenPostCount(writtenPostCount)
                                .appliedPostCount(appliedPostCount)
                                .likedPostCount(likedPostCount)
                                .build();
    }


    public List<WrittenProjectInfoResponseDto> getWrittenPost(Long userId) {
        User userEntity = userRepo.findById(userId)
                                  .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));

        UserHistory userHistoryEntity = userEntity.getUserHistory();
        List<Project> writtenProjectList = userHistoryEntity.getUserProjects()
                                                      .stream()
                                                      .filter(userProject -> userProject.getRoleType() == UserProjectRoleType.LEADER)
                                                      .map(UserProject::getProject)
                                                      .collect(Collectors.toList());


        return writtenProjectList.stream()
                                 .map((writtenProject) -> WrittenProjectInfoResponseDto.builder()
                                                                                       .projectTitle(writtenProject.getProjectTitle())
                                                                                       .writtenTime(writtenProject.getCreatedDate()
                                                                                                                  .toLocalDate())
                                                                                       .hitCount(writtenProject.getHitCount())
                                                                                       .projectImageUrl(writtenProject.getProjectImageUrl())
                                                                                       .applicantCount(writtenProject.getProjectDetail()
                                                                                                                     .getProjectApplyList()
                                                                                                                     .size())
                                                                                       .build())
                                 .collect(Collectors.toList());
    }
}
