package MakeUs.Moira.response.model;

import MakeUs.Moira.response.model.CommonResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends CommonResult {
    private T data;
}
