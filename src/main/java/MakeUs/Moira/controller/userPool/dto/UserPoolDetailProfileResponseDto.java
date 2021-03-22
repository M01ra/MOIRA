package MakeUs.Moira.controller.userPool.dto;

import MakeUs.Moira.controller.user.dto.myPage.HashtagResponseDto;
import MakeUs.Moira.controller.userPortfolio.userAward.dto.UserAwardResponseDto;
import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerResponseDto;
import MakeUs.Moira.controller.userPortfolio.userLicense.dto.UserLicenseResponseDto;
import MakeUs.Moira.controller.userPortfolio.userLink.dto.UserLinkResponseDto;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.UserSchoolResponseDto;
import MakeUs.Moira.domain.userPool.UserPool;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.userCareer.UserCareer;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserPoolDetailProfileResponseDto {
    private Long                         userId;
    private List<UserSchoolResponseDto>  userSchoolResponseDtoList;
    private List<UserCareerResponseDto>  userCareerResponseDtoList;
    private List<UserLicenseResponseDto> userLicenseResponseDtoList;
    private List<UserAwardResponseDto>   userAwardResponseDtoList;
    private List<UserLinkResponseDto>    userLinkResponseDtoList;
    private List<HashtagResponseDto>     hashtagResponseDtoList;

    public UserPoolDetailProfileResponseDto(UserPortfolio userPortfolio)
    {
        this.userId = userPortfolio.getUser()
                                   .getId();
        this.userSchoolResponseDtoList = userPortfolio.getUserSchoolList()
                                                      .stream()
                                                      .map(UserSchoolResponseDto::new)
                                                      .collect(Collectors.toList());
        this.userCareerResponseDtoList = userPortfolio.getUserCareerList()
                                                      .stream()
                                                      .map(UserCareerResponseDto::new)
                                                      .collect(Collectors.toList());
        this.userLicenseResponseDtoList = userPortfolio.getUserLicenseList()
                                                       .stream()
                                                       .map(UserLicenseResponseDto::new)
                                                       .collect(Collectors.toList());
        this.userAwardResponseDtoList = userPortfolio.getUserAwardList()
                                                     .stream()
                                                     .map(UserAwardResponseDto::new)
                                                     .collect(Collectors.toList());
        this.userLinkResponseDtoList = userPortfolio.getUserLinkList()
                                                    .stream()
                                                    .map(UserLinkResponseDto::new)
                                                    .collect(Collectors.toList());
        this.hashtagResponseDtoList = userPortfolio.getUser()
                                                   .getUserHistory()
                                                   .getUserHashtags()
                                                   .stream()
                                                   .map(HashtagResponseDto::new)
                                                   .collect(Collectors.toList());
    }
}
