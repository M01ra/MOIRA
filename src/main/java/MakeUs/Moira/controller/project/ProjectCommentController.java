package MakeUs.Moira.controller.project;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.project.dto.projectComment.ProjectCommentRequestDTO;
import MakeUs.Moira.controller.project.dto.projectComment.ProjectCommentResponseDTO;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.project.ProjectCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"프로젝트(팀) 댓글"})
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class ProjectCommentController {

    private final ProjectCommentService projectCommentService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(
            value = "댓글창 - 댓글 쓰기",
            notes = "프로젝트(팀)의 댓글을 추가합니다")
    @PostMapping("/{projectId}")
    public SingleResult<Long> createProjectComment(@RequestBody ProjectCommentRequestDTO projectCommentRequestDTO,
                                                   @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
                                                   @ApiParam(value = "부모 댓글 ID") @RequestParam(name = "parentId", required = false) Long parentId,
                                                   @RequestHeader(value = "X-AUTH-TOKEN") String token){
        Long commentId = projectCommentService.createProjectComment(projectCommentRequestDTO, projectId, parentId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSingleResult(commentId, "프로젝트 댓글 추가 성공");
    }

    @ApiOperation(
            value = "댓글창 - 댓글 조회",
            notes = "프로젝트(팀)의 댓글을 조회합니다. 프로젝트에 생성된 모든 댓글을 최신순으로 제공합니다")
    @GetMapping("/{projectId}")
    public ListResult<ProjectCommentResponseDTO> getProjectComments(@ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
                                                                    @RequestHeader(value = "X-AUTH-TOKEN") String token){
        List<ProjectCommentResponseDTO> projectCommentResponseDTOList = projectCommentService.getProjectComments(projectId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingListResult(projectCommentResponseDTOList, "프로젝트 댓글 조회 성공");
    }

    @ApiOperation(
            value = "댓글창 - 댓글 삭제",
            notes = "프로젝트(팀)의 댓글을 삭제합니다")
    @DeleteMapping("/{commentId}")
    public CommonResult deleteProjectComment(@ApiParam(value = "댓글 ID", required = true) @PathVariable Long commentId,
                                             @RequestHeader(value = "X-AUTH-TOKEN") String token){
        projectCommentService.deleteProjectComment(commentId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 댓글 삭제 성공");
    }

}
