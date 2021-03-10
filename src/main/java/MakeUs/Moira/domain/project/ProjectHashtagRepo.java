package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.project.ProjectHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectHashtagRepo extends JpaRepository<ProjectHashtag, Long> {

}
