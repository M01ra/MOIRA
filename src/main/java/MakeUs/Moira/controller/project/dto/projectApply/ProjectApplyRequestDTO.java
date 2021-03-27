package MakeUs.Moira.controller.project.dto.projectApply;

import MakeUs.Moira.domain.userPortfolio.UserPortfolioType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectApplyRequestDTO {
    @ApiModelProperty(value = "프로젝트 ID", example = "1")
    private Long projectId;
    @ApiModelProperty(value = "선택 사항 리스트", example = "[\"SCHOOL\",\"CAREER\",\"LICENSE\"]")
    private List<UserPortfolioType> userPortfolioTypeList;
}
