package MakeUs.Moira.controller;

import MakeUs.Moira.domain.project.ProjectStatus;
import MakeUs.Moira.domain.project.dto.ProjectDTO;
import MakeUs.Moira.domain.project.dto.ProjectsResponseDTO;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ResponseService responseService;

    @ApiOperation(value = "프로젝트(팀) 생성", notes = "성공시 생성된 프로젝트(팀)의 ID를 반환합니다")
    @PostMapping("/project")
    public SingleResult<Long> createProject(@RequestBody ProjectDTO projectDTO) {
        Long projectId = projectService.createProject(projectDTO);
        return responseService.mappingSingleResult(projectId, "프로젝트 생성 성공");
    }

    @ApiOperation(value = "프로젝트(팀)에 이미지 추가", notes = "프로젝트(팀)에 이미지를 추가합니다")
    @PostMapping("/project/{projectId}/image")
    public CommonResult uploadImage(@ApiParam(value = "파일 리스트", required = true) @RequestPart List<MultipartFile> files, @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId){
        projectService.uploadImages(files, projectId);
        return responseService.mappingSuccessCommonResultOnly("프로젝트에 이미지 추가 성공");
    }

    @ApiOperation(value = "프로젝트(팀)의 상태 변경", notes = "프로젝트(팀)의 상태를 RECRUITING, PROGRESSING, COMPLETED로 변경합니다")
    @PutMapping("/project/{projectId}")
    public CommonResult changeProjectStatus(@ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId, @ApiParam(value = "변경할 상태", required = true) @RequestParam ProjectStatus status){
        projectService.changeProjectStatus(projectId, status);
        return responseService.mappingSuccessCommonResultOnly("프로젝트 상태 변경 성공");
    }

    @ApiOperation(value = "프로젝트(팀)들 조회", notes = "프로젝트(팀)들을 정렬 방식(sort)과 Page에 따라 10개씩 조회합니다")
    @GetMapping("/projects")
    public ListResult<ProjectsResponseDTO> getProjects(@ApiParam(value = "정렬 방식(최신 순: modifiedDate, 조회순: hitCount, 좋아요순: likeCount") @RequestParam(name = "sort", required = false, defaultValue = "modifiedDate") String sort, @ApiParam(value = "페이지") @RequestParam(name = "page", required = false, defaultValue = "0") int page){
        List<ProjectsResponseDTO> projectDTOList = projectService.getProjects(sort, page);
        return responseService.mappingListResult(projectDTOList, "프로젝트 조회 성공");
    }

}
