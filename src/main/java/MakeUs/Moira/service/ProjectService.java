package MakeUs.Moira.service;

import MakeUs.Moira.advice.exception.ProjectException;
import MakeUs.Moira.domain.hashtag.Hashtag;
import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.domain.position.Position;
import MakeUs.Moira.domain.project.*;
import MakeUs.Moira.domain.project.projectDetail.*;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.project.dto.ProjectDTO;
import MakeUs.Moira.domain.project.dto.ProjectPositonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
}
