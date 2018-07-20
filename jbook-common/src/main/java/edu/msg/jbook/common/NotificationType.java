package edu.msg.jbook.common;

/**
 * Created by iacobd on 27.07.2016.
 */
public enum NotificationType {
    COMMENT_OR_LIKE, POST, FRIEND_ACCEPT, BIRTHDAY, EVENT_IN_A_DAY, EVENT_MODIFIED;

    public static NotificationType toNotificationType(String type) {
        switch (type) {
            case "COMMENT_OR_LIKE":
                return NotificationType.COMMENT_OR_LIKE;
            case "POST":
                return NotificationType.POST;
            case "FRIEND_ACCEPT":
                return NotificationType.FRIEND_ACCEPT;
            case "BIRTHDAY":
                return NotificationType.BIRTHDAY;
            case "EVENT_IN_A_DAY":
                return NotificationType.EVENT_IN_A_DAY;
            case "EVENT_MODIFIED":
                return NotificationType.EVENT_MODIFIED;
            default:
                return NotificationType.POST;
        }
    }
}
