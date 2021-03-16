package MakeUs.Moira.domain.hashtag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepo extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findHashtagByHashtagName(String hashtagName);
    Optional<List<Hashtag>> findAllByIdIn(List<Long> hashtagIdList);
}
