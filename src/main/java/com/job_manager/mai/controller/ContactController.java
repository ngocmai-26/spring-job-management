package com.job_manager.mai.controller;

import com.job_manager.mai.listener.ContactListener;
import com.job_manager.mai.request.contact.AddContactRequest;
import com.job_manager.mai.request.contact.ResponseContactRequest;
import com.job_manager.mai.service.contact.ContactService;
import com.job_manager.mai.util.ApiResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;


    @PostMapping()
    public ResponseEntity<?> addContactRequest(@Valid @RequestBody AddContactRequest addContactRequest) {
        try {
            return contactService.sendAddContactRequest(addContactRequest.getFrom(), addContactRequest.getTo());
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PostMapping("/response")
    public ResponseEntity<?> responseContactRequest(@Valid @RequestBody ResponseContactRequest request) {
        try {
            return contactService.responseContactRequest(request.getContactId(), request.getCommand());
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/search/by-email/{email}")
    public ResponseEntity<?> searchContactByUserEmail(@PathVariable(name = "email") String email) {
        try {
            return contactService.getContactByEmail(email);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllContactByUser(@PathVariable(name = "userId") String userId, Pageable pageable) {
        try {
            return contactService.getAllContactByUser(pageable, userId);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/requests/{userId}")
    public ResponseEntity<?> getAllContactRequestByUser(@PathVariable(name = "userId") String userId, Pageable pageable) {
        try {
            return contactService.getAllAddContactRequestByUserOwner(pageable, userId);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/requests/related/{userId}")
    public ResponseEntity<?> getAllContactRequestByUserRelated(@PathVariable(name = "userId") String userId, Pageable pageable) {
        try {
            return contactService.getAllAddContactRequestByUserRelate(pageable, userId);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }
    @DeleteMapping("/request/delete/{contactId}")
    public ResponseEntity<?> deleteContactRequest(@PathVariable(name = "contactId") Long contactId){
        try {
            return contactService.cancelContactRequest(contactId);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

}
