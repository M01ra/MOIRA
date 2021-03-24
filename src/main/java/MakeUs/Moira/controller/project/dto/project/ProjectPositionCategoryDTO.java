package MakeUs.Moira.controller.project.dto.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectPositionCategoryDTO {
    @ApiModelProperty(value = "포지션 카테고리 이름", example = "개발자")
    private String positionCategoryName;
    @ApiModelProperty(value = "인원 수", example = "3")
    private int count;
}
