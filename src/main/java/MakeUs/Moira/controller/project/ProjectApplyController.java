package MakeUs.Moira.controller.project;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.project.dto.ProjectApplicantsResponseDTO;
import MakeUs.Moira.controller.project.dto.ProjectApplyRequestDTO;
import MakeUs.Moira.controller.project.dto.ProjectApplysResponseDTO;
import MakeUs.Moira.domain.project.projectApply.ProjectApplyStatus;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.service.project.ProjectApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"프로젝트(팀) 지원"})
@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ProjectApplyController {

    private final ProjectApplyService projectApplyService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;


    @ApiOperation(
            value = "프로젝트(팀) 지원",
            notes = "유저가 팀에 지원합니다")
    @PostMapping
    public CommonResult applyProject(@RequestBody ProjectApplyRequestDTO projectApplyRequestDTO,
                                      @RequestHeader(value = "X-AUTH-TOKEN") String token) {
        projectApplyService.applyProject(projectApplyRequestDTO, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 지원 성공");
    }


    @ApiOperation(
            value = "유저의 지원 목록 조회",
            notes = "유저가 팀에 지원 목록을 조회합니다")
    @GetMapping
    public ListResult<ProjectApplysResponseDTO> getApplyProjects(@RequestHeader(value = "X-AUTH-TOKEN") String token) {
        List<ProjectApplysResponseDTO> projectApplysResponseDTOList = projectApplyService.getApplyProjects(Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingListResult(projectApplysResponseDTOList, "유저 지원 목록 조회 성공");
    }


    @ApiOperation(
            value = "프로젝트(팀)의 지원자 목록 조회",
            notes = "팀에 지원한 유저들을 조회합니다")
    @GetMapping("/{projectId}")
    public ListResult<ProjectApplicantsResponseDTO> getProjectApplicants(@ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
                                                                         @RequestHeader(value = "X-AUTH-TOKEN") String token){
        List<ProjectApplicantsResponseDTO> projectApplicantsResponseDTOList = projectApplyService.getProjectApplicants(projectId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingListResult(projectApplicantsResponseDTOList, "프로젝트 지원자 조회 성공");
    }


    @ApiOperation(
            value = "지원서의 상태를 변경",
            notes = "팀장이 지원서의 상태를 변경합니다")
    @PutMapping("/{projectApplyId}")
    public CommonResult changeProjectApplyStatus(@ApiParam(value = "프로젝트(팀) 지원 ID", required = true) @PathVariable Long projectApplyId,
                                                 @RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                 @ApiParam(value = "변경할 상태", required = true) @RequestParam(name = "status", required = true) ProjectApplyStatus status){
        projectApplyService.changeProjectApplyStatus(projectApplyId, Long.valueOf(jwtTokenProvider.getUserPk(token)), status);
        return responseService.mappingSuccessCommonResultOnly("지원서 상태 변경 성공");
    }


    @ApiOperation(
            value = "프로젝트(팀) 지원 취소",
            notes = "유저가 지원한 팀을 취소합니다")
    @DeleteMapping("/{projectApplyId}")
    public CommonResult cancelApplyProject(@ApiParam(value = "프로젝트(팀) 지원 ID", required = true) @PathVariable Long projectApplyId,
                                           @RequestHeader(value = "X-AUTH-TOKEN") String token){
        projectApplyService.cancelApplyProject(projectApplyId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSuccessCommonResultOnly("프로젝트 지원 취소 성공");
    }


    //    @ApiOperation(
//            value = "유저의 지원서 상세 조회",
//            notes = "팀에 지원한 유저의 지원서를 상세 조회합니다")
//    @GetMapping("/{projectId}")
//    public SingleResult<ProjectApplyResponseDTO> getProjectApply(@ApiParam(value = "프로젝트(팀) 지원 ID", required = true) @PathVariable Long projectApplyId,
//                                                                         @RequestHeader(value = "X-AUTH-TOKEN") String token){
//        ProjectApplyResponseDTO projectApplyResponseDTO = projectApplyService.getProjectApply(projectApplyId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
//        return responseService.mappingSingleResult(projectApplyResponseDTO, "지원서 상세 조회 성공");
//    }
}
