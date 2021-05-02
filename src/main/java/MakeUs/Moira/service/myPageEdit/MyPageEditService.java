package MakeUs.Moira.service.myPageEdit;

import MakeUs.Moira.controller.myPageEdit.dto.*;
import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.domain.position.PositionRepo;
import MakeUs.Moira.domain.position.UserPosition;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.userHashtag.UserHashtag;
import MakeUs.Moira.domain.userHashtag.UserHashtagRepo;
import MakeUs.Moira.domain.userHistory.UserHistory;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.exception.CustomException;
import MakeUs.Moira.exception.ErrorCode;
import MakeUs.Moira.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class MyPageEditService {

    private final UserRepo        userRepo;
    private final PositionRepo    positionRepo;
    private final UserHashtagRepo userHashtagRepo;
    private final HashtagRepo     hashtagRepo;
    private final S3Service       s3Service;


    public MyPageEditResponseDto getMyPageEdit(Long userId) {
        User userEntity = getUserEntity(userId);
        UserPortfolio userPortfolio = userEntity.getUserPortfolio();
        return new MyPageEditResponseDto(userEntity, userPortfolio);
    }

    @Transactional
    public MyPageEditNicknameResponseDto updateMyPageEditNickname(Long userId,
                                                                  MyPageEditNicknameRequestDto myPageEditNicknameRequestDto)
    {
        String newNickname = myPageEditNicknameRequestDto.getNickname();
        if (isDuplicatedNickname(newNickname)) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_NICKNAME);
        }

        User userEntity = getUserEntity(userId);
        userEntity.updateNickname(newNickname);
        return userEntity.toMyPageEditNicknameResponseDto();
    }


    public List<MyPageEditPositionInfoDto> getPositionList() {
        List<UserPosition> userPositionList = positionRepo.findAll();
        return userPositionList.stream()
                               .map(UserPosition::toMyPageEditPositionInfoDto)
                               .collect(Collectors.toList());
    }


    @Transactional
    public MyPageEditPositionResponseDto updateMyPageEditPosition(Long userId,
                                                                  MyPageEditPositionRequestDto myPageEditPositionRequestDto)
    {
        User userEntity = getUserEntity(userId);
        UserPosition userPosition = positionRepo.findById(myPageEditPositionRequestDto.getPositionId())
                                                .orElseThrow(()->new CustomException(ErrorCode.INVALID_POSITION));
        userEntity.updateUserPosition(userPosition);
        return userEntity.toMyPageEditPositionResponseDto();
    }


    @Transactional
    public MyPageEditIntroductionResponseDto updateMyPageEditIntroduction(Long userId,
                                                                          MyPageEditIntroductionRequestDto myPageEditIntroductionRequestDto)
    {
        User userEntity = getUserEntity(userId);
        userEntity.updateShorIntroduction(myPageEditIntroductionRequestDto.getShortIntroduction());
        return userEntity.toMyPageEditIntroductionResponseDto();
    }

    @Transactional
    public MyPageEditHashtagResponseDto updateMyPageEditHashtag(Long userId,
                                                                MyPageEditHashtagRequestDto myPageEditHashtagRequestDto)
    {

        UserHistory userHistoryEntity = getUserEntity(userId).getUserHistory();

        // 고아 객체 적용
        userHistoryEntity.getUserHashtags()
                         .clear();

        hashtagRepo.findAllByIdIn(myPageEditHashtagRequestDto.getHashtagIdList())
                   .orElseThrow(()->new CustomException(ErrorCode.INVALID_HASHTAG))
                   .forEach((hashtagEntity) -> {
                       UserHashtag userHashtagEntity = new UserHashtag();
                       userHashtagEntity.updateHashtag(hashtagEntity)
                                        .updateUserHistory(userHistoryEntity);
                       userHashtagRepo.saveAndFlush(userHashtagEntity);
                   });

        return userHistoryEntity.toMyPageEditHashtagResponseDto();
    }

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }

    private boolean isDuplicatedNickname(String newNickname) {
        return userRepo.findByNickname(newNickname)
                       .isPresent();
    }

    @Transactional
    public String updateMyPageEditImage(Long userId,
                                         MultipartFile image)
    {
        User userEntity = getUserEntity(userId);

        // 기존에 존재했을 경우 삭제
        if(userEntity.getProfileImage() != null){
            s3Service.delete(userEntity.getProfileImageKey());
        }
        String key = s3Service.createUUIDKey(image.getOriginalFilename());
        String imageUrl = s3Service.upload(image, key);
        userEntity.updateProfileImage(imageUrl);
        userEntity.updateProfileImageKey(key);
        return imageUrl;
    }
}
