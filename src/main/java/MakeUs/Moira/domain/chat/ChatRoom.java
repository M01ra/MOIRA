package MakeUs.Moira.domain.chat;

import MakeUs.Moira.domain.user.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class ChatRoom {

    @Id
    private Long id;

    @ManyToOne
    private User userA;

    @ManyToOne
    private User userB;

    @OneToMany
    private List<ChatMessage> chatMessageList;
}
