package MakeUs.Moira.service.user;


import MakeUs.Moira.advice.exception.DuplicatedNicknameException;
import MakeUs.Moira.advice.exception.InvalidUserIdException;
import MakeUs.Moira.controller.user.dto.HashtagResponseDto;
import MakeUs.Moira.controller.user.dto.PositionResponseDto;
import MakeUs.Moira.controller.user.dto.SignupResponseDto;
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

    private final UserRepo userRepo;
    private final UserHistoryRepo userHistoryRepo;
    private final PositionRepo positionRepo;
    private final UserHashtagRepo userHashtagRepo;
    private final HashtagRepo hashtagRepo;


    private User findUserById(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));
    }


    public boolean isDuplicatedNickname(String nickname) {
        return userRepo.findByNickname(nickname)
                       .isPresent();
    }


    @Transactional
    public SignupResponseDto signup(Long userId, String nickname, Long positionId, List<Long> hashtagIdList) {

        // 1. 유저 엔티티
        User userEntity = findUserById(userId);

        // 2. 닉네임
        if (isDuplicatedNickname(nickname)) throw new DuplicatedNicknameException("중복된 닉네임");
        userEntity.updateNickname(nickname);

        // 3. 포지션
        UserPosition userPositionEntity = positionRepo.findById(positionId)
                                                      .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 PositionId"));
        userEntity.updateUserPosition(userPositionEntity);

        // 4. UserHistory 가져오기 + hashtagId 로 hashtagList 가져오기 => UserHashtag 만들고 저장
        UserHistory userHistoryEntity = userEntity.getUserHistory();
        if (!userHistoryEntity.getUserHashtags().isEmpty()) {

            userHistoryEntity.getUserHashtags()
                             .forEach(userHashtagRepo::delete);
            userHistoryEntity.getUserHashtags()
                             .clear();
        }

        hashtagRepo.findAllByIdIn(hashtagIdList)
                   .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 hashtagId"))
                   .forEach((hashtagEntity) -> {
                       UserHashtag userHashtagEntity = new UserHashtag();
                       userHashtagEntity.updateUserHistory(userHistoryEntity)
                                        .updateHashtag(hashtagEntity);
                       userHashtagRepo.saveAndFlush(userHashtagEntity);
                   });


        PositionResponseDto positionResponseDto = PositionResponseDto.builder()
                                                                     .positionId(userEntity.getUserPosition()
                                                                                           .getId())
                                                                     .positionName(userEntity.getUserPosition()
                                                                                             .getPositionName())
                                                                     .build();


        List<HashtagResponseDto> hashtagResponseDtoList = userEntity.getUserHistory()
                                                                    .getUserHashtags()
                                                                    .stream()
                                                                    .map(userHashtag -> HashtagResponseDto.builder()
                                                                                                          .hashtagId(userHashtag.getHashtag()
                                                                                                                         .getId())
                                                                                                          .hashtagName(userHashtag.getHashtag()
                                                                                                                                  .getHashtagName())
                                                                                                          .build())
                                                                    .collect(Collectors.toList());
        return SignupResponseDto.builder()
                                .userId(userEntity.getId())
                                .nickname(userEntity.getNickname())
                                .positionResponseDto(positionResponseDto)
                                .hashtagResponseDtoList(hashtagResponseDtoList)
                                .build();
    }
}
