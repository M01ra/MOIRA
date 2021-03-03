package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.user.User;

import javax.persistence.*;

@Entity
public class ProjectComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProjectDetail projectDetail;

    @ManyToOne
    private User writer;

    private String comment;

    // 셀프 양방향 관계
    @ManyToOne
    private ProjectComment parentComment;
}
