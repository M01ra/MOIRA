package MakeUs.Moira.domain.position;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepo extends JpaRepository<Position, Long> {
    public Optional<Position>findByPositionName(String positionName);
}
