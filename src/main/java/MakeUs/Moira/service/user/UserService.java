package MakeUs.Moira.service.user;

import MakeUs.Moira.advice.exception.UserException;
import MakeUs.Moira.controller.user.dto.UserSearchRequestDto;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.controller.user.dto.UserSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;

    public UserSearchRequestDto findById(Long id) {
        User entity = userRepo.findById(id).orElseThrow(UserException::new);
        return new UserSearchRequestDto(entity);
    }

    @Transactional
    public Long save(UserSaveRequestDto userSaveDto) {
        User entity = userSaveDto.toEntity();
        entity.RegisterUser();
        return userRepo.save(entity).getId();
    }
}
