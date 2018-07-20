package edu.msg.jbook.backend.assembler;

import edu.msg.jbook.common.exceptions.CommonException;

/**
 * Created by cioncag on 28.07.2016.
 */
public interface Assembler<M, D> {

    M dtoToModel(D dto) throws CommonException;
    D modelToDto(M model) throws CommonException;
}
