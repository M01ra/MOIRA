package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.user.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepo extends JpaRepository<UserProject, Long> {
}
