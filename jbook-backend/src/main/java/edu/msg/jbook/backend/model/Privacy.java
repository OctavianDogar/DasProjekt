package edu.msg.jbook.backend.model;

/**
 * Created by dogaro on 27/07/2016.
 */
public enum Privacy {

    ONLY_ME, FRIENDS, FRIENDS_OF_FRIENDS, PUBLIC;

    public static Privacy toPrivacy(String type) {
        switch (type) {
            case "ONLY_ME":
                return Privacy.ONLY_ME;
            case "FRIENDS":
                return Privacy.FRIENDS;
            case "FRIENDS_OF_FRIENDS":
                return Privacy.FRIENDS_OF_FRIENDS;
            case "PUBLIC":
                return Privacy.PUBLIC;
            default:
                return Privacy.PUBLIC;
        }
    }

    }
