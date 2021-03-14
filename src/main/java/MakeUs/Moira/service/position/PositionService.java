package MakeUs.Moira.service.position;

import MakeUs.Moira.domain.position.PositionCategory;
import MakeUs.Moira.domain.position.PositionCategoryRepo;
import MakeUs.Moira.domain.position.dto.PositionCategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PositionService {

    private final PositionCategoryRepo positionCategoryRepo;

    public List<PositionCategoryResponseDto> findAllPositionCategory() {
        return positionCategoryRepo.findAll()
                .stream()
                .map(entity -> PositionCategoryResponseDto.builder()
                        .id(entity.getId())
                        .positionCategoryName(entity.getCategoryName())
                        .positionCategoryImage(entity.getPositionCategoryImage())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
