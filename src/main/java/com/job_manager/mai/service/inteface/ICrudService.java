package com.job_manager.mai.service.inteface;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface ICrudService<Request, C extends Request, U extends Request, D extends Request, TypeId> {
    ResponseEntity<?> store(C request) throws Exception;

    ResponseEntity<?> edit(U request, TypeId id) throws Exception;

    ResponseEntity<?> destroy(TypeId id) throws Exception;

    ResponseEntity<?> destroyAll(D request) throws Exception;

    ResponseEntity<?> getAll(Pageable pageable);

    ResponseEntity<?> getById(TypeId id) throws Exception;
}
