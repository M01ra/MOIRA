package MakeUs.Moira.controller.user;

import MakeUs.Moira.advice.exception.UserException;
import MakeUs.Moira.domain.user.dto.UserSaveRequestDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController{

    private final UserService userService;
    private final ResponseService responseService;

    @PostMapping(value = "/user")
    public SingleResult<Long> save(@RequestBody UserSaveRequestDto userSaveRequestDto) throws UserException {
//        throw new UserException("UserException");
        Long savedId = userService.save(userSaveRequestDto);
        return responseService.mappingSingleResult(savedId, "사용자 저장 성공");
    }

}
