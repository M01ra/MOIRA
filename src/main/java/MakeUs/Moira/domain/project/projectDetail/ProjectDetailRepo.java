package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDetailRepo extends JpaRepository<ProjectDetail, Long> {
}
