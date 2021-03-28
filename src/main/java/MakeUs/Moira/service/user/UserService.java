package MakeUs.Moira.service.user;


import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.user.dto.myPage.HashtagResponseDto;
import MakeUs.Moira.controller.user.dto.signup.PositionResponseDto;
import MakeUs.Moira.controller.user.dto.signup.SignupResponseDto;
import MakeUs.Moira.domain.hashtag.Hashtag;
import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.domain.position.PositionRepo;
import MakeUs.Moira.domain.position.UserPosition;
import MakeUs.Moira.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo        userRepo;
    private final UserHistoryRepo userHistoryRepo;
    private final PositionRepo    positionRepo;
    private final UserHashtagRepo userHashtagRepo;
    private final HashtagRepo     hashtagRepo;


    public boolean isDuplicatedNickname(String nickname) {
        return userRepo.findByNickname(nickname)
                       .isPresent();
    }

    @Transactional
    public SignupResponseDto signup(Long userId, String nickname, Long positionId, List<Long> hashtagIdList) {

        // 1. 유저 엔티티
        User userEntity = findUserById(userId);

        // 2. 닉네임
        if (isDuplicatedNickname(nickname)) {
            throw new CustomException(ErrorCode.ALREADY_REGISTRED_NICKNAME);
        }
        userEntity.updateNickname(nickname);

        // 3. 포지션
        UserPosition userPositionEntity = getUserPosition(positionId);
        userEntity.updateUserPosition(userPositionEntity);

        // 4. 한 줄 자기소개 기본 값 설정
        userEntity.updateShorIntroduction("안녕하세요! " + nickname + "입니다!");

        // 5.. UserHistory 가져오기 + hashtagId 로 hashtagList 가져오기 => UserHashtag 만들고 저장
        UserHistory userHistoryEntity = userEntity.getUserHistory();
        if (!isEmptyHashtagList(userHistoryEntity)) {
            // 고아객체 적용
            userHistoryEntity.getUserHashtags()
                             .clear();
        }

        getHashtagList(hashtagIdList).forEach((hashtagEntity) -> {
            UserHashtag userHashtagEntity = new UserHashtag();
            userHashtagEntity.updateHashtag(hashtagEntity)
                             .updateUserHistory(userHistoryEntity);
            userHashtagRepo.save(userHashtagEntity);
        });

        PositionResponseDto positionResponseDto = getPositionResponseDto(userEntity);
        List<HashtagResponseDto> hashtagResponseDtoList = getHashtagResponseDtoList(userEntity);

        return SignupResponseDto.builder()
                                .userId(userEntity.getId())
                                .nickname(userEntity.getNickname())
                                .positionResponseDto(positionResponseDto)
                                .hashtagResponseDtoList(hashtagResponseDtoList)
                                .build();
    }

    @Transactional
    public List<String> getUserHashtags(Long userId) {
        UserHistory userHistory = findUserHistoryById(userId);
        return userHashtagRepo.findAllByUserHistoryId(userHistory.getId())
                              .stream()
                              .map(userHashtag -> userHashtag.getHashtag()
                                                             .getHashtagName())
                              .collect(Collectors.toList());
    }


    private User findUserById(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }

    private UserHistory findUserHistoryById(Long userId) {
        return userHistoryRepo.findByUserId(userId)
                              .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }

    private UserPosition getUserPosition(Long positionId) {
        return positionRepo.findById(positionId)
                           .orElseThrow(() -> new CustomException(ErrorCode.INVALID_POSITION));
    }

    private boolean isEmptyHashtagList(UserHistory userHistoryEntity) {
        return userHistoryEntity.getUserHashtags()
                                .isEmpty();
    }

    private List<Hashtag> getHashtagList(List<Long> hashtagIdList) {
        return hashtagRepo.findAllByIdIn(hashtagIdList)
                          .orElseThrow(() -> new CustomException(ErrorCode.INVALID_HASHTAG));
    }

    private PositionResponseDto getPositionResponseDto(User userEntity) {
        return PositionResponseDto.builder()
                                  .positionId(userEntity.getUserPosition()
                                                        .getId())
                                  .positionName(userEntity.getUserPosition()
                                                          .getPositionName())
                                  .build();
    }

    private List<HashtagResponseDto> getHashtagResponseDtoList(User userEntity) {
        return userEntity.getUserHistory()
                         .getUserHashtags()
                         .stream()
                         .map(HashtagResponseDto::new)
                         .collect(Collectors.toList());
    }
}
