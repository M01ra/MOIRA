package MakeUs.Moira.service.projectComment;

import MakeUs.Moira.controller.projectComment.dto.ProjectCommentRequestDTO;
import MakeUs.Moira.controller.projectComment.dto.ProjectCommentResponseDTO;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.ProjectRepo;
import MakeUs.Moira.domain.projectComment.ProjectComment;
import MakeUs.Moira.domain.projectComment.ProjectCommentRepo;
import MakeUs.Moira.domain.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.userProject.UserProjectRoleType;
import MakeUs.Moira.exception.CustomException;
import MakeUs.Moira.exception.ErrorCode;
import MakeUs.Moira.service.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectCommentService {

    private final ProjectCommentRepo projectCommentRepo;
    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final AlarmService alarmService;


    @Transactional
    public Long createProjectComment(ProjectCommentRequestDTO projectCommentRequestDTO, Long projectId, Long parentId, Long userId) {
        Project projectEntity = getValidProject(projectId);
        ProjectDetail projectDetailEntity = projectEntity.getProjectDetail();
        User userEntity = getValidUser(userId);

        ProjectComment parentProjectComment = null;
        // 부모 댓글 ID가 있을 경우
        if(parentId != null){
            parentProjectComment = getValidProjectComment(parentId);
            // 부모 댓글이 대댓글일 경우
            if(parentProjectComment.getParentComment() != null){
                throw new CustomException(ErrorCode.ALREADY_EXIST_PARENT_COMMENT);
            }
            // 자신이 쓴 댓글이 아닐 경우 알람
            if(!parentProjectComment.getWriter().getId().equals(userId)) {
                alarmService.saveComment(getProjectTitle(projectEntity.getProjectTitle()), projectId, parentProjectComment.getWriter().getId(), projectEntity.getProjectImageUrl());
            }
        }
        else{
            // 자신이 쓴 댓글이 아닐 경우 알람
            User leader = getLeader(projectEntity);
            if(!leader.getId().equals(userId)) {
                alarmService.saveComment(getProjectTitle(projectEntity.getProjectTitle()), projectId, getLeader(projectEntity).getId(), projectEntity.getProjectImageUrl());
            }
        }

        ProjectComment projectComment = ProjectComment.builder()
                .projectDetail(projectDetailEntity)
                .writer(userEntity)
                .comment(projectCommentRequestDTO.getContent())
                .parentComment(parentProjectComment)
                .build();

        projectComment = projectCommentRepo.save(projectComment);
        projectDetailEntity.addProjectComment(projectComment);
        return projectComment.getId();
    }


    @Transactional
    public List<ProjectCommentResponseDTO> getProjectComments(Long projectId, Long userId) {
        Project projectEntity = getValidProject(projectId);
        ProjectDetail projectDetailEntity = projectEntity.getProjectDetail();
        List<ProjectComment> projectCommentList = projectCommentRepo.findAllByProjectDetailOrderByCreatedDate(projectDetailEntity);

        return projectCommentList
                .stream()
                .map(projectComment -> {
                    Long parentId = null;
                    if (projectComment.getParentComment() != null) {
                        parentId = projectComment.getParentComment().getId();
                    }
                    boolean isDeletable = false;
                    if (userId == projectComment.getWriter().getId()) {
                        isDeletable = true;
                    }
                    return ProjectCommentResponseDTO.builder()
                            .id(projectComment.getId())
                            .userId(projectComment.getWriter().getId())
                            .parentId(parentId)
                            .nickname(projectComment.getWriter().getNickname())
                            .imageUrl(projectComment.getWriter().getProfileImage())
                            .content(projectComment.getComment())
                            .time(getTime(projectComment.getCreatedDate()))
                            .isDeletable(isDeletable)
                            .build();
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteProjectComment(Long commentId, Long userId){
        ProjectComment projectCommentEntity = getValidProjectComment(commentId);
        ProjectDetail projectDetailEntity = projectCommentEntity.getProjectDetail();
        if(!projectCommentEntity.getWriter().getId().equals(userId)){
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }
        projectDetailEntity.removeProjectComment(projectCommentEntity);
        projectCommentRepo.delete(projectCommentEntity);
    }

    private User getValidUser(Long userId){
        User userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
        return userEntity;
    }

    private Project getValidProject(Long projectId){
        Project projectEntity = projectRepo.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT));
        return projectEntity;
    }


    private ProjectComment getValidProjectComment(Long projectCommentId){
        Optional<ProjectComment> optionalProjectComment = projectCommentRepo.findById(projectCommentId);
        if(!optionalProjectComment.isPresent()){
            throw new CustomException(ErrorCode.INVALID_COMMENT);
        }
        return optionalProjectComment.get();
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


    private String getProjectTitle(String projectTitle){
        if(projectTitle.length() > 10){
            projectTitle = projectTitle.substring(0, 10) + "...";
        }
        return projectTitle;
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
