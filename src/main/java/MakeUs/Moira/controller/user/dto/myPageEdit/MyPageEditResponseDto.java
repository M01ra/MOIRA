package MakeUs.Moira.controller.user.dto.myPageEdit;


import MakeUs.Moira.controller.user.dto.myPage.HashtagResponseDto;
import MakeUs.Moira.controller.userPortfolio.userAward.dto.UserAwardResponseDto;
import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerResponseDto;
import MakeUs.Moira.controller.userPortfolio.userLicense.dto.UserLicenseResponseDto;
import MakeUs.Moira.controller.userPortfolio.userLink.dto.UserLinkResponseDto;

import MakeUs.Moira.controller.userPortfolio.userSchool.dto.UserSchoolResponseDto;
import MakeUs.Moira.domain.user.User;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class MyPageEditResponseDto {
    private String                   profileImageUrl;
    private String                   nickname;
    private String                   positionName;
    private String                   shortIntroduction;
    private List<HashtagResponseDto> hashtagResponseDtoList;

    private List<UserSchoolResponseDto>         userSchoolResponseDtoList;
    private List<UserCareerResponseDto>         userCareerResponseDtoList;
    private List<UserLicenseResponseDto>        userLicenseResponseDtoList;
    private List<UserAwardResponseDto>          userAwardResponseDtoList;
    private List<UserLinkResponseDto>           userLinkResponseDtoList;
    //private List<UserPrivateProjectResponseDto> userPrivateProjectResponseDtoList;

    public MyPageEditResponseDto(User user, UserPortfolio userPortfolio) {
        this.profileImageUrl = user.getProfileImage();
        this.nickname = user.getNickname();
        this.positionName = user.getUserPosition().getPositionName();
        this.shortIntroduction = user.getShortIntroduction();
        this.hashtagResponseDtoList = user.getUserHistory()
                                          .getUserHashtags()
                                          .stream()
                                          .map(HashtagResponseDto::new)
                                          .collect(Collectors.toList());

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
//        this.userPrivateProjectResponseDtoList = userPortfolio.getUserPrivateProjectList()
//                                                              .stream()
//                                                              .map(UserSchoolResponseDto::new)
//                                                              .collect(Collectors.toList());;
    }
}