package MakeUs.Moira.service.userPool;


import MakeUs.Moira.advice.exception.InvalidUserIdException;
import MakeUs.Moira.controller.userPool.dto.UserPoolDetailProfileResponseDto;
import MakeUs.Moira.controller.userPool.dto.UserPoolLikeAddResponse;
import MakeUs.Moira.controller.userPool.dto.UserPoolResponseDto;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserHistory;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.userPool.UserPool;
import MakeUs.Moira.domain.userPool.UserPoolLike;
import MakeUs.Moira.domain.userPool.UserPoolLikeRepo;
import MakeUs.Moira.domain.userPool.UserPoolRepo;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserPoolService {

    private final UserRepo         userRepo;
    private final UserPoolRepo     userPoolRepo;
    private final UserPoolLikeRepo userPoolLikeRepo;


    @Transactional
    public boolean addUserPool(Long userId) {
        try {
            User userEntity = getUserEntity(userId);
            userEntity.updateUserPool(new UserPool());
            UserPool userPoolEntity = userEntity.getUserPool();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public List<UserPoolResponseDto> getUserPoolList(Long userId, int page, String positionCategory,
                                                     String sortKeyword)
    {
        User userEntity = getUserEntity(userId);

        String positionCategoryFilter = parseToFilter(positionCategory);
        Pageable pageable = getPageableWithSortKeyword(page, sortKeyword);
        List<UserPool> userPoolListFiltered = userPoolRepo.findAllByUser_UserPosition_PositionCategory_CategoryName(positionCategoryFilter, pageable);

        return userPoolListFiltered.stream()
                                   .map(userPoolFiltered -> new UserPoolResponseDto(userEntity, userPoolFiltered))
                                   .collect(Collectors.toList());
    }


    @Transactional
    public UserPoolLikeAddResponse updateUserPoolLike(Long userId, Long userPoolId) {

        UserHistory userHistoryEntity = getUserEntity(userId).getUserHistory();
        UserPool userPoolEntity = userPoolRepo.findById(userPoolId)
                                              .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 userPoolId"));

        UserPoolLike userPoolLikeEntity = getUserPoolLikeEntity(userPoolId, userHistoryEntity, userPoolEntity);
        return new UserPoolLikeAddResponse(userPoolLikeEntity);
    }


    public UserPoolDetailProfileResponseDto getUserPoolDetailProfile(Long userPoolId) {
        UserPool userPoolEntity = userPoolRepo.findById(userPoolId)
                                              .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 userPoolId"));
        UserPortfolio userPortfolioEntity = userPoolEntity.getUser()
                                                          .getUserPortfolio();
        return new UserPoolDetailProfileResponseDto(userPortfolioEntity);
    }


    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));
    }

    private Pageable getPageableWithSortKeyword(int page, String sortby) {
        switch (sortby) {
            case "date": {
                return PageRequest.of(page - 1, 10, Sort.by("createdDate")
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
        //      b) 없으면, 새로 생성

        if (userPoolLikeEntity != null) {
            userPoolLikeEntity.switchIsLiked();
            return userPoolLikeEntity;
        } else {
            UserPoolLike newUserPoolLikeEntity = new UserPoolLike();
            newUserPoolLikeEntity.updateUserPool(userPoolEntity)
                                 .updateUserHistory(userHistoryEntity);
            newUserPoolLikeEntity.getUserPool()
                                 .updateLikeCount(+1);
            //userPoolLikeRepo.saveAndFlush(newUserPoolLikeEntity);
            return newUserPoolLikeEntity;
        }
    }


}
