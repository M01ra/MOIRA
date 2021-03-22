package MakeUs.Moira.domain.userPool;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserPoolLikeRepo extends JpaRepository<UserPoolLike, Long> {
    UserPoolLike findByUserHistory_IdAndUserPool_Id(Long userHistoryId, Long userPoolId);
}
