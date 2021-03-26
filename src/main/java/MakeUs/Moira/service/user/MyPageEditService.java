package MakeUs.Moira.service.user;

import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.user.dto.myPageEdit.MyPageEditResponseDto;
import MakeUs.Moira.controller.user.dto.myPageEdit.MyPageEditProfileUpdateResponseDto;
import MakeUs.Moira.controller.user.dto.myPageEdit.MyPageEditProfileUpdateRequestDto;
import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.domain.position.PositionRepo;
import MakeUs.Moira.domain.position.UserPosition;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class MyPageEditService {

    private final UserRepo        userRepo;
    private final PositionRepo    positionRepo;
    private final UserHashtagRepo userHashtagRepo;
    private final HashtagRepo     hashtagRepo;


    public MyPageEditResponseDto getMyPageEdit(Long userId) {
        User userEntity = getUserEntity(userId);
        UserPortfolio userPortfolio = userEntity.getUserPortfolio();
        return new MyPageEditResponseDto(userEntity, userPortfolio);
    }


    @Transactional
    public MyPageEditProfileUpdateResponseDto updateMyPageProfile(Long userId,
                                                                  MyPageEditProfileUpdateRequestDto myPageEditProfileUpdateRequestDto) {
        User userEntity = getUserEntity(userId);

        userEntity.updateNickname(myPageEditProfileUpdateRequestDto.getNickname());

        UserPosition userPositionEntity = positionRepo.findById(myPageEditProfileUpdateRequestDto.getPositionId())
                                                      .orElseThrow(() -> new CustomException(ErrorCode.INVALID_POSITION));

        userEntity.updateUserPosition(userPositionEntity);
        userEntity.updateShorIntroduction(myPageEditProfileUpdateRequestDto.getShortIntroduction());


        UserHistory userHistoryEntity = userEntity.getUserHistory();
        // cascade라서 그냥 알아서 같이 다 삭제되지 않을까?  + 고아객체 적용
        userHistoryEntity.getUserHashtags()
                         .clear();

        hashtagRepo.findAllByIdIn(myPageEditProfileUpdateRequestDto.getHashtagIdList())
                   .orElseThrow(() -> new CustomException(ErrorCode.INVALID_HASHTAG))
                   .forEach((hashtagEntity) -> {
                       UserHashtag userHashtagEntity = new UserHashtag();
                       userHashtagEntity.updateHashtag(hashtagEntity)
                                        .updateUserHistory(userHistoryEntity);
                       userHashtagRepo.saveAndFlush(userHashtagEntity);
                   });

        return new MyPageEditProfileUpdateResponseDto(userEntity);
    }

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }
}
