package MakeUs.Moira.controller.projectComment;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.projectComment.dto.ProjectCommentRequestDTO;
import MakeUs.Moira.controller.projectComment.dto.ProjectCommentResponseDTO;
import MakeUs.Moira.service.projectComment.ProjectCommentService;
import MakeUs.Moira.util.response.ResponseService;
import MakeUs.Moira.util.response.model.CommonResult;
import MakeUs.Moira.util.response.model.ListResult;
import MakeUs.Moira.util.response.model.SingleResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = {"3-2.프로젝트(팀) 댓글"})
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class ProjectCommentController {

    private final ProjectCommentService projectCommentService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(
            value = "댓글창 - 댓글 쓰기",
            notes = "프로젝트(팀)의 댓글을 추가합니다")
    @PostMapping("/{projectId}")
    public SingleResult<Long> createProjectComment(
            @Valid @RequestBody ProjectCommentRequestDTO projectCommentRequestDTO,
            @NotNull(message = "projectId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @ApiParam(value = "부모 댓글 ID") @RequestParam(name = "parentId", required = false) Long parentId,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
        logger.info(projectCommentRequestDTO.toString());
        Long commentId = projectCommentService.createProjectComment(projectCommentRequestDTO, projectId, parentId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSingleResult(commentId, "프로젝트 댓글 추가 성공");
    }


    @ApiOperation(
            value = "댓글창 - 댓글 조회",
            notes = "프로젝트(팀)의 댓글을 조회합니다. 프로젝트에 생성된 모든 댓글을 최신순으로 제공합니다")
    @GetMapping("/{projectId}")
    public ListResult<ProjectCommentResponseDTO> getProjectComments(
            @NotNull(message = "projectId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
        List<ProjectCommentResponseDTO> projectCommentResponseDTOList = projectCommentService.getProjectComments(projectId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        logger.info(projectCommentResponseDTOList.toString());
        return responseService.mappingListResult(projectCommentResponseDTOList, "프로젝트 댓글 조회 성공");
    }

    @ApiOperation(
            value = "댓글창 - 댓글 삭제",
            notes = "프로젝트(팀)의 댓글을 삭제합니다")
    @DeleteMapping("/{commentId}")
    public CommonResult deleteProjectComment(
            @NotNull(message = "commentId에 빈 값을 넣을 수 없음") @ApiParam(value = "댓글 ID", required = true) @PathVariable Long commentId,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
        projectCommentService.deleteProjectComment(commentId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 댓글 삭제 성공");
    }
}
