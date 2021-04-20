package MakeUs.Moira.domain.userHashtag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserHashtagRepo extends JpaRepository<UserHashtag, Long> {
    List<UserHashtag> findAllByUserHistoryId(Long userHistoryId);
    void deleteAllByUserHistoryId(Long userHistoryId);
}

