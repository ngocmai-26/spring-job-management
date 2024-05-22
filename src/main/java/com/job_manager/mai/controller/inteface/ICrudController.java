package com.job_manager.mai.controller.inteface;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ICrudController<Request, C extends Request, U extends Request, D extends Request, TypeID> {
    ResponseEntity<?> add(C requestBody);

    ResponseEntity<?> update(U requestBody, TypeID Id);

    ResponseEntity<?> delete(TypeID Id);

    ResponseEntity<?> deleteAll(D request);

    ResponseEntity<?> getById(TypeID id);

    ResponseEntity<?> getAll(Pageable pageable);

    ResponseEntity<?> search(String query);

}
