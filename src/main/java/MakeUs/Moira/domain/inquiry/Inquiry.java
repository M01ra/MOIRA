package MakeUs.Moira.domain.inquiry;

import MakeUs.Moira.domain.userHistory.UserHistory;

import javax.persistence.*;

@Entity 
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    private String inquiryContent;

    @Enumerated(EnumType.STRING)
    private InquiryStatus inquiryStatus;
}
