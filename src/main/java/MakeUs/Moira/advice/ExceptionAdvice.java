package MakeUs.Moira.advice;

import MakeUs.Moira.advice.exception.EmailSignInFailedException;
import MakeUs.Moira.advice.exception.TokenProviderInvalidException;
import MakeUs.Moira.advice.exception.ProjectException;
import MakeUs.Moira.advice.exception.S3Exception;
import MakeUs.Moira.advice.exception.EmailSignInFailedException;
import MakeUs.Moira.advice.exception.TokenProviderInvalidException;
import MakeUs.Moira.advice.exception.UserException;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice // 예외 발생 시, JSON으로 반환
public class ExceptionAdvice {

    private final ResponseService responseService;

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
//        // 아무데서도 안 거르면, 여기서 거른다. -> 데이터가 없는 리스폰스
//        return responseService.mappingFailCommonResultOnly(e.getMessage());
//    }


    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userException(HttpServletRequest request, UserException e) {
        return responseService.mappingFailCommonResultOnly(e.getMessage());
    }

    @ExceptionHandler(ProjectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult ProjectException(ProjectException e) {
        return responseService.mappingFailCommonResultOnly(e.getMessage());
    }

    @ExceptionHandler(S3Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult S3Exception(S3Exception e) {
        return responseService.mappingFailCommonResultOnly(e.getMessage());
    }

    @ExceptionHandler(EmailSignInFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSignalFailed(HttpServletRequest request, EmailSignInFailedException e) {
        return responseService.mappingFailCommonResultOnly(e.getMessage());
    }

    @ExceptionHandler(TokenProviderInvalidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult tokenProviderInvalidException(HttpServletRequest request, TokenProviderInvalidException e) {
        return responseService.mappingFailCommonResultOnly(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult Exception(HttpServletRequest request, Exception e) {
        return responseService.mappingFailCommonResultOnly(e.getMessage());
    }

    @ExceptionHandler(ProjectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult ProjectException(ProjectException e) {
        return responseService.mappingFailCommonResultOnly(e.getMessage());
    }

    @ExceptionHandler(S3Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult S3Exception(S3Exception e) {
        return responseService.mappingFailCommonResultOnly(e.getMessage());
    }

}
