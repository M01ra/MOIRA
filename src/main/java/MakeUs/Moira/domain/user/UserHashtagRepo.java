package MakeUs.Moira.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserHashtagRepo extends JpaRepository<UserHashtag, Long> {
    void deleteAllByUserHistoryId(Long userHistoryId);
}

