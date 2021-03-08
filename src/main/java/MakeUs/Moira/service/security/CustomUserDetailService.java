package MakeUs.Moira.service.security;

import MakeUs.Moira.advice.exception.UserException;
import MakeUs.Moira.domain.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    public UserDetails loadUserByUsername(String userPk) {
        return userRepo.findById(Long.valueOf(userPk)).orElseThrow(UserException::new);
    }
}
