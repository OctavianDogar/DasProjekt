package edu.msg.jbook.backend.exception;

import javax.ejb.ApplicationException;

/**
 * Created by iacobd on 26.07.2016.
 */
@ApplicationException(rollback=true)
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -8955867151659734213L;

	public ServiceException() {
        super();
    }

	public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

	public ServiceException(String message) {
        super(message);
    }
}
