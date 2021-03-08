package MakeUs.Moira.repository;

import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Long> {
}
