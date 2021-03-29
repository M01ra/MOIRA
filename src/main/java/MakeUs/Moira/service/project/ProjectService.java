package MakeUs.Moira.service.project;

import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.controller.project.dto.ProjectMemberResponseDto;
import MakeUs.Moira.controller.project.dto.project.*;
import MakeUs.Moira.domain.hashtag.Hashtag;
import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.domain.position.PositionCategory;
import MakeUs.Moira.domain.position.PositionCategoryRepo;
import MakeUs.Moira.domain.project.*;
import MakeUs.Moira.domain.project.projectDetail.*;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
        if(projectRequestDTO.getHashtagList() != null){
            for(String hashtagName : projectRequestDTO.getHashtagList()){
                projectEntity.addProjectHashtagList(
                        ProjectHashtag.builder()
                                      .projectHashtag(getValidHashtag(hashtagName))
                                      .project(projectEntity)
                                      .build()
                );
            }
        }


        // ProjectDetail 생성
        ProjectDetail projectDetailEntity = ProjectDetail.builder()
                .project(projectEntity)
                .projectContent(projectRequestDTO.getContent())
                .projectDuration(projectRequestDTO.getDuration())
                .projectLocalType(projectRequestDTO.getLocalType())
                .build();


        // 프로젝트 포지션 리스트
        if(projectRequestDTO.getPositionCategoryList() != null) {
            for (ProjectPositionCategoryDTO projectPositionCategoryDTO : projectRequestDTO.getPositionCategoryList()) {
                PositionCategory positionCategoryEntity = positionCategoryRepo.findByCategoryName(projectPositionCategoryDTO.getPositionCategoryName())
                                                                              .orElseThrow(() -> new CustomException(ErrorCode.INVALID_POSITION_CATEGORY));

                projectDetailEntity.addProjectPosition(
                        ProjectPosition.builder()
                                       .projectDetail(projectDetailEntity)
                                       .recruitPositionCount(projectPositionCategoryDTO.getCount())
                                       .recruitUserPositionCategory(positionCategoryEntity)
                                       .build()
                );
            }
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
    public void uploadImage(List<MultipartFile> files, Long projectId, Long userId) {
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = getValidProject(projectId);
        getAuthorizedUserProject(userHistoryEntity.getId(), projectId);

        // 기존에 존재했을 경우
        if (projectEntity.getProjectImageUrl() != null) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_PROJECT_IMAGE);
        }
        // 첫번째 이미지를 대표이미지로 저장
        String key = s3Service.createUUIDKey(files.get(0).getOriginalFilename());
        String imageUrl = s3Service.upload(files.get(0), key);
        projectEntity.updateProjectImageUrl(imageUrl);
        projectEntity.updateProjectImageKey(key);

        // 그 외는 ProjectImage에 추가
        if(files.size() > 1){
            for (int i = 1; i < files.size(); i++) {
                key = s3Service.createUUIDKey(files.get(i).getOriginalFilename());
                imageUrl = s3Service.upload(files.get(i), key);
                projectEntity.addProjectImage(new ProjectImage(projectEntity, imageUrl, key));
            }
        }
    }


    @Transactional
    public void modifyImage(MultipartFile file, Long projectId, Long userId) {
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = getValidProject(projectId);
        getAuthorizedUserProject(userHistoryEntity.getId(), projectId);

        // 기존에 존재했을 경우 삭제
        if(projectEntity.getProjectImageUrl() != null){
            s3Service.delete(projectEntity.getProjectImageKey());
        }
        String key = s3Service.createUUIDKey(file.getOriginalFilename());
        String imageUrl = s3Service.upload(file, key);
        projectEntity.updateProjectImageUrl(imageUrl);
        projectEntity.updateProjectImageKey(key);
    }


    @Transactional
    public void changeProjectStatus(Long projectId, ProjectStatus status, Long userId) {
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = getValidProject(projectId);
        getAuthorizedUserProject(userHistoryEntity.getId(), projectId);

        // 프로젝트 완료시 참여자들 완주 count 증가
        if(status == ProjectStatus.COMPLETED){
            projectEntity.getUserProjectList()
                         .stream()
                         .filter(userProject -> userProject.getUserProjectStatus() != UserProjectStatus.DROP)
                         .forEach(userProject -> userProject.getUserHistory().addCompletionCount());
        }

        projectEntity.updateProjectStatus(status);
    }


    @Transactional
    public void modifyProject(Long projectId, ProjectModifyRequestDTO projectModifyRequestDTO, Long userId) {
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);
        Project projectEntity = getValidProject(projectId);
        ProjectDetail projectDetailEntity = projectEntity.getProjectDetail();
        getAuthorizedUserProject(userHistoryEntity.getId(), projectId);

        projectEntity.updateProjectTitle(projectModifyRequestDTO.getTitle());
        projectDetailEntity.updateProjectContent(projectModifyRequestDTO.getContent());
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
        ProjectDetail projectDetailEntity = projectEntity.getProjectDetail();
        getValidUser(userId);
        UserHistory userHistoryEntity = getValidUserHistory(userId);

        // 로그인한 유저가 좋아요를 눌렀는지 확인
        boolean isLike = isUserLikeProject(userHistoryEntity, projectEntity);

        // 조회수 증가
        projectEntity.addHit();

        return ProjectResponseDTO.builder()
                .title(projectEntity.getProjectTitle())
                .content(projectDetailEntity.getProjectContent())
                .writer(getWriter(projectEntity.getUserProjectList()))
                .hashtagList(getHashtagList(projectEntity.getProjectHashtagList()))
                .imageUrlList(projectEntity.getProjectImageUrlList())
                .hitCount(projectEntity.getHitCount())
                .likeCount(projectEntity.getLikeCount())
                .duration(projectEntity.getProjectDetail().getProjectDuration())
                .location(projectEntity.getProjectDetail().getProjectLocalType())
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
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
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
                    throw new CustomException(ErrorCode.INVALID_USER);
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


    private UserProject getAuthorizedUserProject(Long userHistoryId, Long projectId) {
        UserProject userProjectEntity = userProjectRepo.findByUserHistoryIdAndProjectId(userHistoryId, projectId)
                                                       .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_USER));
        if (userProjectEntity.getRoleType() != UserProjectRoleType.LEADER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        return userProjectEntity;
    }


    private Hashtag getValidHashtag(String hashtagName) {
        Hashtag hashtag = hashtagRepo.findHashtagByHashtagName(hashtagName)
                                     .orElseThrow(() -> new CustomException(ErrorCode.INVALID_HASHTAG));
        return hashtag;
    }


    private void checkValidSort(String sort){
        if(!sort.equals("date") && !sort.equals("hitCount") && !sort.equals("likeCount")){
            throw new CustomException(ErrorCode.INVALID_SORT);
        }
    }


    private void checkValidPosition(String position){
        if(!position.equals("개발자") && !position.equals("기획자") && !position.equals("디자이너")){
            throw new CustomException(ErrorCode.INVALID_POSITION_CATEGORY);
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
