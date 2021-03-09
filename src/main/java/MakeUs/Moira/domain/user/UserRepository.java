package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
