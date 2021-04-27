package MakeUs.Moira.controller.projectApply.dto;


import MakeUs.Moira.controller.myPage.dto.HashtagResponseDto;
import MakeUs.Moira.controller.userPortfolio.userAward.dto.UserAwardResponseDto;
import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerResponseDto;
import MakeUs.Moira.controller.userPortfolio.userLicense.dto.UserLicenseResponseDto;
import MakeUs.Moira.controller.userPortfolio.userLink.dto.UserLinkResponseDto;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.UserSchoolResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ProjectApplyResponseDTO {
    @ApiModelProperty(value = "지원자 ID", example = "1")
    private Long                     userId;
    @ApiModelProperty(value = "지원자 프로필 URL", example = "https://moira-springboot.s3.ap-northeast-2.amazonaws.com/project-3-Sketchpad.png")
    private String                   imageUrl;
    @ApiModelProperty(value = "닉네임", example = "웰시고기")
    private String                   nickname;
    @ApiModelProperty(value = "한줄 소개", example = "안녕하세요")
    private String                   shortIntroduction;

    @ApiModelProperty(value = "선택사항(학력) 리스트")
    private List<UserSchoolResponseDto>         userSchoolResponseDtoList;
    @ApiModelProperty(value = "선택사항(경력) 리스트")
    private List<UserCareerResponseDto>         userCareerResponseDtoList;
    @ApiModelProperty(value = "선택사항(자격증) 리스트")
    private List<UserLicenseResponseDto>        userLicenseResponseDtoList;
    @ApiModelProperty(value = "선택사항(수상) 리스트")
    private List<UserAwardResponseDto>          userAwardResponseDtoList;
    @ApiModelProperty(value = "선택사항(링크) 리스트")
    private List<UserLinkResponseDto>           userLinkResponseDtoList;
    @ApiModelProperty(value = "선택사항(태그) 리스트")
    private List<HashtagResponseDto>            hashtagResponseDtoList;
}