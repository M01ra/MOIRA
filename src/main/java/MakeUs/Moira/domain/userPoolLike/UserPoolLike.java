package MakeUs.Moira.domain.userPoolLike;

import MakeUs.Moira.domain.userHistory.UserHistory;
import MakeUs.Moira.domain.userPool.UserPool;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class UserPoolLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @ManyToOne
    private UserPool userPool;

    private boolean isUserPoolLiked = true;

    public UserPoolLike updateUserHistory(UserHistory userHistory) {
        if (this.userHistory != null) {
            this.userHistory.getUserPoolLikes()
                            .remove(this);
        }
        this.userHistory = userHistory;
        userHistory.getUserPoolLikes()
                   .add(this);
        return this;
    }

    public UserPoolLike updateUserPool(UserPool userPool) {
        this.userPool = userPool;
        return this;
    }

    public UserPoolLike switchIsLiked() {
        this.isUserPoolLiked = !this.isUserPoolLiked;

        if (this.isUserPoolLiked) {
            this.userPool.updateLikeCount(+1);
        } else {
            this.userPool.updateLikeCount(-1);
        }
        return this;
    }
}
