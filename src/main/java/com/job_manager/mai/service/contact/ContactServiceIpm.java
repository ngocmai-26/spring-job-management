package com.job_manager.mai.service.contact;

import com.job_manager.mai.contrains.ContactResponseCommand;
import com.job_manager.mai.contrains.ContactStatus;
import com.job_manager.mai.contrains.Messages;
import com.job_manager.mai.exception.UserNotFoundException;
import com.job_manager.mai.model.Contact;
import com.job_manager.mai.model.User;
import com.job_manager.mai.pusher.ContactPusher;
import com.job_manager.mai.repository.ContactRepository;
import com.job_manager.mai.repository.UserRepository;
import com.job_manager.mai.util.ApiResponseHelper;
import com.job_manager.mai.util.SecurityHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ContactServiceIpm implements ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;


    private final ContactPusher contactPusher;

    @Override
    public ResponseEntity<?> sendAddContactRequest(String from, String to) throws Exception {
        User owner = userRepository.findById(from).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
        User relate = userRepository.findById(to).orElseThrow(() -> new UsernameNotFoundException(Messages.INVATOR_NOT_FOUND));
        Contact contact = contactRepository.findByOwnerAndRelate(owner, relate).orElse(contactRepository.findByOwnerAndRelate(relate, owner).orElse(new Contact()));
        contact.setRelate(relate);
        contact.setOwner(owner);
        contact.setStatus(ContactStatus.WAITING_RESPONSE);
        Contact savedContact = contactRepository.save(contact);
        contactPusher.pushNewContactRequest(savedContact);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> responseContactRequest(long contactId, int command) throws Exception {
        Contact contact = contactRepository.findById(contactId).orElseThrow(() -> new UsernameNotFoundException(Messages.CONTACT_NOT_FOUND));
        if (command == ContactResponseCommand.DENIED.getValue()) {
            contact.setStatus(ContactStatus.DENIED);
        } else {
            contact.setStatus(ContactStatus.BE_FRIEND);
        }
        contactRepository.save(contact);
        contactPusher.pushContactRequestResponse(contact);
        return ApiResponseHelper.success(contact);
    }

    @Override
    public ResponseEntity<?> getAllContactByUser(Pageable pageable, String userId) throws Exception {
        return ApiResponseHelper.success(contactRepository.findAllByOwnerOrRelateAndStatus(pageable, userId, ContactStatus.BE_FRIEND));
    }

    @Override
    public ResponseEntity<?> getAllAddContactRequestByUserRelate(Pageable pageable, String userId) throws Exception {
        return ApiResponseHelper.success(contactRepository.findAllByRelateAndStatus(pageable, userRepository.findById(userId).orElse(null), ContactStatus.WAITING_RESPONSE));
    }

    @Override
    public ResponseEntity<?> getAllAddContactRequestByUserOwner(Pageable pageable, String userId) throws Exception {
        return ApiResponseHelper.success(contactRepository.findAllByOwnerAndStatus(pageable, userRepository.findById(userId).orElse(null), ContactStatus.WAITING_RESPONSE));
    }


    @Override
    public ResponseEntity<?> getContactByEmail(String email) throws Exception {
        User user = userRepository.findByEmailOrPhone(email).orElse(null);
        User owner = userRepository.findByEmailOrPhone(SecurityHelper.getLoggedUser()).orElse(null);
        Contact contact = contactRepository.findByOwnerAndRelate(owner, user).orElse(null);
        if (user == null || user.getEmail().equals(SecurityHelper.getLoggedUser()) || contact != null) {
            return ApiResponseHelper.success(new ArrayList<>());
        }
        return ApiResponseHelper.success(Collections.singletonList(user));
    }

    @Override
    public ResponseEntity<?> cancelContactRequest(Long contactId) {
        contactRepository.deleteById(contactId);
        return  ApiResponseHelper.success();
    }
}
