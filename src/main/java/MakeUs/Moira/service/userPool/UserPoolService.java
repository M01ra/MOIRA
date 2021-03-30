package MakeUs.Moira.service.userPool;


import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.userPool.dto.UserPoolDetailReviewDetailResponseDto;
import MakeUs.Moira.controller.userPool.dto.*;

import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfo;
import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfoRepo;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.userPool.UserPool;
import MakeUs.Moira.domain.userPool.UserPoolLike;
import MakeUs.Moira.domain.userPool.UserPoolLikeRepo;
import MakeUs.Moira.domain.userPool.UserPoolRepo;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;

import MakeUs.Moira.domain.userReview.UserReview;
import MakeUs.Moira.domain.userReview.UserReviewRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserPoolService {

    private final UserRepo               userRepo;
    private final UserPoolRepo           userPoolRepo;
    private final UserPoolLikeRepo       userPoolLikeRepo;
    private final UserReviewRepo         userReviewRepo;
    private final ComplimentMarkInfoRepo complimentMarkInfoRepo;


    @Transactional
    public UserPoolOnOffResponseDto switchUserPoolVisibility(Long userId) {
        UserPool userPoolEntity = getUserEntity(userId).getUserPool();
        userPoolEntity.switchVisible();
        return userPoolEntity.toUserPoolOnOffResponseDto();
    }


    public List<UserPoolResponseDto> getUserPool(Long userId, int page, String positionCategory,
                                                 String sortKeyword)
    {
        User userEntity = getUserEntity(userId);

        Pageable pageable = getPageableWithSortKeyword(page, sortKeyword);
        List<UserPool> userPoolFilteredList = userPoolRepo.findAllByUser_UserPosition_PositionCategory_CategoryName(positionCategory, pageable);

        return userPoolFilteredList.stream()
                                   .filter(UserPool::isVisible)
                                   .map(userPoolFiltered -> new UserPoolResponseDto(userEntity, userPoolFiltered))
                                   .collect(Collectors.toList());
    }


    public List<UserPoolResponseDto> getUserPoolByNickname(Long userId, String keyword) {

        User userEntity = getUserEntity(userId);

        List<User> userResultList = userRepo.findByNicknameContaining(keyword);
        return userResultList.stream()
                             .map(User::getUserPool)
                             .filter(UserPool::isVisible)
                             .map(userPool -> new UserPoolResponseDto(userEntity, userPool))
                             .collect(Collectors.toList());
    }


    @Transactional
    public UserPoolLikeAddResponse updateUserPoolLike(Long userId, Long userPoolId) {

        UserHistory userHistoryEntity = getUserEntity(userId).getUserHistory();
        UserPool userPoolEntity = getUserPoolEntity(userPoolId);

        UserPoolLike userPoolLikeEntity = getUserPoolLikeEntity(userPoolId, userHistoryEntity, userPoolEntity);
        return new UserPoolLikeAddResponse(userPoolLikeEntity);
    }


    @Transactional
    public UserPoolDetailProfileResponseDto getUserPoolDetailProfile(Long userPoolId) {
        UserPool userPoolEntity = getUserPoolEntity(userPoolId);
        userPoolEntity.updateHitCount();
        UserPortfolio userPortfolioEntity = userPoolEntity.getUser()
                                                          .getUserPortfolio();
        return new UserPoolDetailProfileResponseDto(userPortfolioEntity);
    }


    public UserPoolDetailReviewResponseDto getUserPoolDetailReview(Long userPoolId) {
        UserHistory userHistory = getUserHistoryEntity(userPoolId);
        Long userHistoryId = userHistory.getId();

        List<UserReview> userReviewList = getUserReviewList(userHistoryId);
        List<ComplimentMarkInfo> complimentMarkInfoList = complimentMarkInfoRepo.findAll();

        List<ComplimentMarkWithCountDto> complimentMarkWithCountDtoList
                = getComplimentMarkWithCountDtoList(userReviewList, complimentMarkInfoList);

        return new UserPoolDetailReviewResponseDto(userHistory.getUser()
                                                              .getNickname(), userReviewList, complimentMarkWithCountDtoList);
    }


    public List<UserPoolDetailReviewDetailResponseDto> getUserPoolDetailReviewDetail(Long userPoolId,
                                                                                     String sortKeyword)
    {
        Long userHistoryId = getUserHistoryEntity(userPoolId).getId();
        List<UserReview> userReviewList = userReviewRepo.findAllByUserProject_UserHistory_Id(userHistoryId);
        sortUserReviewListByKeyword(userReviewList, sortKeyword);
        return userReviewList.stream()
                             .map(UserReview::toUserPoolDetailReviewDetailResponseDto)
                             .collect(Collectors.toList());
    }


    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }

    private UserPool getUserPoolEntity(Long userPoolId) {
        return userPoolRepo.findById(userPoolId)
                           .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_POOL));
    }

    private UserHistory getUserHistoryEntity(Long userPoolId) {
        return userPoolRepo.findById(userPoolId)
                           .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_POOL))
                           .getUser()
                           .getUserHistory();
    }

    private Pageable getPageableWithSortKeyword(int page, String sortby) {
        switch (sortby) {
            case "date": {
                return PageRequest.of(page - 1, 10, Sort.by("modifiedDate")
                                                        .descending());
            }
            case "hit": {
                return PageRequest.of(page - 1, 10, Sort.by("hitCount")
                                                        .descending());
            }
            case "like": {
                return PageRequest.of(page - 1, 10, Sort.by("likeCount")
                                                        .descending());
            }
            default: {
                throw new CustomException(ErrorCode.INVALID_SORT);
            }
        }
    }


    private UserPoolLike getUserPoolLikeEntity(Long userPoolId, UserHistory userHistoryEntity,
                                               UserPool userPoolEntity)
    {

        UserPoolLike userPoolLikeEntity = userPoolLikeRepo.findByUserHistory_IdAndUserPool_Id(userHistoryEntity.getId(), userPoolId);

        // UserPoolLike 가 이미 있는지 검사

        //      a) 있으면, 값만 변경
        if (userPoolLikeEntity != null) {
            userPoolLikeEntity.switchIsLiked();
            return userPoolLikeEntity;
        }

        //      b) 없으면, 새로 생성
        UserPoolLike newUserPoolLikeEntity = new UserPoolLike();
        newUserPoolLikeEntity.updateUserPool(userPoolEntity)
                             .updateUserHistory(userHistoryEntity);
        newUserPoolLikeEntity.getUserPool()
                             .updateLikeCount(+1);
        //userPoolLikeRepo.saveAndFlush(newUserPoolLikeEntity);
        return newUserPoolLikeEntity;

    }

    private List<UserReview> getUserReviewList(Long userHistoryId) {
        return userReviewRepo.findAllByUserProject_UserHistory_Id(userHistoryId);
    }

    private List<ComplimentMarkWithCountDto> getComplimentMarkWithCountDtoList(List<UserReview> userReviewList,
                                                                               List<ComplimentMarkInfo> complimentMarkInfoList)
    {
        return complimentMarkInfoList.stream()
                                     .map(complimentMarkInfo -> complimentMarkInfo.getComplimentMarkWithCountDto(userReviewList))
                                     .collect(Collectors.toList());
    }

    private void sortUserReviewListByKeyword(List<UserReview> userReviewList, String sortKeyword) {
        if (sortKeyword.equals("date")) {
            userReviewList.sort(Comparator.comparing(AuditorEntity::getCreatedDate)
                                          .reversed());
        } else if (sortKeyword.equals("point")) {
            userReviewList.sort(Comparator.comparing(UserReview::getMannerPoint)
                                          .reversed());
        } else {
            throw new CustomException(ErrorCode.INVALID_SORT);
        }
    }
}
