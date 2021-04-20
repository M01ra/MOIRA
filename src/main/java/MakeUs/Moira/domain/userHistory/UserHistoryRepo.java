package MakeUs.Moira.domain.userHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserHistoryRepo extends JpaRepository<UserHistory, Long> {
    public Optional<UserHistory> findByUserId(Long userId);
}
