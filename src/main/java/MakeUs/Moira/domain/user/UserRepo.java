package MakeUs.Moira.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    //Optional<User> findByEmail(String email);
    User findBySocialIdAndSocialProvider(String socialId, String socialProvider);

    Optional<User> findByNickname(String nickname);

    List<User> findByNicknameContaining(String keyword);
}
