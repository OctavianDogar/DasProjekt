package edu.msg.jbook.backend.model;

/**
 * Created by iacobd on 27.07.2016.
 */
public abstract class AbstractEntity<I> extends AbstractModel {
    public abstract I getId();
    public abstract void setId(I id);
}
