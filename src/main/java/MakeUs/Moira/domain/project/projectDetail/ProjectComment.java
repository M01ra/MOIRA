package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ProjectComment extends AuditorEntity {

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

    public ProjectComment(ProjectDetail projectDetail, User writer, String comment, ProjectComment parentComment){
        this.projectDetail = projectDetail;
        this.writer = writer;
        this.comment = comment;
        this.parentComment = parentComment;
    }
}
