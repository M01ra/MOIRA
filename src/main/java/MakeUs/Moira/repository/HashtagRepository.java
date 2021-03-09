package MakeUs.Moira.repository;

import MakeUs.Moira.domain.hashtag.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    public Optional<Hashtag> findHashtagByHashtagName(String hashtagName);
}
