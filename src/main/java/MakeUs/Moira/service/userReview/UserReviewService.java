package MakeUs.Moira.service.userReview;

import MakeUs.Moira.controller.userPool.dto.ComplimentMarkWithCountDto;
import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.userReview.dto.UserReviewAddRequestDto;
import MakeUs.Moira.controller.userReview.dto.UserReviewAddResponseDto;
import MakeUs.Moira.controller.userReview.dto.UserReviewDetailResponseDto;
import MakeUs.Moira.controller.userReview.dto.UserReviewResponseDto;
import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfo;
import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfoRepo;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.userReview.UserReview;
import MakeUs.Moira.domain.userReview.UserReviewComplimentMark;
import MakeUs.Moira.domain.userReview.UserReviewRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UserReviewService {

    private final UserRepo               userRepo;
    private final UserProjectRepo        userProjectRepo;
    private final UserReviewRepo         userReviewRepo;
    private final ComplimentMarkInfoRepo complimentMarkInfoRepo;

    @Transactional
    public UserReviewAddResponseDto addUserReview(Long userId, UserReviewAddRequestDto userReviewAddRequestDto) {
        User writtenUserEntity = getUserEntity(userId);
        Long targetUserProjectId = userReviewAddRequestDto.getUserProjectId();

        UserProject targetUserProjectEntity = userProjectRepo.findById(targetUserProjectId)
                                                             .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_PROJECT));

        UserReview newUserReview = userReviewAddRequestDto.toEntity();
        newUserReview.updateUserProject(targetUserProjectEntity);
        newUserReview.updateWrittenUser(writtenUserEntity);

        // 칭찬 뱃지 매핑
        List<ComplimentMarkInfo> complimentMarkInfoList = complimentMarkInfoRepo.findAllById(userReviewAddRequestDto.getComplimentMarkIdList());

        complimentMarkInfoList.forEach((complimentMarkInfo) -> {
            UserReviewComplimentMark newUserReviewComplimentMark = new UserReviewComplimentMark();
            newUserReviewComplimentMark.updateUserReview(newUserReview);
            newUserReviewComplimentMark.updateComplimentMarkInfo(complimentMarkInfo);
        });

        return new UserReviewAddResponseDto(newUserReview);
    }


    public UserReviewResponseDto getUserReview(Long targetId) {

        User userEntity = getUserEntity(targetId);
        Long userHistoryId = userEntity.getUserHistory()
                                       .getId();

        List<UserReview> userReviewList = userReviewRepo.findAllByUserProject_UserHistory_Id(userHistoryId);
        List<ComplimentMarkInfo> complimentMarkInfoList = complimentMarkInfoRepo.findAll();

        List<ComplimentMarkWithCountDto> complimentMarkWithCountDtoList = getComplimentMarkWithCountDtoList(userReviewList, complimentMarkInfoList);

        return new UserReviewResponseDto(userEntity.getNickname(), userReviewList, complimentMarkWithCountDtoList);
    }


    public List<UserReviewDetailResponseDto> getUserReviewDetail(Long targetId, String sortKeyword) {
        Long userHistoryId = getUserHistory(targetId).getId();
        List<UserReview> userReviewList = userReviewRepo.findAllByUserProject_UserHistory_Id(userHistoryId);
        sortUserReviewListByKeyword(userReviewList, sortKeyword);
        return userReviewList.stream()
                             .map(UserReview::toUserReviewDetailResponseDto)
                             .collect(Collectors.toList());
    }


    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }

    private UserHistory getUserHistory(Long targetId) {
        return getUserEntity(targetId).getUserHistory();
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
