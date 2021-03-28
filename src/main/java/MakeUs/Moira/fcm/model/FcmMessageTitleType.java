package MakeUs.Moira.fcm.model;

import lombok.Getter;

@Getter
public enum FcmMessageTitleType {
    /*
    1. 나한테 쪽지옴
    2. 내가 쓴 모집글에 지원함
    3. 내가 쓴 모집글에 댓글담
    4. 내 댓글에 대댓글담
    5. 내가 지원자이고 팀장한테 초대 받음
    6. 내가 지원자이고 팀장이 지원 거절함
    7. 내가 팀장이고 내가 보낸 초대에 지원자가 수락함
    8. 내가 팀장이고 내가 보낸 초대에 지원자가 거절함
    9. 나에게 리뷰가 달림
     */
    MESSAGE_RECEIVED(1),
    MY_PROJCET_APPLIED(2),
    COMMENT_TO_MY_PROJECT(3),
    COMMENT_TO_MY_COMMENT(4),
    OFFER_RECEIVED_FROM_TEAM_LEADER(5),
    REJECT_RECEIVED_FROM_TEAM_LEADER(6),
    ACCEPT_RECEIVED_FROM_APPLICANT(7),
    REJECT_RECEIVED_FROM_APPLICANT(8),
    REVIEW_RECEIVED(9);

    private final int code;

    FcmMessageTitleType(int code) {
        this.code = code;
    }
}
