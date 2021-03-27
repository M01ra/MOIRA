package MakeUs.Moira.domain.position;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepo extends JpaRepository<UserPosition, Long> {
    List<UserPosition> findAllByPositionCategory_Id(Long positionCategoryId);
}
