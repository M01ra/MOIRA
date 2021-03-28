package MakeUs.Moira.controller.report.dto;

import MakeUs.Moira.domain.report.ReportTargetType;
import MakeUs.Moira.domain.report.ReportType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;


@Getter
@Builder
@ToString
public class ReportRequestDTO {

    @ApiModelProperty(value = "신고할 (게시물, 댓글, 채팅방) ID", example = "1")
    @NotNull(message = "targetId에 빈 값을 넣을 수 없음")
    private Long targetId;

    @ApiModelProperty(value = "신고 대상 종류(PROJECT, COMMENT, CHAT)", example = "CHAT")
    @NotNull(message = "targetType에 빈 값을 넣을 수 없음")
    private ReportTargetType targetType;

    @ApiModelProperty(value = "신고 유형 (불건전한_내용, 상업적인_내용, 허위_내용, 욕설_및_비난)", example = "욕설_및_비난")
    @NotNull(message = "reportType에 빈 값을 넣을 수 없음")
    private ReportType reportType;
}
