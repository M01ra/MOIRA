package MakeUs.Moira.domain.alarmHistory;

import lombok.Getter;

@Getter
public enum AlarmType {
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
    PROJECT,
    APPLY,
    APPLY_ANSWER,
    INVITE_ANSWER,
    REVIEW,
    CHATROOM
}
