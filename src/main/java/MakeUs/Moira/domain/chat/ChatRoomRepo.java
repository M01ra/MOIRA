package MakeUs.Moira.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ChatRoomRepo extends JpaRepository<ChatRoom, Long> {
    @Query("select chatroom " +
            "from ChatRoom chatroom " +
            "where " + "chatroom.userA.id=:userId or chatroom.userB.id=:userId")
    List<ChatRoom> findAllChatRoom(@Param("userId") Long userId);

    @Query("select chatroom " +
            "from ChatRoom chatroom " +
            "where " + "(chatroom.userA.id=:userId and chatroom.userB.id=:opponentId)" + " or " + "(chatroom.userA.id=:opponentId and chatroom.userB.id=:userId)")
    ChatRoom findChatRoom(@Param("userId") Long userId,
                          @Param("opponentId") Long opponentId);
}
