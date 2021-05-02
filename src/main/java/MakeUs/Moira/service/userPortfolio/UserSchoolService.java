package MakeUs.Moira.service.userPortfolio;

import MakeUs.Moira.controller.userPortfolio.userSchool.dto.UserSchoolAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.UserSchoolResponseDto;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.MajorInfoResponseDto;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.SchoolInfoResponseDto;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.userSchool.*;
import MakeUs.Moira.exception.CustomException;
import MakeUs.Moira.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserSchoolService {

    private final UserRepo       userRepo;
    private final UserSchoolRepo userSchoolRepo;
    private final SchoolInfoRepo schoolInfoRepo;
    private final MajorInfoRepo  majorInfoRepo;


    public List<SchoolInfoResponseDto> searchSchoolInfo(String keyword) {
        return schoolInfoRepo.findBySchoolNameContaining(keyword)
                             .stream()
                             .map(SchoolInfoResponseDto::new)
                             .collect(Collectors.toList());
    }


    public List<MajorInfoResponseDto> searchMajorInfo(String keyword) {
        return majorInfoRepo.findByMajorNameContaining(keyword)
                            .stream()
                            .map(MajorInfoResponseDto::new)
                            .collect(Collectors.toList());
    }


    @Transactional
    public List<UserSchoolResponseDto> addUserSchool(Long userId, UserSchoolAddRequestDto userSchoolAddRequestDto) {

        User userEntity = getUserEntity(userId);
        UserPortfolio userPortfolioEntity = userEntity.getUserPortfolio();
        SchoolInfo schoolInfoEntity = getSchoolInfo(userSchoolAddRequestDto);
        MajorInfo majorInfoEntity = getMajorInfo(userSchoolAddRequestDto);

        UserSchool newUserSchoolEntity = new UserSchool(schoolInfoEntity, majorInfoEntity, userSchoolAddRequestDto);
        newUserSchoolEntity.updateUserPortfolio(userPortfolioEntity);
        userRepo.flush();


        return userEntity.getUserPortfolio()
                         .getUserSchoolList()
                         .stream()
                         .map(UserSchoolResponseDto::new)
                         .collect(Collectors.toList());
    }


    @Transactional
    public void deleteUserSchool(Long userId, Long userSchoolId) {
        UserPortfolio userPortfolioEntity = getUserEntity(userId).getUserPortfolio();
        UserSchool userSchool = userSchoolRepo.findById(userSchoolId)
                                              .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_PORTFOLIO));
        userPortfolioEntity.deleteUserSchool(userSchool);
    }

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }

    private SchoolInfo getSchoolInfo(UserSchoolAddRequestDto userSchoolAddRequestDto) {
        return schoolInfoRepo.findById(userSchoolAddRequestDto.getSchoolId())
                             .orElseThrow(() -> new CustomException(ErrorCode.INVALID_SCHOOL));
    }

    private MajorInfo getMajorInfo(UserSchoolAddRequestDto userSchoolAddRequestDto) {
        return majorInfoRepo.findById(userSchoolAddRequestDto.getMajorId())
                            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MAJOR));
    }
}
