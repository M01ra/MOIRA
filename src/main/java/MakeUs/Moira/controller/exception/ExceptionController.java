package MakeUs.Moira.controller.exception;

import MakeUs.Moira.advice.exception.AuthEntryPointException;
import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.domain.user.UserRole;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping(value = "/exception")
@RestController
public class ExceptionController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;

    @GetMapping(value = "/entrypoint")
    public CommonResult jwtTokenError() {
        throw new AuthEntryPointException("JWT_TOKEN_ERROR");
    }

    @GetMapping(value = "/denied")
    public CommonResult accessDeniedException() {
        throw new AccessDeniedException("해당 리소스에 접근 권한이 없습니다.");
    }
}
