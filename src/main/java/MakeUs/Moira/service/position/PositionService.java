package MakeUs.Moira.service.position;

import MakeUs.Moira.domain.position.PositionCategoryRepo;
import MakeUs.Moira.domain.position.PositionRepo;
import MakeUs.Moira.controller.user.dto.signup.PositionCategoryResponseDto;
import MakeUs.Moira.controller.user.dto.signup.PositionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PositionService {

    private final PositionCategoryRepo positionCategoryRepo;
    private final PositionRepo positionRepo;


    public List<PositionCategoryResponseDto> findAllPositionCategory() {
        return positionCategoryRepo.findAll()
                .stream()
                .map(entity -> PositionCategoryResponseDto.builder()
                        .id(entity.getId())
                        .positionCategoryName(entity.getCategoryName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<PositionResponseDto> findAllPosition(Long positionCategoryId) {
        return positionRepo.findAllByPositionCategory_Id(positionCategoryId).stream()
                .map(entity -> PositionResponseDto.builder()
                        .positionId(entity.getId())
                        .positionName(entity.getPositionName())
                        .build())
                .collect(Collectors.toList());
    }
}
