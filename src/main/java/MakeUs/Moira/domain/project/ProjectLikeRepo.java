package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.user.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectLikeRepo extends JpaRepository<ProjectLike, Long> {
    public Optional<ProjectLike> findByUserHistoryAndProject(UserHistory userHistory, Project project);
}
