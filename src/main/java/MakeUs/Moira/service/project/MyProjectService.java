package MakeUs.Moira.service.project;

import MakeUs.Moira.advice.exception.ProjectException;
import MakeUs.Moira.controller.project.dto.myProject.MyProjectResponseDTO;
import MakeUs.Moira.controller.project.dto.myProject.MyProjectTeammateResponseDTO;
import MakeUs.Moira.controller.project.dto.myProject.MyProjectsResponseDTO;
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
        checkValidSort(sort);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        List<MyProjectsResponseDTO> myProjectResponseDTOList =  userHistoryEntity.getUserProjects()
                                                                                 .stream()
                                                                                 .filter(userProject -> userProject.getUserProjectStatus() == status)
                                                                                 .map(userProject -> {
                                                                                     Project projectEntity = userProject.getProject();
                                                                                     Long memberCount = projectEntity.getUserProjectList()
                                                                                                                     .stream()
                                                                                                                     .filter(myUserProject -> myUserProject.getUserProjectStatus() != UserProjectStatus.DROP)
                                                                                                                     .count();
                                                                                     return MyProjectsResponseDTO.builder()
                                                                                                                 .projectId(projectEntity.getId())
                                                                                                                 .imageUrl(projectEntity.getProjectImageUrl())
                                                                                                                 .title(projectEntity.getProjectTitle())
                                                                                                                 .date(projectEntity.getCreatedDate())
                                                                                                                 .memberCount(memberCount.intValue())
                                                                                                                 .build();
                                                                                 })
                                                                                 .collect(Collectors.toList());

        // 입력에 맞는 정렬
        if(sort.equals("date")) myProjectResponseDTOList.sort(Comparator.comparing(MyProjectsResponseDTO::getDate).reversed());
        else if(sort.equals("character")) myProjectResponseDTOList.sort(Comparator.comparing(MyProjectsResponseDTO::getTitle));
        else throw new ProjectException("존재하지 않는 정렬 기준");
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
                         return MyProjectTeammateResponseDTO.builder()
                                                     .userId(userEntity.getId())
                                                     .imageUrl(userEntity.getProfileImage())
                                                     .isLeader(userProject.getRoleType() == UserProjectRoleType.LEADER)
                                                     .nickname(userEntity.getNickname())
                                                     .position(userProject.getUserPosition().toString())
                                                     .build();
                     })
                     .collect(Collectors.toList());

        return MyProjectResponseDTO.builder()
                                   .title(projectEntity.getProjectTitle())
                                   .imageUrl(projectEntity.getProjectImageUrl())
                                   .content(projectEntity.getProjectDetail().getProjectContent())
                                   .memberCount(myProjectTeammateResponseDTOList.size())
                                   .myProjectTeammateResponseDTOList(myProjectTeammateResponseDTOList)
                                   .build();
    }


    private Project getValidProject(Long projectId){
        Project projectEntity = projectRepo.findById(projectId)
                                           .orElseThrow(() -> new ProjectException("존재하지 않은 프로젝트 ID"));
        return projectEntity;
    }


    private UserHistory getValidUserHistory(Long userId){
        UserHistory userHistoryEntity = userHistoryRepo.findByUserId(userId)
                                                       .orElseThrow(() -> new ProjectException("유효하지 않는 유저"));
        return userHistoryEntity;
    }


    private void checkValidSort(String sort){
        if(!sort.equals("date") && !sort.equals("character")){
            throw new ProjectException("유효하지 않는 정렬 방식");
        }
    }

}
