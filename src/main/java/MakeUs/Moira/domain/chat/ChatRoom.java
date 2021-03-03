package MakeUs.Moira.domain.chat;

import MakeUs.Moira.domain.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User userA;

    @ManyToOne
    private User userB;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessageList;
}
