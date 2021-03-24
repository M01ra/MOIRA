package MakeUs.Moira.domain.userReview;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReviewComplimentMarkRepo extends JpaRepository<UserReviewComplimentMark, Long> {
    List<UserReviewComplimentMark> findAllByUserReview_UserProject_UserHistory_Id(Long userHistoryId);
}
