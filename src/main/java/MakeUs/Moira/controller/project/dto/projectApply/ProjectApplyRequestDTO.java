package MakeUs.Moira.controller.project.dto.projectApply;

import MakeUs.Moira.domain.userPortfolio.UserPortfolioType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class ProjectApplyRequestDTO {
    @ApiModelProperty(value = "프로젝트 ID", example = "1")
    @NotNull(message = "projectId에 빈 값을 넣을 수 없음")
    private Long projectId;

    @ApiModelProperty(value = "선택 사항 리스트", example = "[\"SCHOOL\",\"CAREER\",\"LICENSE\"]")
    private List<UserPortfolioType> userPortfolioTypeList;
}
