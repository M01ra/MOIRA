package MakeUs.Moira.controller.project;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.project.dto.myProject.MyProjectResponseDTO;
import MakeUs.Moira.controller.project.dto.myProject.MyProjectsResponseDTO;
import MakeUs.Moira.domain.user.UserProjectStatus;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.project.MyProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = {"나의 프로젝트(팀)"})
@RestController
@RequestMapping("/myProject")
@RequiredArgsConstructor
public class MyProjectController {

    private final MyProjectService MyProjectService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;


    @ApiOperation(
            value = "팀 목록 - 나의팀 리스트 조회",
            notes = "유저가 정렬 방식(최신 순(기본 값): date, 가나다순: character)과 상태(진행중: PROGRESS, 완료: COMPLETE)를 통해 프로젝트(팀) 리스트를 조회합니다.")
    @GetMapping
    public ListResult<MyProjectsResponseDTO> getMyProjects(@ApiParam(value = "정렬 방식(최신 순(기본 값): date, 가나다순: character)") @RequestParam(name = "sort", required = false, defaultValue = "date") String sort,
                                                           @ApiParam(value = "상태(진행중: PROGRESS, 완료: COMPLETE)") @RequestParam(name = "status", required = true, defaultValue = "PROGRESS") UserProjectStatus status,
                                                           @RequestHeader(value = "X-AUTH-TOKEN") String token){
        List<MyProjectsResponseDTO> myProjectResponseDTOList = MyProjectService.getMyProjects(Long.valueOf(jwtTokenProvider.getUserPk(token)), sort, status);
        return responseService.mappingListResult(myProjectResponseDTOList, "나의 프로젝트(팀) 리스트 조회 성공");
    }


    @ApiOperation(
            value = "팀 목록 - 나의팀 상세 조회",
            notes = "유저의 프로젝트(팀)을 조회합니다.")
    @GetMapping("/{projectId}")
    public SingleResult<MyProjectResponseDTO> getMyProject(@ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId,
                                                           @RequestHeader(value = "X-AUTH-TOKEN") String token){
        MyProjectResponseDTO myProjectResponseDTO = MyProjectService.getMyProject(projectId, Long.valueOf(jwtTokenProvider.getUserPk(token)));
        return responseService.mappingSingleResult(myProjectResponseDTO, "나의 프로젝트(팀) 조회 성공");
    }
}
