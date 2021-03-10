package MakeUs.Moira.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findBySocialIdAndSocialProvider(int socialId, String socialProvider);
}
