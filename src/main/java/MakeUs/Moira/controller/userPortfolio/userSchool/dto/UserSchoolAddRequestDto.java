package MakeUs.Moira.controller.userPortfolio.userSchool.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class UserSchoolAddRequestDto {
    @ApiModelProperty(value = "학교 id")
    private Long   schoolId;
    @ApiModelProperty(value = "학과 id")
    private Long   majorId;
    @ApiModelProperty(value = "입학년도")
    private String staredAt;
    @ApiModelProperty(value = "졸업년도")
    private String endAt;
    @ApiModelProperty(value = "학적 상태", example = "ATTENDING, BREAK, GRADUATED, PROSPECTIVE, DROP")
    private String userSchoolStatus;
}
