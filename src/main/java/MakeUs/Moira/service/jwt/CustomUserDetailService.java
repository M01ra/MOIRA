package MakeUs.Moira.service.jwt;

import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.exception.CustomException;
import MakeUs.Moira.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
        return userRepo.findById(Long.valueOf(userPk)).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }
}
