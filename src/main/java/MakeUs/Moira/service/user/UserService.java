package MakeUs.Moira.service.user;


import MakeUs.Moira.advice.exception.DuplicatedNicknameException;
import MakeUs.Moira.advice.exception.InvalidUserIdException;
import MakeUs.Moira.controller.user.dto.nicknameResponseDto;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;

    public boolean isDuplicatedNickname(String nickname) {
        return userRepo.findByNickname(nickname).isPresent();
    }

    @Transactional
    public nicknameResponseDto updateNickname(String userId, String nickname) {

        if (isDuplicatedNickname(nickname)) {
            throw new DuplicatedNicknameException("중복된 닉네임");
        }

        User entity = userRepo.findById(Long.parseLong(userId))
                .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId 입니다"));

        entity.updateNickname(nickname);
        return nicknameResponseDto.builder()
                .id(entity.getId())
                .nickname(entity.getNickname())
                .build();
    }


//    @Transactional
//    public Long save(UserSaveRequestDto userSaveDto) {
//        User entity = userSaveDto.toEntity();
//        entity.RegisterUser();
//        return userRepo.save(entity).getId();
//    }
}
