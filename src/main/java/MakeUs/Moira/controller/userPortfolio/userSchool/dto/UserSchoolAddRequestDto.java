package MakeUs.Moira.controller.userPortfolio.userSchool.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class UserSchoolAddRequestDto {

    @ApiModelProperty(value = "학교 id")
    @NotNull(message = "schoolId에 빈 값을 넣을 수 없음")
    private Long   schoolId;

    @ApiModelProperty(value = "학과 id")
    @NotNull(message = "majorId에 빈 값을 넣을 수 없음")
    private Long   majorId;

    @ApiModelProperty(value = "입학년도")
    @NotBlank(message = "staredAt에 빈 값을 넣을 수 없음")
    private String staredAt;

    @ApiModelProperty(value = "졸업년도")
    private String endAt;

    @ApiModelProperty(value = "학적 상태", example = "ATTENDING, BREAK, GRADUATED, PROSPECTIVE, DROP")
    @NotNull(message = "userSchoolStatus에 빈 값을 넣을 수 없음")
    private String userSchoolStatus;
}
