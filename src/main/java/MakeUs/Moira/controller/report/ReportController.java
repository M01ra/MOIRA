package MakeUs.Moira.controller.report;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.report.dto.ReportRequestDTO;
import MakeUs.Moira.service.report.ReportService;
import MakeUs.Moira.util.response.ResponseService;
import MakeUs.Moira.util.response.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"9.신고"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(
            value = "댓글, 게시글, 채팅 신고하기",
            notes = "댓글, 게시글, 채팅에 대한 신고하기입니다\n"
    )
    @PostMapping
    public CommonResult createReport(
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @Valid @ApiParam(value = "신고 정보", required = true) @RequestBody ReportRequestDTO reportRequestDTO){
        logger.info(reportRequestDTO.toString());
        reportService.createReport(Long.parseLong(jwtTokenProvider.getUserPk(token)), reportRequestDTO);
        return responseService.mappingSuccessCommonResultOnly("신고 성공");
    }

}
