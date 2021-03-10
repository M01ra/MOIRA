package MakeUs.Moira.controller;

import MakeUs.Moira.domain.project.dto.ProjectDTO;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ResponseService responseService;

    @ApiOperation(value = "프로젝트(팀) 생성", notes = "성공시 생성된 프로젝트(팀)의 ID를 반환합니다")
    @PostMapping
    public SingleResult<Long> createProject(@RequestBody ProjectDTO projectDTO) {
        Long projectId = projectService.createProject(projectDTO);
        return responseService.mappingSingleResult(projectId, "프로젝트 생성 성공");
    }

    @ApiOperation(value = "프로젝트(팀)에 이미지 추가", notes = "프로젝트(팀)에 이미지를 추가합니다")
    @PostMapping("/{projectId}/image")
    public CommonResult uploadImage(@ApiParam(value = "파일 리스트", required = true) @RequestPart List<MultipartFile> files, @ApiParam(value = "프로젝트(팀) ID", required = true) @PathVariable Long projectId){
        projectService.uploadImages(files, projectId);
        return responseService.mappingSuccessCommonResultOnly("프로젝트에 이미지 추가 성공");
    }



}
