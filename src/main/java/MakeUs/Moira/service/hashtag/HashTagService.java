package MakeUs.Moira.service.hashtag;

import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.domain.hashtag.dto.HashtagResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HashTagService {

    private final HashtagRepo hashtagRepo;

    public List<HashtagResponseDto> getAllHashTag() {
        return hashtagRepo.findAll().stream()
                .map(entity -> HashtagResponseDto.builder()
                        .id(entity.getId())
                        .hashtagName(entity.getHashtagName())
                        .build())
                .collect(Collectors.toList());
    }
}
