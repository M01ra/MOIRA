package MakeUs.Moira.domain.position;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionCategoryRepo extends JpaRepository<PositionCategory, Long> {
    Optional<PositionCategory> findByCategoryName(String categoryName);
}
