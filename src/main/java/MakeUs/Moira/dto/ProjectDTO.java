package MakeUs.Moira.dto;

import MakeUs.Moira.domain.project.projectDetail.ProjectDuration;
import MakeUs.Moira.domain.project.projectDetail.ProjectLocalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectDTO {
    private String projectTitle;
    private String projectContent;
    private ProjectDuration projectDuration;
    private ProjectLocalType projectLocalType;
    private List<ProjectPositonDTO> projectPositionList;
    private List<String> projectHashtagList;
    private List<MultipartFile> projectImageList;
    private List<String> projectQuestionList;
    private Long userId;
}
