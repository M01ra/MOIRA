package MakeUs.Moira.response.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommonResult {

    @ApiModelProperty(value = "응답 성공여부 : true/false")
    private boolean isSucceed;

    @ApiModelProperty(value = "응답 메시지")
    private String msg;
}