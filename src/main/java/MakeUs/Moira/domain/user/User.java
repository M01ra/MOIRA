package MakeUs.Moira.domain.user;

import MakeUs.Moira.controller.security.dto.LoginResponseDto;
import MakeUs.Moira.controller.user.dto.myPageEdit.MyPageEditIntroductionResponseDto;
import MakeUs.Moira.controller.user.dto.myPageEdit.MyPageEditNicknameResponseDto;
import MakeUs.Moira.controller.user.dto.myPageEdit.MyPageEditPositionResponseDto;
import MakeUs.Moira.domain.position.UserPosition;
import MakeUs.Moira.domain.userPool.UserPool;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Entity
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private UserHistory userHistory;

    @OneToOne(cascade = CascadeType.ALL)
    private UserPortfolio userPortfolio;

    @OneToOne(cascade = CascadeType.ALL)
    private UserPool userPool;

    private String socialProvider;

    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    private String email;

    private String shortIntroduction;

    private String nickname;

    private String profileImage;

    @ManyToOne
    private UserPosition userPosition;

    @Builder
    public User(String socialProvider, String socialId, UserRole userRole) {
        this.socialProvider = socialProvider;
        this.socialId = socialId;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(this.userRole.getKey());
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(simpleGrantedAuthority);
        return simpleGrantedAuthorities;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.id.toString();
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getPassword() {
        return null;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User updateUserHistory(UserHistory userHistory) {
        this.userHistory = userHistory;
        userHistory.updateUser(this);
        return this;
    }

    public User updateUserPortfolio(UserPortfolio userPortfolio) {
        this.userPortfolio = userPortfolio;
        userPortfolio.updateUser(this);
        return this;
    }

    public User updateUserPool(UserPool userPool) {
        this.userPool = userPool;
        userPool.updateUser(this);
        return this;
    }


    public User updateNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public User updateUserPosition(UserPosition userPosition) {
        this.userPosition = userPosition;
        return this;
    }

    public User updateShorIntroduction(String shortIntroduction) {
        this.shortIntroduction = shortIntroduction;
        return this;
    }

    public MyPageEditNicknameResponseDto toMyPageEditNicknameResponseDto() {
        return MyPageEditNicknameResponseDto.builder()
                                            .userId(id)
                                            .nickname(nickname)
                                            .build();
    }

    public MyPageEditPositionResponseDto toMyPageEditPositionResponseDto() {
        return MyPageEditPositionResponseDto.builder()
                                            .userId(id)
                                            .positionId(userPosition.getId())
                                            .positionName(userPosition.getPositionName())
                                            .build();
    }

    public MyPageEditIntroductionResponseDto toMyPageEditIntroductionResponseDto() {
        return MyPageEditIntroductionResponseDto.builder()
                                                .userId(id)
                                                .shortIntroduction(getShortIntroduction())
                                                .build();
    }
}
