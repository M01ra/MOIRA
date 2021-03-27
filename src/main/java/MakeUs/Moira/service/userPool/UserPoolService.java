package MakeUs.Moira.service.userPool;


import MakeUs.Moira.advice.exception.InvalidUserIdException;
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

        String positionCategoryFilter = parseToFilter(positionCategory);
        Pageable pageable = getPageableWithSortKeyword(page, sortKeyword);
        List<UserPool> userPoolFilteredList = userPoolRepo.findAllByUser_UserPosition_PositionCategory_CategoryName(positionCategoryFilter, pageable);

        return userPoolFilteredList.stream()
                                   .filter(UserPool::isVisible)
                                   .map(userPoolFiltered -> new UserPoolResponseDto(userEntity, userPoolFiltered))
                                   .collect(Collectors.toList());
    }


    public List<UserPoolResponseDto> getUserPoolByNickname(Long userId, String keyword) {

        keywordLengthValidation(keyword);

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
        Long userHistoryId = getUserHistoryEntity(userPoolId).getId();

        List<UserReview> userReviewList = getUserReviewList(userHistoryId);
        List<ComplimentMarkInfo> complimentMarkInfoList = complimentMarkInfoRepo.findAll();

        List<ComplimentMarkWithCountDto> complimentMarkWithCountDtoList
                = getComplimentMarkWithCountDtoList(userReviewList, complimentMarkInfoList);

        return new UserPoolDetailReviewResponseDto(userReviewList, complimentMarkWithCountDtoList);
    }


    public List<UserPoolDetailReviewDetailResponseDto> getUserPoolDetailReviewDetail(Long userPoolId, String sortKeyword) {
        Long userHistoryId = getUserHistoryEntity(userPoolId).getId();
        List<UserReview> userReviewList = userReviewRepo.findAllByUserProject_UserHistory_Id(userHistoryId);
        sortUserReviewListByKeyword(userReviewList, sortKeyword);
        return userReviewList.stream()
                             .map(UserReview::toUserPoolDetailReviewDetailResponseDto)
                             .collect(Collectors.toList());
    }


    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));
    }

    private UserPool getUserPoolEntity(Long userPoolId) {
        return userPoolRepo.findById(userPoolId)
                           .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 userPoolId"));
    }

    private UserHistory getUserHistoryEntity(Long userPoolId) {
        return userPoolRepo.findById(userPoolId)
                           .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 userPoolId"))
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
                throw new IllegalArgumentException("유효하지 않은 sortKeyword");
            }
        }
    }

    private String parseToFilter(String positionCategory) {
        switch (positionCategory) {
            case "develop":
                return positionCategory = "개발자";
            case "director":
                return positionCategory = "기획자";
            case "designer":
                return positionCategory = "디자이너";
            default:
                throw new IllegalArgumentException("유효하지 않은 positionCategory");
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

    private void keywordLengthValidation(String keyword) {
        if (keyword.length() < 3) {
            throw new IllegalArgumentException("검색어의 길이가 3글자 미만입니다.");
        }
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
            throw new IllegalArgumentException("유효하지 않은 sort");
        }
    }
}
