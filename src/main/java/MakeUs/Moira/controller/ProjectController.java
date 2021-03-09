package MakeUs.Moira.controller;

import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.dto.ProjectDTO;
import MakeUs.Moira.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<String> createProject(@RequestBody ProjectDTO projectDTO) {
        try{
            projectService.createProject(projectDTO);
            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            return new ResponseEntity<>("프로젝트 생성 성공", headers, HttpStatus.CREATED);
        } catch(NoSuchElementException e){
            return new ResponseEntity<>("잘못된 데이터 입력", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{projectId}/image")
    public ResponseEntity<String> uploadImage(@RequestPart List<MultipartFile> files, @PathVariable Long projectId){
        projectService.uploadImages(files, projectId);
        return new ResponseEntity<>("프로젝트 이미지 추가 성공", HttpStatus.OK);
    }



}
