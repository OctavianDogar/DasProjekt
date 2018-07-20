package edu.msg.jbook.backend.repository;

import edu.msg.jbook.backend.exception.RepositoryException;
import edu.msg.jbook.backend.model.AbstractModel;

import java.util.List;

/**
 * Created by ilyesk on 26.07.2016.
 */
public interface BaseRepository<T extends AbstractModel, I> {

    List<T> getAll() throws RepositoryException;

    T getById(I id) throws RepositoryException;

    T save(T item) throws RepositoryException;

    T merge(T item) throws RepositoryException;

    void delete(T item) throws RepositoryException;
}
