package MakeUs.Moira.service.report;

import MakeUs.Moira.controller.report.dto.ReportRequestDTO;
import MakeUs.Moira.domain.chat.ChatRoomRepo;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.ProjectRepo;
import MakeUs.Moira.domain.projectComment.ProjectComment;
import MakeUs.Moira.domain.projectComment.ProjectCommentRepo;
import MakeUs.Moira.domain.report.Report;
import MakeUs.Moira.domain.report.ReportRepo;
import MakeUs.Moira.domain.report.ReportStatus;
import MakeUs.Moira.domain.userHistory.UserHistory;
import MakeUs.Moira.domain.userHistory.UserHistoryRepo;
import MakeUs.Moira.domain.userProject.UserProjectRoleType;
import MakeUs.Moira.exception.CustomException;
import MakeUs.Moira.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepo reportRepo;
    private final UserHistoryRepo userHistoryRepo;
    private final ProjectCommentRepo projectCommentRepo;
    private final ProjectRepo projectRepo;
    private final ChatRoomRepo chatRoomRepo;


    @Transactional
    public void createReport(Long userId, ReportRequestDTO reportRequestDTO){

        // 입력한 ID 검증
        Long targetUserId;
        switch(reportRequestDTO.getTargetType()){
            case PROJECT:
                Project project = projectRepo.findById(reportRequestDTO.getTargetId())
                                             .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT));
                targetUserId = project.getUserProjectList().stream()
                       .filter(userProject -> userProject.getRoleType() == UserProjectRoleType.LEADER)
                       .findFirst()
                       .orElseThrow(() -> new CustomException(ErrorCode.NON_EXIST_PROJECT_LEADER))
                       .getUserHistory()
                       .getUser()
                       .getId();
                if(targetUserId.equals(userId)){
                    throw new CustomException(ErrorCode.UNAUTHORIZED_REPORT_PROJECT);
                }
                break;

            case COMMENT:
                ProjectComment projectComment = projectCommentRepo.findById(reportRequestDTO.getTargetId())
                                                                  .orElseThrow(() -> new CustomException(ErrorCode.INVALID_COMMENT));
                targetUserId = projectComment.getWriter().getId();
                if(targetUserId.equals(userId)){
                    throw new CustomException(ErrorCode.UNAUTHORIZED_REPORT_COMMENT);
                }
                break;

            case CHAT:
                chatRoomRepo.findById(reportRequestDTO.getTargetId())
                            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CHAT_ROOM));
        }

        reportRepo.save(Report.builder()
              .userHistory(getValidUserHistory(userId))
              .reportType(reportRequestDTO.getReportType())
              .targetId(reportRequestDTO.getTargetId())
              .targetType(reportRequestDTO.getTargetType())
              .reportStatus(ReportStatus.PROCESSING)
              .build());
    }


    private UserHistory getValidUserHistory(Long userId) {
        UserHistory userHistoryEntity = userHistoryRepo.findByUserId(userId)
                                                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
        return userHistoryEntity;
    }
}
