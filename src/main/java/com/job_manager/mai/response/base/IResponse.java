package com.job_manager.mai.response.base;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Set;

public interface IResponse<S, T> {
    ModelMapper mapper = new ModelMapper();

    Collection<T> mapTo(Collection<S> source);

    T mapTo(S source);

    Page<T> mapTo(Page<S> source);
}
