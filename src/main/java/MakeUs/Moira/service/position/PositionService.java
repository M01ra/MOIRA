package MakeUs.Moira.service.position;

import MakeUs.Moira.domain.position.PositionCategoryRepo;
import MakeUs.Moira.domain.position.PositionRepo;
import MakeUs.Moira.controller.user.dto.PositionCategoryResponseDto;
import MakeUs.Moira.controller.user.dto.PositionResponseDto;
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
                        .positionCategoryImage(entity.getPositionCategoryImage())
                        .build())
                .collect(Collectors.toList());
    }

    public List<PositionResponseDto> findAllPosition(Long positionCategoryId) {
        return positionRepo.findAllByPositionCategory_Id(positionCategoryId).stream()
                .map(entity -> PositionResponseDto.builder()
                        .id(entity.getId())
                        .positionName(entity.getPositionName())
                        .build())
                .collect(Collectors.toList());
    }
}
