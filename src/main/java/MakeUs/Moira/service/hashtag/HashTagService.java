package MakeUs.Moira.service.hashtag;

import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.controller.user.dto.myPage.HashtagResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HashTagService {

    private final HashtagRepo hashtagRepo;

    public List<HashtagResponseDto> getAllHashTag() {
        return hashtagRepo.findAll()
                          .stream()
                          .map(entity -> HashtagResponseDto.builder()
                                                           .hashtagId(entity.getId())
                                                           .hashtagName(entity.getHashtagName())
                                                           .build())
                          .collect(Collectors.toList());
    }
}
