package MakeUs.Moira.controller.exception;

import MakeUs.Moira.exception.CustomException;
import MakeUs.Moira.exception.ErrorCode;
import MakeUs.Moira.util.response.model.CommonResult;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"99.예외처리"})
@RequiredArgsConstructor
@RequestMapping(value = "/exception")
@RestController
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public CommonResult jwtTokenError() {
        throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
    }

    @GetMapping(value = "/denied")
    public CommonResult accessDeniedException() {
        throw new CustomException(ErrorCode.UNAUTHORIZED_JWT);
    }
}
