package MakeUs.Moira.controller.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectApplysResponseDTO {
    private Long projectApplyId;
    private Long projectId;
    private String imageUrl;
    private String title;
    private int hitCount;
    private String time;
}
