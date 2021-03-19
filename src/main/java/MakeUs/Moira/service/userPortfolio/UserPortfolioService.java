package MakeUs.Moira.service.userPortfolio;

import MakeUs.Moira.controller.userPortfolio.dto.MajorInfoResponseDto;
import MakeUs.Moira.controller.userPortfolio.dto.SchoolInfoResponseDto;
import MakeUs.Moira.domain.userPortfolio.userSchool.MajorInfoRepo;
import MakeUs.Moira.domain.userPortfolio.userSchool.SchoolInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserPortfolioService {

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
}
