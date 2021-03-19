package MakeUs.Moira.controller.project.dto;

import MakeUs.Moira.domain.userPortfolio.UserPortfolioType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectApplyRequestDTO {
    private Long projectId;
    private Long userId;
    private List<String> answerList;
    private List<UserPortfolioType> userPortfolioTypeList;
}
