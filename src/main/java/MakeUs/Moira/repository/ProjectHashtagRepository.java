package MakeUs.Moira.repository;

import MakeUs.Moira.domain.project.ProjectHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectHashtagRepository extends JpaRepository<ProjectHashtag, Long> {

}
