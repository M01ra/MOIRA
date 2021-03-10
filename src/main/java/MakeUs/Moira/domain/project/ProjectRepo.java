package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.hashtag.Hashtag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {
    public List<Project> findProjectsByProjectStatusEqualsAndProjectHashtagListIn(ProjectStatus projectStatus, List<Hashtag> hashtagList, Pageable pageable);
    public List<Project> findProjectsByProjectStatusEquals(ProjectStatus projectStatus, Pageable pageable);
}
