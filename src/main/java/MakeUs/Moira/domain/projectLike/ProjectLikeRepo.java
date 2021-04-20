package MakeUs.Moira.domain.projectLike;

import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.userHistory.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectLikeRepo extends JpaRepository<ProjectLike, Long> {
    public Optional<ProjectLike> findByUserHistoryAndProject(UserHistory userHistory, Project project);
}
