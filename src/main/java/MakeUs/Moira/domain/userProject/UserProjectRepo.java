package MakeUs.Moira.domain.userProject;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProjectRepo extends JpaRepository<UserProject, Long> {
    Optional<UserProject> findByUserHistoryIdAndProjectId(Long userHistoryId, Long projectId);

    List<UserProject> findAllByProject_Id(Long projectId);
}