package com.job_manager.mai.controller.inteface;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ISearchController {
    ResponseEntity<?> searchByName(Pageable pageable,String name);
}
