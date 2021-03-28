package MakeUs.Moira.controller.project;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.project.dto.*;
import MakeUs.Moira.controller.project.dto.project.*;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.project.ProjectService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = {"3-1.프로젝트(팀)"})
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProjectService projectService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;


    @ApiOperation(
            value = "팀원 모집 - 팀 만들기",
            notes = "성공시 생성된 프로젝트(팀)의 ID를 반환합니다")
    @PostMapping
    public SingleResult<Long> createProject(
            @Valid @ApiParam(value = "생성할 팀 정보", required = true) @RequestBody ProjectRequestDTO projectRequestDTO,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token) {
        logger.info(projectRequestDTO.toString());
        Long projectId = projectService.createProject(projectRequestDTO, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSingleResult(projectId, "프로젝트 생성 성공");
    }


    @ApiOperation(
            value = "팀 만들기 - 이미지 리스트 추가",
            notes = "프로젝트(팀)에 이미지 리스트를 추가합니다.\n처음 프로젝트를 생성한 직후만 호출하면 됩니다")
    @PostMapping("/{projectId}/image")
    public CommonResult uploadImage(
            @NotNull(message = "files에 빈 값을 넣을 수 없음") @ApiParam(value = "이미지 파일 리스트", required = true) @RequestPart List<MultipartFile> files,
            @NotNull(message = "projectId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
        files.forEach(file -> logger.info("fileName : " + file.getOriginalFilename() + " size : " + file.getSize()));
        projectService.uploadImage(files, projectId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트에 이미지 생성 성공");
    }


    @ApiOperation(
            value = "나의 팀 - 상세 페이지(팀장) - 대표 이미지 수정하기",
            notes = "프로젝트(팀)에 이미지를 수정합니다. 기존에 등록하지 않았을 경우 대표 이미지를 등록합니다")
    @PutMapping("/{projectId}/image")
    public CommonResult modifyImage(
            @NotNull(message = "file에 빈 값을 넣을 수 없음") @ApiParam(value = "이미지 파일", required = true) @RequestPart MultipartFile file,
            @NotNull(message = "projectId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
        logger.info("fileName : " + file.getOriginalFilename() + " size : " + file.getSize());
        projectService.modifyImage(file, projectId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 대표 이미지 수정 성공");
    }


    @ApiOperation(
            value = "나의 팀 - 상세 페이지(팀장) - 프로젝트 완료하기(수정)",
            notes = "프로젝트(팀)의 상태를 RECRUITING, PROGRESSING, COMPLETED로 변경합니다")
    @PutMapping("/{projectId}/status")
    public CommonResult changeProjectStatus(
            @NotNull(message = "projectId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @Valid @ApiParam(value = "변경할 상태", required = true) @RequestBody ProjectModifyStatusRequestDTO projectModifyStatusRequestDTO,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        logger.info(projectModifyStatusRequestDTO.toString());
        projectService.changeProjectStatus(projectId, projectModifyStatusRequestDTO.getStatus(), Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 상태 변경 성공");
    }


    @ApiOperation(
            value = "나의 팀 - 상세 페이지(팀장) - 팀 수정하기",
            notes = "프로젝트(팀)의 이름(제목) 혹은 내용을 변경합니다")
    @PutMapping("/{projectId}")
    public CommonResult changeProjectStatus(
            @NotNull(message = "projectId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @Valid @ApiParam(value = "변경할 이름(제목)", required = true) @RequestBody ProjectModifyRequestDTO projectModifyTitleRequestDTO,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
        logger.info(projectModifyTitleRequestDTO.toString());
        projectService.modifyProject(projectId, projectModifyTitleRequestDTO, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 수정 성공");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "JWT 토큰",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "팀원 모집 - 모집글 리스트",
            notes = "프로젝트(팀)들을 태그(tag), 정렬 방식(sort), 포지션 카테고리(position), Page에 따라 10개씩 조회합니다.\n" +
                    "태그, 정렬 방식, 포지션 카테고리, Page,는 모두 필수가 아니며 미입력시 기본 값이 적용됩니다.\n" +
                    "키워드를 포함할 시 다른 조건(태그, 정렬, 포지션)는 무시되고 키워드로 검색됩니다.")
    @GetMapping
    public ListResult<ProjectsResponseDTO> getProjects(
            @ApiParam(value = "태그, 여러개 입력시 ,로 구분.\nex) tag=AOS,IOS,WEB") @RequestParam(name = "tag", required = false) String tag,
            @ApiParam(value = "정렬 방식(최신 순(기본 값): date, 조회순: hitCount, 좋아요순: likeCount)", allowableValues = "date, hitCount, likeCount") @RequestParam(name = "sort", required = false, defaultValue = "date") String sort,
            @ApiParam(value = "포지션 카테고리(기본 값 : 전체)") @RequestParam(name = "position", required = false) String position,
            @ApiParam(value = "페이지(기본 값 : 0)") @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @ApiParam(value = "검색어") @RequestParam(name = "keyword", required = false) String keyword){
        List<ProjectsResponseDTO> projectDTOList = projectService.getProjects(tag, sort, position, page, keyword);
        logger.info(projectDTOList.toString());
        return responseService.mappingListResult(projectDTOList, "프로젝트 조회 성공");
    }


    @ApiOperation(
            value = "팀원 모집 - 팀 모집글 상세",
            notes = "프로젝트(팀)의 세부 정보를 조회합니다. 조회시 해당 프로젝트(팀)의 조회수가 1 증가합니다")
    @GetMapping("/{projectId}")
    public SingleResult<ProjectResponseDTO> getProject(
            @NotNull(message = "projectId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
        ProjectResponseDTO projectResponseDTO = projectService.getProject(projectId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        logger.info(projectResponseDTO.toString());
        return responseService.mappingSingleResult(projectResponseDTO, "프로젝트 세부 정보 조회 성공");
    }


    @ApiOperation(
            value = "팀 모집 - 팀 모집글 상세 - 좋아요",
            notes = "유저가 프로젝트(팀)의 좋아요를 추가하거나 이미 추가되었다면 취소합니다")
    @PutMapping("/{projectId}/like")
    public CommonResult addProjectLike(
            @NotNull(message = "projectId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
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
    public ListResult<ProjectMemberResponseDto> getProjectMember(
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @NotNull(message = "projectId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀)의 Id", required = true) @PathVariable Long projectId)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<ProjectMemberResponseDto> projectMemberResponseDtoList = projectService.getProjectMember(userId, projectId);
        logger.info(projectMemberResponseDtoList.toString());
        return responseService.mappingListResult(projectMemberResponseDtoList, "팀 목록 - 완료한 팀 - 팀원 평가하기 - 팀원 목록");
    }

}
