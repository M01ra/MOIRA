package MakeUs.Moira.controller.project;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.project.dto.*;
import MakeUs.Moira.domain.project.ProjectStatus;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = {"프로젝트(팀)"})
@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "프로젝트(팀) 생성", notes = "성공시 생성된 프로젝트(팀)의 ID를 반환합니다")
    @PostMapping("/project")
    public SingleResult<Long> createProject(@RequestBody ProjectRequestDTO projectRequestDTO, HttpServletRequest req) {
        Long projectId = projectService.createProject(projectRequestDTO, jwtTokenProvider.resolveToken(req));
        return responseService.mappingSingleResult(projectId, "프로젝트 생성 성공");
    }

    @ApiOperation(value = "프로젝트(팀)에 이미지 추가", notes = "프로젝트(팀)에 이미지를 추가합니다")
    @PostMapping("/project/{projectId}/image")
    public CommonResult uploadImage(@ApiParam(value = "파일 리스트", required = true) @RequestPart List<MultipartFile> files, @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId, HttpServletRequest req){
        projectService.uploadImages(files, projectId, jwtTokenProvider.resolveToken(req));
        return responseService.mappingSuccessCommonResultOnly("프로젝트에 이미지 추가 성공");
    }

    @ApiOperation(value = "프로젝트(팀)의 상태 변경", notes = "프로젝트(팀)의 상태를 RECRUITING, PROGRESSING, COMPLETED로 변경합니다")
    @PutMapping("/project/{projectId}")
    public CommonResult changeProjectStatus(@ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId, @ApiParam(value = "변경할 상태", required = true) @RequestParam ProjectStatus status, HttpServletRequest req){
        projectService.changeProjectStatus(projectId, status, jwtTokenProvider.resolveToken(req));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 상태 변경 성공");
    }

    @ApiOperation(value = "프로젝트(팀)들 조회", notes = "프로젝트(팀)들을 태그(tag), 정렬 방식(sort), Page에 따라 10개씩 조회합니다.\n태그, 정렬 방식, Page는 모두 필수가 아니며 미입력시 기본 값이 적용됩니다")
    @GetMapping("/projects")
    public ListResult<ProjectsResponseDTO> getProjects(@ApiParam(value = "태그, 여러개 입력시 ,로 구분.\nex) tag=AOS,IOS,WEB") @RequestParam(name = "tag", required = false) String tag, @ApiParam(value = "정렬 방식(최신 순(기본 값): modifiedDate, 조회순: hitCount, 좋아요순: likeCount)") @RequestParam(name = "sort", required = false, defaultValue = "modifiedDate") String sort, @ApiParam(value = "페이지(기본 값 : 0)") @RequestParam(name = "page", required = false, defaultValue = "0") int page){
        List<ProjectsResponseDTO> projectDTOList = projectService.getProjects(tag, sort, page);
        return responseService.mappingListResult(projectDTOList, "프로젝트 조회 성공");
    }

    @ApiOperation(value = "프로젝트(팀) 세부 조회", notes = "프로젝트(팀)의 세부 정보를 조회합니다. 조회시 해당 프로젝트(팀)의 조회수가 1 증가합니다")
    @GetMapping("/project/{projectId}")
    public SingleResult<ProjectResponseDTO> getProject(@ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId, HttpServletRequest req){
        ProjectResponseDTO projectResponseDTO = projectService.getProject(projectId, jwtTokenProvider.resolveToken(req));
        return responseService.mappingSingleResult(projectResponseDTO, "프로젝트 세부 정보 조회 성공");
    }

    @ApiOperation(value = "프로젝트(팀)의 좋아요 변경", notes = "유저가 프로젝트(팀)의 좋아요를 추가하거나 이미 추가되었다면 취소합니다")
    @PutMapping("/project/{projectId}/like")
    public CommonResult addProjectLike(@ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId, HttpServletRequest req){
        projectService.changeProjectLike(projectId, jwtTokenProvider.resolveToken(req));
        return responseService.mappingSuccessCommonResultOnly("프로젝트에 좋아요 변경 성공");
    }

    @ApiOperation(value = "프로젝트(팀) 댓글 생성", notes = "프로젝트(팀)의 댓글을 추가합니다")
    @PostMapping("/project/{projectId}/comment")
    public SingleResult<Long> createProjectComment(@RequestBody ProjectCommentRequestDTO projectCommentRequestDTO, @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId, @ApiParam(value = "부모 댓글 ID") @RequestParam(name = "parent", required = false) Long parentId, HttpServletRequest req){
        Long commentId = projectService.createProjectComment(projectCommentRequestDTO, projectId, parentId, jwtTokenProvider.resolveToken(req));
        return responseService.mappingSingleResult(commentId, "프로젝트 댓글 추가 성공");
    }

    @ApiOperation(value = "프로젝트(팀) 댓글 조회", notes = "프로젝트(팀)의 댓글을 조회합니다. 프로젝트에 생성된 모든 댓글을 최신순으로 제공합니다")
    @GetMapping("/project/{projectId}/comment")
    public ListResult<ProjectCommentResponseDTO> getProjectComments(@ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId, HttpServletRequest req){
        List<ProjectCommentResponseDTO> projectCommentResponseDTOList = projectService.getProjectComments(projectId, jwtTokenProvider.resolveToken(req));
        return responseService.mappingListResult(projectCommentResponseDTOList, "프로젝트 댓글 조회 성공");
    }

    @ApiOperation(value = "프로젝트(팀) 댓글 삭제", notes = "프로젝트(팀)의 댓글을 삭제합니다")
    @DeleteMapping("/comment/{commentId}")
    public CommonResult deleteProjectComment(@ApiParam(value = "댓글 ID", required = true) @PathVariable Long commentId, HttpServletRequest req){
        projectService.deleteProjectComment(commentId, jwtTokenProvider.resolveToken(req));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 댓글 삭제 성공");
    }

}
