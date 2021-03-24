package MakeUs.Moira.service.project;

import MakeUs.Moira.advice.exception.InvalidUserIdException;
import MakeUs.Moira.advice.exception.ProjectException;
import MakeUs.Moira.controller.project.dto.ProjectMemberResponseDto;
import MakeUs.Moira.controller.project.dto.project.ProjectPositionCategoryDTO;
import MakeUs.Moira.controller.project.dto.project.ProjectRequestDTO;
import MakeUs.Moira.controller.project.dto.project.ProjectResponseDTO;
import MakeUs.Moira.controller.project.dto.project.ProjectsResponseDTO;
import MakeUs.Moira.domain.hashtag.Hashtag;
import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.domain.position.PositionCategory;
import MakeUs.Moira.domain.position.PositionCategoryRepo;
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

    private final HashtagRepo     hashtagRepo;
    private final ProjectRepo     projectRepo;
    private final UserRepo        userRepo;
    private final UserHistoryRepo userHistoryRepo;
    private final UserProjectRepo userProjectRepo;
    private final PositionCategoryRepo positionCategoryRepo;
    private final ProjectLikeRepo projectLikeRepo;
    private final S3Service       s3Service;


    @Transactional
    public Long createProject(ProjectRequestDTO projectRequestDTO, Long userId) {
        User userEntity = getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);

        Project projectEntity = new Project();

        // 제목(이름)
        projectEntity.updateProjectTitle(projectRequestDTO.getTitle());
        projectEntity = projectRepo.save(projectEntity);

        // 태그
        for(String hashtagName : projectRequestDTO.getHashtagList()){
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
                .projectContent(projectRequestDTO.getContent())
                .projectDuration(projectRequestDTO.getDuration())
                .projectLocalType(projectRequestDTO.getLocalType())
                .build();


        // 프로젝트 포지션 리스트
        for (ProjectPositionCategoryDTO projectPositionCategoryDTO : projectRequestDTO.getPositionCategoryList()) {
            PositionCategory positionCategoryEntity = positionCategoryRepo.findByCategoryName(projectPositionCategoryDTO.getPositionCategoryName())
                                                                          .orElseThrow(() -> new ProjectException("존재하지 않은 포지션 카테고리"));

            projectDetailEntity.addProjectPosition(
                    ProjectPosition.builder()
                            .projectDetail(projectDetailEntity)
                            .recruitPositionCount(projectPositionCategoryDTO.getCount())
                            .recruitUserPositionCategory(positionCategoryEntity)
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

        if (file == null) {
            throw new ProjectException("존재하지 않는 파일");
        }
        // 기존에 존재했을 경우 삭제
        if (projectEntity.getProjectImageUrl() != null) {
            s3Service.delete("project" + projectId);
        }
        String imageUrl = s3Service.upload(file, "project-" + projectId);
        projectEntity.updateProjectImageUrl(imageUrl);
    }


    @Transactional
    public void changeProjectStatus(Long projectId, ProjectStatus status, Long userId) {
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = getValidProject(projectId);
        getAuthorizedUserProject(userHistoryEntity.getId(), projectId);

        projectEntity.updateProjectStatus(status);
    }


    @Transactional
    public void changeProjectTitle(Long projectId, String title, Long userId) {
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = getValidProject(projectId);
        getAuthorizedUserProject(userHistoryEntity.getId(), projectId);

        projectEntity.updateProjectTitle(title);
    }


    @Transactional
    public void changeProjectLike(Long projectId, Long userId) {
        Project projectEntity = getValidProject(projectId);
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);

        // 좋아요 상태 변경
        Optional<ProjectLike> optionalProjectLike = projectLikeRepo.findByUserHistoryAndProject(userHistoryEntity, projectEntity);
        if (optionalProjectLike.isPresent()) {
            ProjectLike projectLikeEntity = optionalProjectLike.get();
            projectLikeEntity.changeProjectLiked();
        } else {
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
    public List<ProjectsResponseDTO> getProjects(String tag, String sort, String position, int page, String keyword) {
        checkValidSort(sort);
        // 10개씩 page부터 sort 정렬방식
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort)
                                                         .descending());
        List<ProjectRepo.ProjectsResponseInterface> projectsResponseInterfaceList;

        // 키워드가 있으면 키워드만으로 검색
        if(keyword != null){
            projectsResponseInterfaceList = projectRepo.findProjectsByOrderPageKeyword(keyword, pageable);
        }

        // 검색 조건에 태그가 있을 경우
        else if(tag != null) {
            String[] tags = tag.split("\\,");
            for (String tagName : tags) {
                getValidHashtag(tagName);
            }
            // 검색 조건에 포지션이 있을 경우
            if(position != null){
                checkValidPosition(position);
                projectsResponseInterfaceList = projectRepo.findProjectsByOrderPageTagPosition(tags, position, pageable);
            }
            // 검색 조건에 포지션이 없을 경우
            else projectsResponseInterfaceList = projectRepo.findProjectsByOrderPageTag(tags, pageable);
        }
        // 검색 조건에 태그가 없을 경우
        else {
            // 검색 조건에 포지션이 있을 경우
            if(position != null){
                checkValidPosition(position);
                projectsResponseInterfaceList = projectRepo.findProjectsByOrderPagePosition(position, pageable);
            }
            // 검색 조건에 포지션이 없을 경우
            else projectsResponseInterfaceList = projectRepo.findProjectsByOrderPage(pageable);
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
                            .time(getTime(projectsResponseDTO.getDate()))
                            .hashtagList(getHashtagList(projectEntity.getProjectHashtagList()))
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public ProjectResponseDTO getProject(Long projectId, Long userId) {
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
                .hashtagList(getHashtagList(projectEntity.getProjectHashtagList()))
                .imageUrl(projectEntity.getProjectImageUrl())
                .hitCount(projectEntity.getHitCount())
                .likeCount(projectEntity.getLikeCount())
                .duration(projectEntity.getProjectDetail().getProjectDuration().toString())
                .location(projectEntity.getProjectDetail().getProjectLocalType().toString())
                .positionCategoryList(getProjectPositionCategoryList(projectEntity.getProjectDetail().getProjectPositionList()))
                .time(getTime(projectEntity.getModifiedDate()))
                .isLike(isLike)
                .build();
    }


    public List<ProjectMemberResponseDto> getProjectMember(Long userId, Long projectId) {
        List<UserProject> userProjectEntityList = userProjectRepo.findAllByProject_Id(projectId);
        // 자기는 빼고, 리턴
        return userProjectEntityList.stream()
                                    .filter(userProject -> !userProject.getUserHistory()
                                                                       .getUser()
                                                                       .getId()
                                                                       .equals(userId))
                                    .map(userProject -> new ProjectMemberResponseDto(userId, userProject))
                                    .collect(Collectors.toList());
    }

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));
    }


    private List<String> getHashtagList(List<ProjectHashtag> projectHashtagList) {
        List<String> hashtagList = new ArrayList<>();
        for (ProjectHashtag projectHashtag : projectHashtagList) {
            hashtagList.add(projectHashtag.getProjectHashtag()
                                          .getHashtagName());
        }
        return hashtagList;
    }


    private List<ProjectPositionCategoryDTO> getProjectPositionCategoryList(List<ProjectPosition> projectPositionCategoryList){
        List<ProjectPositionCategoryDTO> projectPositionCategoryDTOList = new ArrayList<>();
        for(ProjectPosition projectPosition: projectPositionCategoryList){
            projectPositionCategoryDTOList.add(new ProjectPositionCategoryDTO(projectPosition.getRecruitingPositionCategoryName(), projectPosition.getRecruitPositionCount()));
        }
        return projectPositionCategoryDTOList;
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


    private String getWriter(List<UserProject> userProjectList) {
        String writer = null;
        for (UserProject userProject : userProjectList) {
            if (userProject.getRoleType() == UserProjectRoleType.LEADER) {
                Optional<User> optionalUser = userRepo.findById(userProject.getUserHistory()
                                                                           .getUser()
                                                                           .getId());
                if (!optionalUser.isPresent()) {
                    throw new ProjectException("존재하지 않는 유저");
                }
                writer = optionalUser.get()
                                     .getNickname();
                break;
            }
        }
        return writer;
    }


    private User getValidUser(Long userId) {
        User userEntity = userRepo.findById(userId)
                                  .orElseThrow(() -> new ProjectException("유효하지 않는 유저"));
        return userEntity;
    }


    private UserHistory getValidUserHistory(Long userId) {
        UserHistory userHistoryEntity = userHistoryRepo.findByUserId(userId)
                                                       .orElseThrow(() -> new ProjectException("유효하지 않는 유저"));
        return userHistoryEntity;
    }


    private Project getValidProject(Long projectId) {
        Project projectEntity = projectRepo.findById(projectId)
                                           .orElseThrow(() -> new ProjectException("존재하지 않은 프로젝트 ID"));
        return projectEntity;
    }


    private UserProject getAuthorizedUserProject(Long userHistoryId, Long projectId) {
        UserProject userProjectEntity = userProjectRepo.findByUserHistoryIdAndProjectId(userHistoryId, projectId)
                                                       .orElseThrow(() -> new ProjectException("프로젝트에 가입되지 않은 유저"));
        if (userProjectEntity.getRoleType() != UserProjectRoleType.LEADER) {
            throw new ProjectException("권한이 없는 유저");
        }
        return userProjectEntity;
    }


    private Hashtag getValidHashtag(String hashtagName) {
        Hashtag hashtag = hashtagRepo.findHashtagByHashtagName(hashtagName)
                                     .orElseThrow(() -> new ProjectException("존재하지 않는 태그"));
        return hashtag;
    }


    private void checkValidSort(String sort){
        if(!sort.equals("date") && !sort.equals("hitCount") && !sort.equals("likeCount")){
            throw new ProjectException("유효하지 않는 정렬 방식");
        }
    }


    private void checkValidPosition(String position){
        if(!position.equals("개발자") && !position.equals("기획자") && !position.equals("디자이너")){
            throw new ProjectException("유효하지 않는 포지션 이름");
        }
    }


    private boolean isUserLikeProject(UserHistory userHistory, Project project){
        Optional<ProjectLike> optionalProjectLike = projectLikeRepo.findByUserHistoryAndProject(userHistory, project);
        if (optionalProjectLike.isPresent()) {
            ProjectLike projectLike = optionalProjectLike.get();
            return projectLike.isProjectLiked();
        }
        return false;
    }


}
