package com.job_manager.mai.request.base;

import lombok.Data;

import java.util.Set;

@Data
public class DeleteRequest<T> {
    Set<T> Ids;
}
