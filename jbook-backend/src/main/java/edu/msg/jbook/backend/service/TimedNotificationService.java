package edu.msg.jbook.backend.service;

import edu.msg.jbook.backend.exception.ServiceException;

/**
 * Created by ilyesk on 09.08.2016.
 */
public interface TimedNotificationService {

    void addBirthDayNotification() throws ServiceException;
    void addDayBeforeEventNotification() throws ServiceException;
}
