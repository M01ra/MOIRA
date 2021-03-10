package MakeUs.Moira.service;

import MakeUs.Moira.advice.exception.ProjectException;
import MakeUs.Moira.domain.hashtag.Hashtag;
import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.domain.position.Position;
import MakeUs.Moira.domain.position.PositionRepo;
import MakeUs.Moira.domain.project.*;
import MakeUs.Moira.domain.project.dto.ProjectsResponseDTO;
import MakeUs.Moira.domain.project.projectDetail.*;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.project.dto.ProjectDTO;
import MakeUs.Moira.domain.project.dto.ProjectPositonDTO;
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
    private final S3Service s3Service;

    @Transactional
    public Long createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        BeanUtils.copyProperties(projectDTO, project);
        project.setLikeCount(0);
        project.setHitCount(0);
        project.setProjectStatus(ProjectStatus.RECRUITING);
        project = projectRepo.save(project);
        if(projectDTO.getProjectHashtagList() != null) {
            List<ProjectHashtag> projectHashtagList = new ArrayList<>();
            for (String hashtag : projectDTO.getProjectHashtagList()) {
                Optional<Hashtag> optionalHashtag = hashtagRepo.findHashtagByHashtagName(hashtag);
                if(!optionalHashtag.isPresent()){
                    throw new ProjectException("존재하지 않는 태그");
                }
                projectHashtagList.add(projectHashtagRepo.save(
                        new ProjectHashtag(project, optionalHashtag.get())
                ));
            }
            project.setProjectHashtagList(projectHashtagList);
        }
        else project.setProjectHashtagList(new ArrayList<>());

        if(projectDTO.getUserId() != null) {
            Optional<User> optionalUser = userRepo.findById(projectDTO.getUserId());
            if(!optionalUser.isPresent()){
                throw new ProjectException("유효하지 않은 JWT 토큰");
            }
            User user = optionalUser.get();
            List<UserProject> userProjectList = new ArrayList<>();
            Optional<UserHistory> optionalUserHistory = userHistoryRepo.findByUser(user);
            if(!optionalUserHistory.isPresent()){
                throw new ProjectException("유효하지 않은 유저");
            }
            UserProject userProject = new UserProject(optionalUserHistory.get(), project, UserProjectRoleType.LEADER, user.getPosition(), UserProjectStatus.PROGRESS);
            userProjectList.add(userProjectRepo.save(userProject));
            project.setUserProjectList(userProjectList);
        }
        else project.setUserProjectList(new ArrayList<>());

        ProjectDetail projectDetail = new ProjectDetail(project, new ArrayList<>(), new ArrayList<>(), projectDTO.getProjectContent(), projectDTO.getProjectDuration(), projectDTO.getProjectLocalType());
        projectDetail = projectDetailRepo.save(projectDetail);

        if(projectDTO.getProjectQuestionList() != null){
            List<ProjectQuestion> projectQuestionList = new ArrayList<>();
            for (String question : projectDTO.getProjectQuestionList()) {
                projectQuestionList.add(projectQuestionRepo.save(new ProjectQuestion(projectDetail, question)));
            }
            projectDetail.setProjectQuestionList(projectQuestionList);
        }
        else projectDetail.setProjectQuestionList(new ArrayList<>());

        if(projectDTO.getProjectPositionList() != null){
            List<ProjectPosition> projectPositionList = new ArrayList<>();
            for (ProjectPositonDTO projectPositonDTO : projectDTO.getProjectPositionList()) {
                Optional<Position> optionalPosition = positionRepo.findByPositionName(projectPositonDTO.getPositionName());
                if(!optionalPosition.isPresent()){
                    throw new ProjectException("존재하지 않은 포지션");
                }
                projectPositionList.add(projectPositonRepo.save(new ProjectPosition(projectDetail, optionalPosition.get(), projectPositonDTO.getCount())));
            }
            projectDetail.setProjectPositionList(projectPositionList);
        }
        else projectDetail.setProjectPositionList(new ArrayList<>());
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
        project.setProjectStatus(status);
    }

    @Transactional
    public List<ProjectsResponseDTO> getProjects(String sort, int page){
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sort).descending());
        List<Project> projectList = null;
        projectList = projectRepo.findProjectsByProjectStatusEquals(ProjectStatus.RECRUITING, pageable);
//        else{
//            String[] tags = tag.split("\\,");
//            List<Hashtag> hashtagList = new ArrayList<>();
//            for(String str : tags){
//                Optional<Hashtag> optionalHashtag = hashtagRepo.findHashtagByHashtagName(str);
//                if(!optionalHashtag.isPresent()){
//                    throw new ProjectException("존재하지 않는 태그");
//                }
//                hashtagList.add(optionalHashtag.get());
//            }
//            projectList = projectRepo.findProjectsByProjectStatusEqualsAndProjectHashtagListIn(ProjectStatus.RECRUITING, hashtagList, pageable);
//        }

        List<ProjectsResponseDTO> projectsResponseDTOList = new ArrayList<>();
        for(Project project : projectList){
            String writer = null;
            for(UserProject userProject : project.getUserProjectList()){
                if(userProject.getRoleType() == UserProjectRoleType.LEADER){
                    Optional<User> optionalUser = userRepo.findById(userProject.getUserHistory().getUser().getId());
                    if(!optionalUser.isPresent()){
                        throw new ProjectException("존재하지 않는 유저");
                    }
                    writer = optionalUser.get().getNickname();
                    break;
                }
            }

            List<String> hashtagList = new ArrayList<>();
            for(ProjectHashtag projectHashtag : project.getProjectHashtagList()){
                hashtagList.add(projectHashtag.getProjectHashtag().getHashtagName());
            }

            String time = null;
            LocalDateTime localDateTime = project.getModifiedDate();
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

            String imageUrl = null;
            if(!project.getProjectImageList().isEmpty()){
                imageUrl = project.getProjectImageList().get(0).getProjectImageUrl();
            }

            ProjectsResponseDTO projectsResponseDTO = new ProjectsResponseDTO(project.getId(), writer, project.getProjectTitle(), hashtagList, imageUrl, project.getHitCount(), time);
            projectsResponseDTOList.add(projectsResponseDTO);
        }

        return projectsResponseDTOList;
    }
}
