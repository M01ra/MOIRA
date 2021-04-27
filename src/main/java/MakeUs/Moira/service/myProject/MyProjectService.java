package MakeUs.Moira.service.myProject;

import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.myProject.dto.MyProjectResponseDTO;
import MakeUs.Moira.controller.myProject.dto.MyProjectTeammateResponseDTO;
import MakeUs.Moira.controller.myProject.dto.MyProjectsResponseDTO;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.ProjectRepo;
import MakeUs.Moira.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyProjectService {

    private final ProjectRepo projectRepo;
    private final UserHistoryRepo userHistoryRepo;


    @Transactional
    public List<MyProjectsResponseDTO> getMyProjects(Long userId, String sort, UserProjectStatus status) {
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        List<MyProjectsResponseDTO> myProjectResponseDTOList =  userHistoryEntity.getUserProjects()
                                                                                 .stream()
                                                                                 .filter(userProject -> userProject.getUserProjectStatus() == status)
                                                                                 .map(userProject -> {
                                                                                     Project projectEntity = userProject.getProject();
                                                                                     return MyProjectsResponseDTO.builder()
                                                                                                                 .projectId(projectEntity.getId())
                                                                                                                 .imageUrl(projectEntity.getProjectImageUrl())
                                                                                                                 .title(projectEntity.getProjectTitle())
                                                                                                                 .date(projectEntity.getCreatedDate())
                                                                                                                 .memberCount(getMemberCount(projectEntity))
                                                                                                                 .isMembersReviewed(getIsMembersReviwed(projectEntity, userProject, userId))
                                                                                                                 .build();
                                                                                 })
                                                                                 .collect(Collectors.toList());

        // 입력에 맞는 정렬
        if(sort.equals("date")) myProjectResponseDTOList.sort(Comparator.comparing(MyProjectsResponseDTO::getDate).reversed());
        else if(sort.equals("character")) myProjectResponseDTOList.sort(Comparator.comparing(MyProjectsResponseDTO::getTitle));
        else throw new CustomException(ErrorCode.INVALID_SORT);
        return myProjectResponseDTOList;
    }


    @Transactional
    public MyProjectResponseDTO getMyProject(Long projectId, Long userId) {
        Project projectEntity = getValidProject(projectId);

        // 팀원 리스트
        List<MyProjectTeammateResponseDTO> myProjectTeammateResponseDTOList = projectEntity.getUserProjectList()
                     .stream()
                     .filter(userProject -> !userProject.getId().equals(userId))
                     .map(userProject -> {
                         User userEntity = userProject.getUserHistory().getUser();
                         if(userProject.getRoleType() != UserProjectRoleType.LEADER) {
                             return MyProjectTeammateResponseDTO.builder()
                                                                .userId(userEntity.getId())
                                                                .projectApplyId(projectEntity.getProjectDetail()
                                                                                             .getProjectApplyList()
                                                                                             .stream()
                                                                                             .filter(projectApply -> projectApply.getApplicant()
                                                                                                                                 .getId()
                                                                                                                                 .equals(userEntity.getId()))
                                                                                             .findFirst()
                                                                                             .orElseThrow(() -> new CustomException(ErrorCode.NON_EXIST_PROJECT_APPLY))
                                                                                             .getId())
                                                                .imageUrl(userEntity.getProfileImage())
                                                                .isLeader(userProject.getRoleType() == UserProjectRoleType.LEADER)
                                                                .nickname(userEntity.getNickname())
                                                                .position(userProject.getUserPosition()
                                                                                     .getPositionName())
                                                                .build();
                         }
                         else{
                             return MyProjectTeammateResponseDTO.builder()
                                                                .userId(userEntity.getId())
                                                                .projectApplyId(-1L)
                                                                .imageUrl(userEntity.getProfileImage())
                                                                .isLeader(userProject.getRoleType() == UserProjectRoleType.LEADER)
                                                                .nickname(userEntity.getNickname())
                                                                .position(userProject.getUserPosition()
                                                                                     .getPositionName())
                                                                .build();
                         }
                     })
                     .collect(Collectors.toList());


        return MyProjectResponseDTO.builder()
                                   .title(projectEntity.getProjectTitle())
                                   .imageUrlList(projectEntity.getProjectImageUrlList())
                                   .content(projectEntity.getProjectDetail().getProjectContent())
                                   .memberCount(myProjectTeammateResponseDTOList.size())
                                   .isLeader(getLeader(projectEntity).getId().equals(userId))
                                   .myProjectTeammateResponseDTOList(myProjectTeammateResponseDTOList)
                                   .build();
    }


    private Project getValidProject(Long projectId){
        Project projectEntity = projectRepo.findById(projectId)
                                           .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT));
        return projectEntity;
    }


    private UserHistory getValidUserHistory(Long userId){
        UserHistory userHistoryEntity = userHistoryRepo.findByUserId(userId)
                                                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
        return userHistoryEntity;
    }


    private int getMemberCount(Project projectEntity){
        Long memberCount = projectEntity.getUserProjectList()
                     .stream()
                     .filter(myUserProject -> myUserProject.getUserProjectStatus() != UserProjectStatus.DROP)
                     .count();
        return memberCount.intValue();
    }


    private boolean getIsMembersReviwed(Project projectEntity, UserProject userProject, Long userId){
        return projectEntity.getUserProjectList().stream()
                     .filter(anotherUserProject -> !anotherUserProject.getId().equals(userProject.getId()))
                     .allMatch(anotherUserProject -> anotherUserProject.getReviews()
                                                                       .stream()
                                                                       .anyMatch(userReview -> userReview.getWrittenUser().getId().equals(userId)));
    }


    private User getLeader(Project projectEntity){
        return projectEntity.getUserProjectList().stream()
                            .filter(userProject -> userProject.getRoleType() == UserProjectRoleType.LEADER)
                            .findFirst()
                            .orElseThrow(() -> new CustomException(ErrorCode.NON_EXIST_PROJECT_LEADER))
                            .getUserHistory()
                            .getUser();
    }

}
