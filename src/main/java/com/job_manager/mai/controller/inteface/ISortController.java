package com.job_manager.mai.controller.inteface;

import org.springframework.http.ResponseEntity;

public interface ISortController {
    ResponseEntity<?> sortByName(String name);
}
