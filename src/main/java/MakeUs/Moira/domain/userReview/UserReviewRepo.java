package MakeUs.Moira.domain.userReview;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReviewRepo extends JpaRepository<UserReview, Long> {
    List<UserReview> findAllByUserProject_UserHistory_Id(Long userHistoryId);
}
