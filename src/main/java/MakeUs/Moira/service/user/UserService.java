package MakeUs.Moira.service.user;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.user.dto.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;

    @Transactional
    public Long save(UserSaveRequestDto userSaveRequestDto){
        User entity = userSaveRequestDto.toEntity();
        entity.RegisterUser();
        return userRepo.save(entity).getId();
    }

}
