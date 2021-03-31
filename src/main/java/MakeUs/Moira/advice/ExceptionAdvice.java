package MakeUs.Moira.advice;

import MakeUs.Moira.advice.exception.*;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice // 예외 발생 시, JSON으로 반환
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        System.out.println(e.getClass());
        // 아무데서도 안 거르면, 여기서 거른다. -> 데이터가 없는 리스폰스
        logger.info("code : 500 " + e.getMessage());
        return responseService.mappingFailCommonResultOnly(500, e.getMessage());
    }


    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult CustomException(CustomException e) {
        logger.info("code : " + e.getErrorCode().getCode() +  " " + e.getMessage());
        return responseService.mappingFailCommonResultOnly(e.getErrorCode().getCode(), e.getMessage());
    }


    // 잘못된 KAKAO TOKEN
    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult HttpClientErrorException(HttpServletRequest request, HttpClientErrorException e) {
        logger.info("code : 411 " + e.getMessage());
        return responseService.mappingFailCommonResultOnly(411, e.getMessage());
    }

    // @Valid에 의한 검증
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult MethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        int code = 400;
        String message = "";
        BindingResult bindingResult = e.getBindingResult();

        //에러가 있다면
        if(bindingResult.hasErrors()) {
            //DTO에 설정한 meaasge값을 가져온다
            message = bindingResult.getFieldError()
                                   .getDefaultMessage();

            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError()
                                                 .getCode();

            switch (bindResultCode) {
                case "NotNull":
                    code = ErrorCode.NOT_NULL.getCode();
                    break;
                case "NotBlank":
                    code = ErrorCode.NOT_BLANK.getCode();
                    break;
                case "Min":
                    code = ErrorCode.MIN.getCode();
                    break;
                case "Max":
                    code = ErrorCode.MAX.getCode();
                    break;
            }
        }
        logger.info("code : " + code + " " + e.getMessage());
        return responseService.mappingFailCommonResultOnly(code, message);
    }


    // 잘못된 enum RequestDTO
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult HttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        logger.info("code : 410 " + e.getMessage());
        return responseService.mappingFailCommonResultOnly(410, e.getMessage());
    }


    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult JWTException(HttpServletRequest request, JwtException e) {
        logger.info("code : 435 " + e.getMessage());
        return responseService.mappingFailCommonResultOnly(435, e.getMessage());
    }

}
