package MakeUs.Moira.repository;

import MakeUs.Moira.domain.user.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
}
