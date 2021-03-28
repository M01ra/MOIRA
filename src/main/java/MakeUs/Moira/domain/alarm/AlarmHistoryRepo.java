package MakeUs.Moira.domain.alarm;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmHistoryRepo extends JpaRepository<AlarmHistory, Long> {
    List<AlarmHistory> findByUserId(Long userId);
    List<AlarmHistory> findByUserIdOrderByCreatedDateDesc(Long userId, Pageable pageable);
}
