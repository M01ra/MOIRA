package MakeUs.Moira.domain.chat;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface ChatMessageRepo extends JpaRepository<ChatMessage, Long>{
    List<ChatMessage> findAllByChatRoom_IdOrderByCreatedDate(Long chatRoomId, Pageable pageable);
}
