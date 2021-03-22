package MakeUs.Moira.controller.project;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.project.dto.*;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.project.ProjectService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = {"3.프로젝트(팀)"})
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService   projectService;
    private final ResponseService  responseService;
    private final JwtTokenProvider jwtTokenProvider;


    @ApiOperation(
            value = "프로젝트(팀) 생성",
            notes = "성공시 생성된 프로젝트(팀)의 ID를 반환합니다")
    @PostMapping
    public SingleResult<Long> createProject(@RequestBody ProjectRequestDTO projectRequestDTO,
                                            @RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        Long projectId = projectService.createProject(projectRequestDTO, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSingleResult(projectId, "프로젝트 생성 성공");
    }


    @ApiOperation(
            value = "프로젝트(팀)에 이미지 추가 혹은 수정",
            notes = "프로젝트(팀)에 이미지를 추가 혹은 수정합니다")
    @PutMapping("/{projectId}/image")
    public CommonResult uploadImage(@ApiParam(value = "이미지 파일", required = true) @RequestPart MultipartFile file,
                                    @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
                                    @RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        projectService.uploadImage(file, projectId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트에 이미지 추가 성공");
    }


    @ApiOperation(
            value = "프로젝트(팀)의 상태 변경",
            notes = "프로젝트(팀)의 상태를 RECRUITING, PROGRESSING, COMPLETED로 변경합니다")
    @PutMapping("/{projectId}/status")
    public CommonResult changeProjectStatus(
            @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @ApiParam(value = "변경할 상태", required = true) @RequestBody ProjectModifyStatusRequestDTO projectModifyStatusRequestDTO,
            @RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        projectService.changeProjectStatus(projectId, projectModifyStatusRequestDTO.getStatus(), Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 상태 변경 성공");
    }


    @ApiOperation(
            value = "프로젝트(팀)의 이름(제목) 변경",
            notes = "프로젝트(팀)의 이름(제목)를 변경합니다")
    @PutMapping("/{projectId}/title")
    public CommonResult changeProjectStatus(
            @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @ApiParam(value = "변경할 이름(제목)", required = true) @RequestBody ProjectModifyTitleRequestDTO projectModifyTitleRequestDTO,
            @RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        projectService.changeProjectTitle(projectId, projectModifyTitleRequestDTO.getTitle(), Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 이름(제목) 변경 성공");
    }


    @ApiOperation(
            value = "프로젝트(팀)들 조회",
            notes = "프로젝트(팀)들을 태그(tag), 정렬 방식(sort), Page에 따라 10개씩 조회합니다.\n태그, 정렬 방식, Page는 모두 필수가 아니며 미입력시 기본 값이 적용됩니다")
    @GetMapping
    public ListResult<ProjectsResponseDTO> getProjects(
            @ApiParam(value = "태그, 여러개 입력시 ,로 구분.\nex) tag=AOS,IOS,WEB") @RequestParam(name = "tag", required = false) String tag,
            @ApiParam(value = "정렬 방식(최신 순(기본 값): modifiedDate, 조회순: hitCount, 좋아요순: likeCount)") @RequestParam(name = "sort", required = false, defaultValue = "modifiedDate") String sort,
            @ApiParam(value = "페이지(기본 값 : 0)") @RequestParam(name = "page", required = false, defaultValue = "0") int page)
    {
        List<ProjectsResponseDTO> projectDTOList = projectService.getProjects(tag, sort, page);
        return responseService.mappingListResult(projectDTOList, "프로젝트 조회 성공");
    }


    @ApiOperation(
            value = "프로젝트(팀) 세부 조회",
            notes = "프로젝트(팀)의 세부 정보를 조회합니다. 조회시 해당 프로젝트(팀)의 조회수가 1 증가합니다")
    @GetMapping("/{projectId}")
    public SingleResult<ProjectResponseDTO> getProject(
            @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        ProjectResponseDTO projectResponseDTO = projectService.getProject(projectId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSingleResult(projectResponseDTO, "프로젝트 세부 정보 조회 성공");
    }


    @ApiOperation(
            value = "프로젝트(팀)의 좋아요 변경",
            notes = "유저가 프로젝트(팀)의 좋아요를 추가하거나 이미 추가되었다면 취소합니다")
    @PutMapping("/{projectId}/like")
    public CommonResult addProjectLike(@ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
                                       @RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        projectService.changeProjectLike(projectId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트에 좋아요 변경 성공");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "팀 목록 - 완료한 팀 - 팀원 평가하기 - 팀원 목록",
            notes = "- 팀 목록 - 완료한 팀 - 팀원 평가하기 에서 보이는 팀원 목록을 불러옵니다.\n" +
                    "- 평가한 사람의 경우, isCompltedReview가 true로 나옵니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/{projectId}/member")
    public ListResult<ProjectMemberResponseDto> getProjectMember(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                           @ApiParam(value = "선택한 프로젝트(팀)의 Id", required = true) @PathVariable Long projectId)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<ProjectMemberResponseDto> projectMemberResponseDtoList = projectService.getProjectMember(userId, projectId);
        return responseService.mappingListResult(projectMemberResponseDtoList, "팀 목록 - 완료한 팀 - 팀원 평가하기 - 팀원 목록");
    }

}
