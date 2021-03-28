package MakeUs.Moira.domain.alarm;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmHistoryRepo extends JpaRepository<AlarmHistory, Long> {
    List<AlarmHistory> findByUserId(Long userId);
}
