package MakeUs.Moira.domain.userPool;

import MakeUs.Moira.controller.userPool.dto.UserPoolOnOffResponseDto;
import MakeUs.Moira.domain.AuditorEntity;

import MakeUs.Moira.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class UserPool extends AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "userPool")
    private User user;

//    @OneToMany(mappedBy = "userPool", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<UserPoolOptionalInfo> userPoolOptionalInfoList = new ArrayList<>();

    @OneToMany(mappedBy = "userPool", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPoolOffer> userPoolOfferList = new ArrayList<>();

    private int likeCount = 0;

    private int hitCount = 0;

    private boolean isVisible = false;

    public UserPool updateUser(User user) {
        this.user = user;
        return this;
    }

    public UserPool updateLikeCount(int value) {
        likeCount += value;
        return this;
    }

    public UserPool updateHitCount() {
        this.hitCount += 1;
        return this;
    }

    public void switchVisible() {
        this.isVisible = !this.isVisible;
    }

    public boolean isDesiredPositionCategory(String positionCategoryName) {
        return this.user.getUserPosition()
                        .getPositionCategory()
                        .getCategoryName()
                        .equals(positionCategoryName);
    }

    public UserPoolOnOffResponseDto toUserPoolOnOffResponseDto(){
        return UserPoolOnOffResponseDto.builder()
                                       .userId(getUser().getId())
                                       .userPoolId(id)
                                       .isVisible(isVisible)
                                       .build();
    }
}
