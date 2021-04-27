package MakeUs.Moira.controller.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectPositionCategoryDTO {
    @ApiModelProperty(value = "포지션 카테고리 이름", example = "개발자")
    @NotBlank(message = "positionCategoryName에 빈 값을 넣을 수 없음")
    private String positionCategoryName;

    @ApiModelProperty(value = "인원 수", example = "3")
    @NotNull(message = "count에 빈 값을 넣을 수 없음")
    @Min(value = 1, message = "count의 값은 최소 1 이상이어야 함")
    private int count;
}
