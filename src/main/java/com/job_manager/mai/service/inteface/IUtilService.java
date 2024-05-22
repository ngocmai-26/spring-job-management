package com.job_manager.mai.service.inteface;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IUtilService {
    ResponseEntity<?> searchByName(Pageable pageable, String name) throws Exception;

    ResponseEntity<?> SearchAndSortByProperties(Pageable pageable, String searchProperties, String sortProperties) throws Exception;
}
