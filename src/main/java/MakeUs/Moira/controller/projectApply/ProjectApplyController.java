package MakeUs.Moira.controller.projectApply;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.projectApply.dto.ProjectApplicantsResponseDTO;
import MakeUs.Moira.controller.projectApply.dto.ProjectApplyModifyStatusRequestDTO;
import MakeUs.Moira.controller.projectApply.dto.ProjectApplyRequestDTO;
import MakeUs.Moira.controller.projectApply.dto.ProjectApplyResponseDTO;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.projectApply.ProjectApplyService;
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

@Api(tags = {"3-3.프로젝트(팀) 지원"})
@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ProjectApplyController {

    private final ProjectApplyService projectApplyService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ApiOperation(
            value = "모집글 - 지원하기",
            notes = "유저가 프로젝트(팀)에 지원합니다")
    @PostMapping
    public CommonResult applyProject(
            @Valid @RequestBody ProjectApplyRequestDTO projectApplyRequestDTO,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token) {
        logger.info(projectApplyRequestDTO.toString());
        projectApplyService.applyProject(projectApplyRequestDTO, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 지원 성공");
    }


    @ApiOperation(
            value = "마이페이지 - 지원한 글 - 지원 목록 - 지원 내역",
            notes = "특정 지원서(프로필)를 조회합니다")
    @GetMapping("/{projectApplyId}")
    public SingleResult<ProjectApplyResponseDTO> getApplyProject(
            @NotNull(message = "projectApplyId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) 지원 ID", required = true) @PathVariable Long projectApplyId,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token) {
        ProjectApplyResponseDTO projectApplyResponseDTO = projectApplyService.getApplyProject(projectApplyId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        logger.info(projectApplyResponseDTO.toString());
        return responseService.mappingSingleResult(projectApplyResponseDTO, "유저 지원서 조회 성공");
    }


    @ApiOperation(
            value = "마이페이지 - 작성한 글 - 지원자 목록",
            notes = "팀에 지원한 유저들을 조회합니다")
    @GetMapping("/project/{projectId}")
    public ListResult<ProjectApplicantsResponseDTO> getProjectApplicants(
            @NotNull(message = "projectId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
        List<ProjectApplicantsResponseDTO> projectApplicantsResponseDTOList = projectApplyService.getProjectApplicants(projectId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        logger.info(projectApplicantsResponseDTOList.toString());
        return responseService.mappingListResult(projectApplicantsResponseDTOList, "프로젝트 지원자 조회 성공");
    }


    @ApiOperation(
            value = "지원서의 상태를 변경",
            notes = "팀장이 지원서의 상태를 변경합니다")
    @PutMapping("/{projectApplyId}")
    public CommonResult changeProjectApplyStatus(
            @NotNull(message = "projectApplyId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) 지원 ID", required = true) @PathVariable Long projectApplyId,
            @Valid @ApiParam(value = "변경할 상태", required = true) @RequestBody ProjectApplyModifyStatusRequestDTO projectApplyModifyStatusRequestDTO,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
        logger.info(projectApplyModifyStatusRequestDTO.toString());
        projectApplyService.changeProjectApplyStatus(projectApplyId, Long.valueOf(jwtTokenProvider.getUserPk(token)), projectApplyModifyStatusRequestDTO.getStatus());
        return responseService.mappingSuccessCommonResultOnly("지원서 상태 변경 성공");
    }


    @ApiOperation(
            value = "프로젝트(팀) 지원 취소",
            notes = "유저가 지원한 팀을 취소합니다")
    @DeleteMapping("/{projectApplyId}")
    public CommonResult cancelApplyProject(
            @NotNull(message = "projectApplyId에 빈 값을 넣을 수 없음") @ApiParam(value = "프로젝트(팀) 지원 ID", required = true) @PathVariable Long projectApplyId,
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
        projectApplyService.cancelApplyProject(projectApplyId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 지원 취소 성공");
    }
}
