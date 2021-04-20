package MakeUs.Moira.domain.projectHashtag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectHashtagRepo extends JpaRepository<ProjectHashtag, Long> {

}
