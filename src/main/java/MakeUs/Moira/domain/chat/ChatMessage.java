package MakeUs.Moira.domain.chat;

import MakeUs.Moira.domain.user.User;

import javax.persistence.*;

@Entity
public class ChatMessage {

    @Id
    private Long id;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private User sender;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private String messageContent;
}
