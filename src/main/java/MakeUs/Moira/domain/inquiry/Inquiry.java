package MakeUs.Moira.domain.inquiry;

import MakeUs.Moira.domain.user.UserHistory;

import javax.persistence.*;

@Entity
public class Inquiry {

    @Id
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    private String inquiryContent;

    @Enumerated(EnumType.STRING)
    private InquiryStatus inquiryStatus;
}
