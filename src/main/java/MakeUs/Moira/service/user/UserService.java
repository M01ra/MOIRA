package MakeUs.Moira.service.user;


import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;

    public boolean isDuplicatedNickname(String nickname) {
        return userRepo.findByNickname(nickname).isPresent();
    }

//    @Transactional
//    public Long save(UserSaveRequestDto userSaveDto) {
//        User entity = userSaveDto.toEntity();
//        entity.RegisterUser();
//        return userRepo.save(entity).getId();
//    }
}
