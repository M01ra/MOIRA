package MakeUs.Moira.domain.position;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepo extends JpaRepository<UserPosition, Long> {
    public Optional<UserPosition>findByPositionName(String positionName);
}
