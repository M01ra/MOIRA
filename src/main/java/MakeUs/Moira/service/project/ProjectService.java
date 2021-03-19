package MakeUs.Moira.service.project;

import MakeUs.Moira.advice.exception.ProjectException;
import MakeUs.Moira.controller.project.dto.*;
import MakeUs.Moira.domain.hashtag.Hashtag;
import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.domain.position.UserPosition;
import MakeUs.Moira.domain.position.PositionRepo;
import MakeUs.Moira.domain.project.*;
import MakeUs.Moira.domain.project.projectDetail.*;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final HashtagRepo hashtagRepo;
    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final UserHistoryRepo userHistoryRepo;
    private final UserProjectRepo userProjectRepo;
    private final PositionRepo positionRepo;
    private final ProjectLikeRepo projectLikeRepo;
    private final S3Service s3Service;


    @Transactional
    public Long createProject(ProjectRequestDTO projectRequestDTO, Long userId) {
        User userEntity = getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);

        Project projectEntity = new Project();
        // 제목(이름)
        projectEntity.updateProjectTitle(projectRequestDTO.getProjectTitle());
        projectEntity = projectRepo.save(projectEntity);

        // 태그
        for(String hashtagName : projectRequestDTO.getProjectHashtagList()){
            projectEntity.addProjectHashtagList(
                    ProjectHashtag.builder()
                            .projectHashtag(getValidHashtag(hashtagName))
                            .project(projectEntity)
                            .build()
            );
        }

        // ProjectDetail 생성
        ProjectDetail projectDetailEntity = ProjectDetail.builder()
                .project(projectEntity)
                .projectContent(projectRequestDTO.getProjectContent())
                .projectDuration(projectRequestDTO.getProjectDuration())
                .projectLocalType(projectRequestDTO.getProjectLocalType())
                .build();

        // 가입 질문 리스트
        for (String projectQuestion : projectRequestDTO.getProjectQuestionList()) {
            projectDetailEntity.addProjectQuestion(
                    ProjectQuestion.builder()
                            .projectQuestion(projectQuestion)
                            .projectDetail(projectDetailEntity)
                            .build()
            );
        }

        // 프로젝트 포지션 리스트
        for (ProjectPositonDTO projectPositonDTO : projectRequestDTO.getProjectPositionList()) {
            UserPosition userPositionEntity = positionRepo.findByPositionName(projectPositonDTO.getPositionName())
                    .orElseThrow(() -> new ProjectException("존재하지 않은 포지션"));

            projectDetailEntity.addProjectPosition(
                    ProjectPosition.builder()
                            .projectDetail(projectDetailEntity)
                            .recruitPositionCount(projectPositonDTO.getCount())
                            .recruitUserPosition(userPositionEntity)
                            .build()
            );
        }

        projectEntity.updateProjectDetail(projectDetailEntity);

        // UserProject 양방향 추가
        UserProject userProject = UserProject.builder()
                .project(projectEntity)
                .userProjectStatus(UserProjectStatus.PROGRESS)
                .userHistory(userHistoryEntity)
                .roleType(UserProjectRoleType.LEADER)
                .userPosition(userEntity.getUserPosition())
                .build();
        projectEntity.addUserProjectList(userProject);
        userHistoryEntity.addUserProject(userProject);

        return projectEntity.getId();
    }


    @Transactional
    public void uploadImage(MultipartFile file, Long projectId, Long userId) {
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = getValidProject(projectId);
        getAuthorizedUserProject(userHistoryEntity.getId(), projectId);

        if(file == null){
            throw new ProjectException("존재하지 않는 파일");
        }
        // 기존에 존재했을 경우 삭제
        if(projectEntity.getProjectImageUrl() != null){
            s3Service.delete("project" + projectId);
        }
        String imageUrl = s3Service.upload(file, "project-" + projectId);
        projectEntity.updateProjectImageUrl(imageUrl);
    }


    @Transactional
    public void changeProjectStatus(Long projectId, ProjectStatus status, Long userId){
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = getValidProject(projectId);
        getAuthorizedUserProject(userHistoryEntity.getId(), projectId);

        projectEntity.updateProjectStatus(status);
    }


    @Transactional
    public void changeProjectTitle(Long projectId, String title, Long userId){
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = getValidProject(projectId);
        getAuthorizedUserProject(userHistoryEntity.getId(), projectId);

        projectEntity.updateProjectTitle(title);
    }


    @Transactional
    public void changeProjectLike(Long projectId, Long userId){
        Project projectEntity = getValidProject(projectId);
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);

        // 좋아요 상태 변경
        Optional<ProjectLike> optionalProjectLike = projectLikeRepo.findByUserHistoryAndProject(userHistoryEntity, projectEntity);
        if(optionalProjectLike.isPresent()){
            ProjectLike projectLikeEntity = optionalProjectLike.get();
            projectLikeEntity.changeProjectLiked();
        }
        else{
            ProjectLike projectLike = ProjectLike.builder()
                    .project(projectEntity)
                    .userHistory(userHistoryEntity)
                    .build();
            projectLikeRepo.save(projectLike);
            projectEntity.addLike();
            userHistoryEntity.addProjectLike(projectLike);
        }
    }


    @Transactional
    public List<ProjectsResponseDTO> getProjects(String tag, String sort, int page) {
        isValidSort(sort);
        // 10개씩 page부터 sort 정렬방식
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort).descending());
        List<ProjectRepo.ProjectsResponseInterface> projectsResponseInterfaceList;

        // 검색 조건에 태그가 있을 경우
        if(tag != null) {
            String[] tags = tag.split("\\,");
            for (String tagName : tags) {
                getValidHashtag(tagName);
            }
            projectsResponseInterfaceList = projectRepo.findProjectsByTagOrderPage(tags, pageable);
        }
        // 검색 조건에 태그가 없을 경우
        else {
            projectsResponseInterfaceList = projectRepo.findProjectsByOrderPage(pageable);
        }

        return projectsResponseInterfaceList
                .stream()
                .map(projectsResponseDTO -> {
                    Project projectEntity = getValidProject(projectsResponseDTO.getId());
                    return ProjectsResponseDTO.builder()
                            .id(projectsResponseDTO.getId())
                            .title(projectsResponseDTO.getTitle())
                            .writer(projectsResponseDTO.getWriter())
                            .imageUrl(projectsResponseDTO.getImageUrl())
                            .hitCount(projectsResponseDTO.getHitCount())
                            .time(getTime(projectsResponseDTO.getModifiedDate()))
                            .hashtagList(getHashtagList(projectEntity.getProjectHashtagList()))
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public ProjectResponseDTO getProject(Long projectId, Long userId){
        Project projectEntity = getValidProject(projectId);
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);

        // 로그인한 유저가 좋아요를 눌렀는지 확인
        boolean isLike = isUserLikeProject(userHistoryEntity, projectEntity);

        // 조회수 증가
        projectEntity.addHit();

        return ProjectResponseDTO.builder()
                .title(projectEntity.getProjectTitle())
                .writer(getWriter(projectEntity.getUserProjectList()))
                .projectHashtagList(getHashtagList(projectEntity.getProjectHashtagList()))
                .imageUrl(projectEntity.getProjectImageUrl())
                .hitCount(projectEntity.getHitCount())
                .likeCount(projectEntity.getLikeCount())
                .duration(projectEntity.getProjectDetail().getProjectDuration().toString())
                .location(projectEntity.getProjectDetail().getProjectLocalType().toString())
                .positionList(getProjectPositionList(projectEntity.getProjectDetail().getProjectPositionList()))
                .time(getTime(projectEntity.getModifiedDate()))
                .isLike(isLike)
                .build();
    }


    private List<String> getHashtagList(List<ProjectHashtag> projectHashtagList){
        List<String> hashtagList = new ArrayList<>();
        for(ProjectHashtag projectHashtag : projectHashtagList){
            hashtagList.add(projectHashtag.getProjectHashtag().getHashtagName());
        }
        return hashtagList;
    }


    private List<ProjectPositonDTO> getProjectPositionList(List<ProjectPosition> projectPositionList){
        List<ProjectPositonDTO> projectPositonDTOList = new ArrayList<>();
        for(ProjectPosition projectPosition: projectPositionList){
            projectPositonDTOList.add(new ProjectPositonDTO(projectPosition.getRecruitUserPosition().getPositionName(), projectPosition.getRecruitPositionCount()));
        }
        return projectPositonDTOList;
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


    private String getWriter(List<UserProject> userProjectList){
        String writer = null;
        for(UserProject userProject : userProjectList){
            if(userProject.getRoleType() == UserProjectRoleType.LEADER){
                Optional<User> optionalUser = userRepo.findById(userProject.getUserHistory().getUser().getId());
                if(!optionalUser.isPresent()){
                    throw new ProjectException("존재하지 않는 유저");
                }
                writer = optionalUser.get().getNickname();
                break;
            }
        }
        return writer;
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


    private UserProject getAuthorizedUserProject(Long userHistoryId, Long projectId){
        UserProject userProjectEntity = userProjectRepo.findByUserHistoryIdAndProjectId(userHistoryId, projectId)
                .orElseThrow(() -> new ProjectException("프로젝트에 가입되지 않은 유저"));
        if(userProjectEntity.getRoleType() != UserProjectRoleType.LEADER){
            throw new ProjectException("권한이 없는 유저");
        }
        return userProjectEntity;
    }


    private Hashtag getValidHashtag(String hashtagName){
        Hashtag hashtag = hashtagRepo.findHashtagByHashtagName(hashtagName)
                .orElseThrow(() -> new ProjectException("존재하지 않는 태그"));
        return hashtag;
    }


    private void isValidSort(String sort){
        if(!sort.equals("modifiedDate") && !sort.equals("hitCount") && !sort.equals("likeCount")){
            throw new ProjectException("유효하지 않는 정렬 방식");
        }
    }


    private boolean isUserLikeProject(UserHistory userHistory, Project project){
        Optional<ProjectLike> optionalProjectLike = projectLikeRepo.findByUserHistoryAndProject(userHistory, project);
        if(optionalProjectLike.isPresent()) {
            ProjectLike projectLike = optionalProjectLike.get();
            return projectLike.isProjectLiked();
        }
        return false;
    }

}
