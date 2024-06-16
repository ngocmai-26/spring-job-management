package com.job_manager.mai.service.user;

import com.job_manager.mai.contrains.Messages;
import com.job_manager.mai.controller.UserController;
import com.job_manager.mai.exception.AccountNotVerify;
import com.job_manager.mai.exception.PhoneExit;
import com.job_manager.mai.exception.UserExited;
import com.job_manager.mai.exception.UserNotFoundException;
import com.job_manager.mai.model.Account;
import com.job_manager.mai.model.User;
import com.job_manager.mai.repository.AccountRepository;
import com.job_manager.mai.repository.UserRepository;
import com.job_manager.mai.request.user.*;
import com.job_manager.mai.service.base.BaseService;
import com.job_manager.mai.util.ApiResponseHelper;
import com.job_manager.mai.util.SecurityHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceIpm extends BaseService implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;


    @Override
    public ResponseEntity<?> store(CreateUserRequest request) throws Exception {
        Account account = accountRepository.findByUsername(SecurityHelper.getLoggedUser()).orElseThrow(() -> new UserNotFoundException(Messages.USER_NOT_FOUND));
        if (account.getUser() != null) {
            throw new UserExited(Messages.USER_EXITED);
        }
        if (!account.isVerify() || !account.isActive()) {
            throw new AccountNotVerify(Messages.ACCOUNT_NOT_VERIFY);
        }
        if (request.getBirthday().after(new Date())) {
            throw new AccountNotVerify(Messages.BIRTHDAY_NOT_VALID);
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new PhoneExit(Messages.PHONE_EXIT);
        }
        User newUser = getMapper().map(request, User.class);
        newUser.setEmail(account.getUsername());
        if (request.getAvatar() == null) {
            newUser.setAvatar(com.job_manager.mai.contrains.User.DEFAULT_AVATAR);
        }
        User user = userRepository.saveAndFlush(newUser);
        account.setUser(user);

        return ApiResponseHelper.resp(user, HttpStatus.OK, Messages.DEFAULT_SUCCESS_MESSAGE);
    }

    @Override
    public ResponseEntity<?> edit(UpdateUserRequest request, String s) throws Exception {
        User user = userRepository.findById(s).orElseThrow(() -> new UserNotFoundException(Messages.USER_NOT_FOUND));
        BeanUtils.copyProperties(request, user, getNullPropertyNames(request));
        return ApiResponseHelper.success(userRepository.save(user));
    }

    @Override
    public ResponseEntity<?> destroy(String s) throws Exception {
        userRepository.deleteById(s);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> destroyAll(DeleteUserRequest request) throws Exception {
        request.ids.forEach(userRepository::deleteById);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ApiResponseHelper.success(userRepository.findAll(pageable));
    }

    @Override
    public ResponseEntity<?> getById(String s) throws Exception {
        return ApiResponseHelper.success(userRepository.findById(s).orElseThrow(() -> new UserNotFoundException(Messages.USER_NOT_FOUND)));
    }

    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, String name) throws Exception {
        return ApiResponseHelper.success(userRepository.findAllUserByEmailContaining(pageable, name));
    }

    @Override
    public ResponseEntity<?> SearchAndSortByProperties(Pageable pageable, String searchProperties, String sortProperties) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<?> addStaff(StaffChangeRequest request) throws Exception {
        User manager = userRepository.findById(request.getManagerId()).orElse(null);
        if (manager == null) {
            throw new UserNotFoundException(Messages.USER_NOT_FOUND);
        }
        for (String staffId : request.getStaffIds()) {
            User staff = userRepository.findById(staffId).orElseThrow(() -> new UserNotFoundException(Messages.USER_NOT_FOUND));
            if (!manager.getStaffs().contains(staff)) {
                manager.addStaff(staff);
            }
        }
        userRepository.save(manager);
        return ApiResponseHelper.success(manager.getStaffs());
    }

    @Override
    public ResponseEntity<?> removeStaff(StaffChangeRequest request) throws Exception {
        User manager = userRepository.findById(request.getManagerId()).orElse(null);
        if (manager == null) {
            throw new UserNotFoundException(Messages.USER_NOT_FOUND);
        }
        for (String staffId : request.getStaffIds()) {
            User staff = userRepository.findById(staffId).orElseThrow(() -> new UserNotFoundException(Messages.USER_NOT_FOUND));
            if (manager.getStaffs().contains(staff)) {
                manager.removeStaff(staff);
            }
        }
        userRepository.save(manager);
        return ApiResponseHelper.success(manager.getStaffs());
    }
}
