package MakeUs.Moira.repository;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
    public Optional<UserHistory> findByUser(User user);
}
