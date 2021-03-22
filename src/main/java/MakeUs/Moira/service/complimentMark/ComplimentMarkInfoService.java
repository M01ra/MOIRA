package MakeUs.Moira.service.complimentMark;

import MakeUs.Moira.controller.complimentMark.dto.ComplimentMarkInfoResponseDto;
import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ComplimentMarkInfoService {

    private final ComplimentMarkInfoRepo complimentMarkInfoRepo;

    public List<ComplimentMarkInfoResponseDto> getComplimentMark() {
        return complimentMarkInfoRepo.findAll()
                .stream()
                .map(ComplimentMarkInfoResponseDto::new)
                .collect(Collectors.toList());
    }
}
