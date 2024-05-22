package com.job_manager.mai.service.contact;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ContactService {
    public ResponseEntity<?> sendAddContactRequest(String from, String to) throws Exception;

    public ResponseEntity<?> responseContactRequest(long contactId, int command) throws Exception;

    public ResponseEntity<?> getAllContactByUser(Pageable pageable, String userId) throws Exception;

    public ResponseEntity<?> getAllAddContactRequestByUserRelate(Pageable pageable, String userId) throws Exception;

    public ResponseEntity<?> getAllAddContactRequestByUserOwner(Pageable pageable, String userId) throws Exception;

    ResponseEntity<?> getContactByEmail(String email) throws Exception;

    ResponseEntity<?> cancelContactRequest(Long contactId);
}
