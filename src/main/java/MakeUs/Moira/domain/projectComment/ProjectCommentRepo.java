package MakeUs.Moira.domain.projectComment;

import MakeUs.Moira.domain.projectDetail.ProjectDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectCommentRepo extends JpaRepository<ProjectComment, Long> {
    public List<ProjectComment> findAllByProjectDetailOrderByCreatedDate(ProjectDetail projectDetail);
}
