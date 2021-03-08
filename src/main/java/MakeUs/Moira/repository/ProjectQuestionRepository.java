package MakeUs.Moira.repository;

import MakeUs.Moira.domain.project.projectDetail.ProjectQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectQuestionRepository extends JpaRepository<ProjectQuestion, Long> {
}
