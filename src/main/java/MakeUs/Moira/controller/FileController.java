package MakeUs.Moira.controller;

import MakeUs.Moira.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/file")
    public String uploadFile(@RequestPart MultipartFile file){
        return s3Service.upload(file);
    }

    @DeleteMapping("/file")
    public void deleteFile(String key){
        s3Service.delete(key);
    }
}

