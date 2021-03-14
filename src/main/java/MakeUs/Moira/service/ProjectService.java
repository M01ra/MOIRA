package MakeUs.Moira.service;

import MakeUs.Moira.advice.exception.ProjectException;
import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.project.dto.*;
import MakeUs.Moira.domain.hashtag.Hashtag;
import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.domain.position.UserPosition;
import MakeUs.Moira.domain.position.PositionRepo;
import MakeUs.Moira.domain.project.*;
import MakeUs.Moira.domain.project.projectDetail.*;
import MakeUs.Moira.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final HashtagRepo hashtagRepo;
    private final ProjectHashtagRepo projectHashtagRepo;
    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final UserHistoryRepo userHistoryRepo;
    private final UserProjectRepo userProjectRepo;
    private final ProjectImageRepo projectImageRepo;
    private final ProjectQuestionRepo projectQuestionRepo;
    private final ProjectDetailRepo projectDetailRepo;
    private final ProjectPositonRepo projectPositonRepo;
    private final PositionRepo positionRepo;
    private final ProjectLikeRepo projectLikeRepo;
    private final ProjectCommentRepo projectCommentRepo;
    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long createProject(ProjectRequestDTO projectRequestDTO, String token) {
        Project project = new Project();
        project.setProjectTitle(projectRequestDTO.getProjectTitle());
        project = projectRepo.save(project);

        List<ProjectHashtag> projectHashtagList = new ArrayList<>();
        if(projectRequestDTO.getProjectHashtagList() != null) {
            for (String hashtag : projectRequestDTO.getProjectHashtagList()) {
                Optional<Hashtag> optionalHashtag = hashtagRepo.findHashtagByHashtagName(hashtag);
                if(!optionalHashtag.isPresent()){
                    throw new ProjectException("존재하지 않는 태그");
                }
                projectHashtagList.add(projectHashtagRepo.save(
                        new ProjectHashtag(project, optionalHashtag.get())
                ));
            }
        }
        project.setProjectHashtagList(projectHashtagList);
        project = projectRepo.save(project);

        //Optional<User> optionalUser = userRepo.findById(Long.valueOf(jwtTokenProvider.getUserPk(token)));
        Optional<User> optionalUser = userRepo.findById(1L);
        if(!optionalUser.isPresent()){
            throw new ProjectException("유효하지 않는 유저");
        }
        User user = optionalUser.get();
        List<UserProject> userProjectList = new ArrayList<>();
        Optional<UserHistory> optionalUserHistory = userHistoryRepo.findByUser(user);
        if(!optionalUserHistory.isPresent()){
            throw new ProjectException("유효하지 않은 유저");
        }
        UserHistory userHistory = optionalUserHistory.get();
        UserProject userProject = new UserProject(userHistory, project, UserProjectRoleType.LEADER, user.getUserPosition(), UserProjectStatus.PROGRESS);
        userProjectList.add(userProjectRepo.save(userProject));
        project.setUserProjectList(userProjectList);
        userHistory.addUserProject(userProject);

        ProjectDetail projectDetail = new ProjectDetail(project, projectRequestDTO.getProjectContent(), projectRequestDTO.getProjectDuration(), projectRequestDTO.getProjectLocalType());
        projectDetail = projectDetailRepo.save(projectDetail);

        List<ProjectQuestion> projectQuestionList = new ArrayList<>();
        if(projectRequestDTO.getProjectQuestionList() != null){
            for (String question : projectRequestDTO.getProjectQuestionList()) {
                projectQuestionList.add(projectQuestionRepo.save(new ProjectQuestion(projectDetail, question)));
            }
        }
        projectDetail.setProjectQuestionList(projectQuestionList);

        List<ProjectPosition> projectPositionList = new ArrayList<>();
        if(projectRequestDTO.getProjectPositionList() != null){
            for (ProjectPositonDTO projectPositonDTO : projectRequestDTO.getProjectPositionList()) {
                Optional<UserPosition> optionalPosition = positionRepo.findByPositionName(projectPositonDTO.getPositionName());
                if(!optionalPosition.isPresent()){
                    throw new ProjectException("존재하지 않은 포지션");
                }
                projectPositionList.add(projectPositonRepo.save(new ProjectPosition(projectDetail, optionalPosition.get(), projectPositonDTO.getCount())));
            }
            projectDetail.setProjectPositionList(projectPositionList);
        }
        project.setProjectDetail(projectDetail);
        return projectRepo.save(project).getId();
    }

    @Transactional
    public void uploadImages(List<MultipartFile> files, Long projectId) throws NoSuchElementException{
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if(!optionalProject.isPresent()){
            throw new ProjectException("존재하지 않은 프로젝트 ID");
        }
        Project project = optionalProject.get();
        List<ProjectImage> projectImageList = new ArrayList<>();
        for (MultipartFile file : files) {
            projectImageList.add(projectImageRepo.save(new ProjectImage(project, s3Service.upload(file, "project-" + projectId + "-" + file.getOriginalFilename()))));
        }
        project.setProjectImageList(projectImageList);
    }

    @Transactional
    public void changeProjectStatus(Long projectId, ProjectStatus status){
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if(!optionalProject.isPresent()){
            throw new ProjectException("존재하지 않은 프로젝트 ID");
        }
        Project project = optionalProject.get();
        project.changeProjectStatus(status);
    }

    @Transactional
    public List<ProjectsResponseDTO> getProjects(String tag, String sort, int page) {
        if(sort.equals("modifiedDate") && sort.equals("hitCount") && sort.equals("likeCount")){
            throw new ProjectException("유효하지 않는 정렬 방식");
        }
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort).descending());
        List<ProjectRepo.ProjectsResponseInterface> projectsResponseInterfaceList = null;
        if(tag != null) {
            String[] tags = tag.split("\\,");
            List<String> tagList = new ArrayList<>();
            for (String t : tags) {
                tagList.add(t);
                if (!hashtagRepo.findHashtagByHashtagName(t).isPresent()) {
                    throw new ProjectException("존재하지 않는 태그");
                }
            }
            projectsResponseInterfaceList = projectRepo.findProjectsByTagOrderPage(tagList, pageable);
        }
        else {
            projectsResponseInterfaceList = projectRepo.findProjectsByOrderPage(pageable);
        }
        List<ProjectsResponseDTO> projectsResponseDTOList = new ArrayList<>();
        for(ProjectRepo.ProjectsResponseInterface projectsResponseInterface : projectsResponseInterfaceList){
            ProjectsResponseDTO projectsResponseDTO = new ProjectsResponseDTO();
            BeanUtils.copyProperties(projectsResponseInterface, projectsResponseDTO);
            Optional<Project> optionalProject = projectRepo.findById(projectsResponseDTO.getId());
            if(!optionalProject.isPresent()){
                continue;
            }
            Project project = optionalProject.get();
            projectsResponseDTO.setHashtagList(getHashtagList(project.getProjectHashtagList()));
            projectsResponseDTO.setTime(getTime(projectsResponseInterface.getModifiedDate()));
            projectsResponseDTOList.add(projectsResponseDTO);
        }
        return projectsResponseDTOList;
    }

    @Transactional
    public ProjectResponseDTO getProject(Long projectId, String token){
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if(!optionalProject.isPresent()){
            throw new ProjectException("존재하지 않는 프로젝트 ID");
        }
        Project project = optionalProject.get();

        String writer = getWriter(project.getUserProjectList());
        List<String> hashtagList = getHashtagList(project.getProjectHashtagList());
        List<String> imageUrlList = getImageUrlList(project.getProjectImageList());
        String duration = project.getProjectDetail().getProjectDuration().toString();
        String location = project.getProjectDetail().getProjectLocalType().toString();
        List<ProjectPositonDTO> projectPositonDTOList = getProjectPositionList(project.getProjectDetail().getProjectPositionList());
        String time = getTime(project.getModifiedDate());

        //Optional<User> optionalUser = userRepo.findById(Long.valueOf(jwtTokenProvider.getUserPk(token)));
        Optional<User> optionalUser = userRepo.findById(1L);
        if(!optionalUser.isPresent()){
            throw new ProjectException("유효하지 않는 유저");
        }
        User user = optionalUser.get();
        Optional<UserHistory> optionalUserHistory = userHistoryRepo.findByUser(user);
        if(!optionalUserHistory.isPresent()){
            throw new ProjectException("유효하지 않는 유저");
        }
        UserHistory userHistory = optionalUserHistory.get();
        Optional<ProjectLike> optionalProjectLike = projectLikeRepo.findByUserHistoryAndProject(userHistory, project);
        boolean isLike = false;
        if(optionalProjectLike.isPresent()){
            ProjectLike projectLike = optionalProjectLike.get();
            if(projectLike.isProjectLiked()){
                isLike = true;
            }
        }

        project.addHit();
        projectRepo.save(project);
        return new ProjectResponseDTO(writer, project.getProjectTitle(), hashtagList, imageUrlList, project.getHitCount(), project.getLikeCount(), duration, location, projectPositonDTOList, time, isLike);
    }

    @Transactional
    public void changeProjectLike(Long projectId, String token){
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if(!optionalProject.isPresent()){
            throw new ProjectException("존재하지 않는 프로젝트 ID");
        }
        Project project = optionalProject.get();

        //Optional<User> optionalUser = userRepo.findById(Long.valueOf(jwtTokenProvider.getUserPk(token)));
        Optional<User> optionalUser = userRepo.findById(1L);
        if(!optionalUser.isPresent()){
            throw new ProjectException("유효하지 않는 유저");
        }
        User user = optionalUser.get();
        Optional<UserHistory> optionalUserHistory = userHistoryRepo.findByUser(user);
        if(!optionalUserHistory.isPresent()){
            throw new ProjectException("유효하지 않는 유저");
        }
        UserHistory userHistory = optionalUserHistory.get();
        Optional<ProjectLike> optionalProjectLike = projectLikeRepo.findByUserHistoryAndProject(userHistory, project);
        if(optionalProjectLike.isPresent()){
            ProjectLike projectLike = optionalProjectLike.get();
            if(projectLike.isProjectLiked()){
                project.cancelLike();
            }
            else{
                project.addLike();
                userHistory.addProjectLike(projectLike);
            }
            projectLike.changeProjectLiked();
        }
        else{
            ProjectLike projectLike = new ProjectLike(userHistory, project);
            projectLikeRepo.save(projectLike);
            project.addLike();
            userHistory.addProjectLike(projectLike);
        }
    }

    @Transactional
    public Long createProjectComment(ProjectCommentRequestDTO projectCommentRequestDTO, Long projectId, Long parentId, String token) {
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if(!optionalProject.isPresent()){
            throw new ProjectException("존재하지 않는 프로젝트 ID");
        }
        Project project = optionalProject.get();
        ProjectDetail projectDetail = project.getProjectDetail();

        //Optional<User> optionalUser = userRepo.findById(Long.valueOf(jwtTokenProvider.getUserPk(token)));
        Optional<User> optionalUser = userRepo.findById(1L);
        if(!optionalUser.isPresent()){
            throw new ProjectException("유효하지 않는 유저");
        }
        User writer = optionalUser.get();

        ProjectComment parentProjectComment = null;
        if(parentId != null){
            Optional<ProjectComment> optionalProjectComment = projectCommentRepo.findById(parentId);
            if(!optionalProjectComment.isPresent()){
                throw new ProjectException("존재하지 않는 부모 댓글 ID");
            }
            parentProjectComment = optionalProjectComment.get();
        }

        ProjectComment projectComment = new ProjectComment(projectDetail, writer, projectCommentRequestDTO.getContent(), parentProjectComment);
        projectComment = projectCommentRepo.save(projectComment);
        projectDetail.addComment(projectComment);
        return projectComment.getId();
    }

    @Transactional
    public List<ProjectCommentResponseDTO> getProjectComments(Long projectId, String token) {
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if(!optionalProject.isPresent()){
            throw new ProjectException("존재하지 않는 프로젝트 ID");
        }
        Project project = optionalProject.get();
        ProjectDetail projectDetail = project.getProjectDetail();
        List<ProjectComment> projectCommentList = projectCommentRepo.findAllByProjectDetailOrderByCreatedDate(projectDetail);
        List<ProjectCommentResponseDTO> projectCommentResponseDTOList = new ArrayList<>();
        for(ProjectComment projectComment : projectCommentList){
            User writer = projectComment.getWriter();
            Long parentId = null;
            if(projectComment.getParentComment() != null){
                parentId = projectComment.getParentComment().getId();
            }
            boolean isDeletable = false;
            //if(Long.valueOf(jwtTokenProvider.getUserPk(token)) == writer.getId()){
            if(Long.valueOf(1L) == writer.getId()){
                isDeletable = true;
            }
            ProjectCommentResponseDTO projectCommentResponseDTO = new ProjectCommentResponseDTO(projectComment.getId(), writer.getId(),parentId, writer.getNickname(), writer.getProfileImage(), projectComment.getComment(), getTime(projectComment.getCreatedDate()), isDeletable);
            projectCommentResponseDTOList.add(projectCommentResponseDTO);
        }
        return projectCommentResponseDTOList;
    }

    @Transactional
    public void deleteProjectComment(Long commentId, String token){
        Optional<ProjectComment> optionalProjectComment = projectCommentRepo.findById(commentId);
        if(!optionalProjectComment.isPresent()){
            throw new ProjectException("존재하지 않은 댓글 ID");
        }
        ProjectComment projectComment = optionalProjectComment.get();
//        if(projectComment.getWriter().getId() != Long.valueOf(jwtTokenProvider.getUserPk(token))){
        if(projectComment.getWriter().getId() != Long.valueOf(1L)){
            throw new ProjectException("로그인한 유저가 쓴 댓글이 아님");
        }
        projectCommentRepo.delete(optionalProjectComment.get());
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

    private List<String> getImageUrlList(List<ProjectImage> projectImageList){
        List<String> imageUrlList = new ArrayList<>();
        for(ProjectImage projectImage : projectImageList){
            imageUrlList.add(projectImage.getProjectImageUrl());
        }
        return imageUrlList;
    }

    private String getTime(LocalDateTime localDateTime){
        String time = null;
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

}
