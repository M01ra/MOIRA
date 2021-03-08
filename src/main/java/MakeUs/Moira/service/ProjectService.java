package MakeUs.Moira.service;

import MakeUs.Moira.domain.hashtag.Hashtag;
import MakeUs.Moira.domain.position.Position;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.ProjectHashtag;
import MakeUs.Moira.domain.project.ProjectImage;
import MakeUs.Moira.domain.project.ProjectStatus;
import MakeUs.Moira.domain.project.projectDetail.*;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.dto.ProjectDTO;
import MakeUs.Moira.dto.ProjectPositonDTO;
import MakeUs.Moira.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private ProjectHashtagRepository projectHashtagRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private ProjectImageRepository projectImageRepository;

    @Autowired
    private ProjectQuestionRepository projectQuestionRepository;

    @Autowired
    private ProjectDetailRepository projectDetailRepository;

    @Autowired
    private ProjectPositonRepository projectPositonRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private S3Service s3Service;


    public void createProject(ProjectDTO projectDTO) throws NoSuchElementException{
        Project project = new Project();
        BeanUtils.copyProperties(projectDTO, project);
        project.setLikeCount(0);
        project.setHitCount(0);
        project.setProjectStatus(ProjectStatus.RECRUITING);
        project = projectRepository.save(project);
        if(projectDTO.getProjectHashtagList() != null) {
            List<ProjectHashtag> projectHashtagList = new ArrayList<>();
            for (String hashtag : projectDTO.getProjectHashtagList()) {
                Optional<Hashtag> optionalHashtag = hashtagRepository.findHashtagByHashtagName(hashtag);
                if(!optionalHashtag.isPresent()){
                    throw new NoSuchElementException();
                }
                projectHashtagList.add(projectHashtagRepository.save(
                        new ProjectHashtag(project, optionalHashtag.get())
                ));
            }
            project.setProjectHashtagList(projectHashtagList);
        }
        else project.setProjectHashtagList(new ArrayList<>());

        if(projectDTO.getUserId() != null) {
            Optional<User> optionalUser = userRepository.findById(projectDTO.getUserId());
            if(!optionalUser.isPresent()){
                throw new NoSuchElementException();
            }
            User user = optionalUser.get();
            List<UserProject> userProjectList = new ArrayList<>();
            Optional<UserHistory> optionalUserHistory = userHistoryRepository.findByUser(user);
            if(!optionalUserHistory.isPresent()){
                throw new NoSuchElementException();
            }
            UserProject userProject = new UserProject(optionalUserHistory.get(), project, UserProjectRoleType.LEADER, user.getPosition(), UserProjectStatus.PROGRESS);
            userProjectList.add(userProjectRepository.save(userProject));
            project.setUserProjectList(userProjectList);
        }
        else project.setUserProjectList(new ArrayList<>());

        ProjectDetail projectDetail = new ProjectDetail(project, new ArrayList<>(), new ArrayList<>(), projectDTO.getProjectContent(), projectDTO.getProjectDuration(), projectDTO.getProjectLocalType());
        projectDetail = projectDetailRepository.save(projectDetail);

        if(projectDTO.getProjectQuestionList() != null){
            List<ProjectQuestion> projectQuestionList = new ArrayList<>();
            for (String question : projectDTO.getProjectQuestionList()) {
                projectQuestionList.add(projectQuestionRepository.save(new ProjectQuestion(projectDetail, question)));
            }
            projectDetail.setProjectQuestionList(projectQuestionList);
        }
        else projectDetail.setProjectQuestionList(new ArrayList<>());

        if(projectDTO.getProjectPositionList() != null){
            List<ProjectPosition> projectPositionList = new ArrayList<>();
            for (ProjectPositonDTO projectPositonDTO : projectDTO.getProjectPositionList()) {
                Optional<Position> optionalPosition = positionRepository.findByPositionName(projectPositonDTO.getPositionName());
                if(!optionalPosition.isPresent()){
                    throw new NoSuchElementException();
                }
                projectPositionList.add(projectPositonRepository.save(new ProjectPosition(projectDetail, optionalPosition.get(), projectPositonDTO.getCount())));
            }
            projectDetail.setProjectPositionList(projectPositionList);
        }
        else projectDetail.setProjectPositionList(new ArrayList<>());
        project.setProjectDetail(projectDetail);
        projectRepository.save(project);
    }

    public void uploadImages(List<MultipartFile> files, Long projectId) throws NoSuchElementException{
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(!optionalProject.isPresent()){
            throw new NoSuchElementException();
        }
        Project project = optionalProject.get();
        List<ProjectImage> projectImageList = new ArrayList<>();
        for (MultipartFile file : files) {
            projectImageList.add(projectImageRepository.save(new ProjectImage(project, s3Service.upload(file))));
        }
        project.setProjectImageList(projectImageList);
    }
}
