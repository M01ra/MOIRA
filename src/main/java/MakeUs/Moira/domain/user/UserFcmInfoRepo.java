package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.user.UserFcmInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFcmInfoRepo extends JpaRepository<UserFcmInfo, Long> {
    Optional<UserFcmInfo> findByUser_Id(Long userId);
}
