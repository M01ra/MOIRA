package MakeUs.Moira.repository;

import MakeUs.Moira.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
