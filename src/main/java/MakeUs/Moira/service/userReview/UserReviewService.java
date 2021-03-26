package MakeUs.Moira.service.userReview;

import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.userReview.dto.UserReviewAddRequestDto;
import MakeUs.Moira.controller.userReview.dto.UserReviewAddResponseDto;
import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfo;
import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfoRepo;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.userReview.UserReview;
import MakeUs.Moira.domain.userReview.UserReviewComplimentMark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@RequiredArgsConstructor
@Service
public class UserReviewService {

    private final UserRepo               userRepo;
    private final UserProjectRepo        userProjectRepo;
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

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }
}
