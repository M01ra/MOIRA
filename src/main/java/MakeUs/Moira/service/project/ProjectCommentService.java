package MakeUs.Moira.service.project;

import MakeUs.Moira.advice.exception.ProjectException;
import MakeUs.Moira.controller.project.dto.ProjectCommentRequestDTO;
import MakeUs.Moira.controller.project.dto.ProjectCommentResponseDTO;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.ProjectRepo;
import MakeUs.Moira.domain.project.projectDetail.ProjectComment;
import MakeUs.Moira.domain.project.projectDetail.ProjectCommentRepo;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectCommentService {
    private final ProjectCommentRepo projectCommentRepo;
    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;

    @Transactional
    public Long createProjectComment(ProjectCommentRequestDTO projectCommentRequestDTO, Long projectId, Long parentId, Long userId) {
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if(!optionalProject.isPresent()){
            throw new ProjectException("존재하지 않는 프로젝트 ID");
        }
        Project project = optionalProject.get();
        ProjectDetail projectDetail = project.getProjectDetail();

        Optional<User> optionalUser = userRepo.findById(userId);
        //Optional<User> optionalUser = userRepo.findById(1L);
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
    public List<ProjectCommentResponseDTO> getProjectComments(Long projectId, Long userId) {
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
            if(userId == writer.getId()){
                //if(Long.valueOf(1L) == writer.getId()){
                isDeletable = true;
            }
            ProjectCommentResponseDTO projectCommentResponseDTO = new ProjectCommentResponseDTO(projectComment.getId(), writer.getId(),parentId, writer.getNickname(), writer.getProfileImage(), projectComment.getComment(), getTime(projectComment.getCreatedDate()), isDeletable);
            projectCommentResponseDTOList.add(projectCommentResponseDTO);
        }
        return projectCommentResponseDTOList;
    }

    @Transactional
    public void deleteProjectComment(Long commentId, Long userId){
        Optional<ProjectComment> optionalProjectComment = projectCommentRepo.findById(commentId);
        if(!optionalProjectComment.isPresent()){
            throw new ProjectException("존재하지 않은 댓글 ID");
        }
        ProjectComment projectComment = optionalProjectComment.get();
        if(projectComment.getWriter().getId() != userId){
            //if(projectComment.getWriter().getId() != Long.valueOf(1L)){
            throw new ProjectException("로그인한 유저가 쓴 댓글이 아님");
        }
        projectCommentRepo.delete(optionalProjectComment.get());
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
