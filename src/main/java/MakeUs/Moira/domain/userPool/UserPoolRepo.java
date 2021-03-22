package MakeUs.Moira.domain.userPool;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPoolRepo extends JpaRepository<UserPool, Long> {
    List<UserPool> findAllByUser_UserPosition_PositionCategory_CategoryName(String positionCategoryName, Pageable pageable);
}
