package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.user.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProjectRepo extends JpaRepository<UserProject, Long> {
    public Optional<UserProject> findByUserHistoryIdAndProjectId(Long userHistoryId, Long projectId);
}
