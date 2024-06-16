package com.job_manager.mai.controller;

import com.job_manager.mai.contrains.Permission;
import com.job_manager.mai.controller.base.BaseController;
import com.job_manager.mai.controller.inteface.IBaseController;
import com.job_manager.mai.repository.AccountRepository;
import com.job_manager.mai.request.user.*;
import com.job_manager.mai.service.user.UserService;
import com.job_manager.mai.service.user.UserServiceIpm;
import com.job_manager.mai.util.ApiResponseHelper;
import com.job_manager.mai.util.SecurityHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements IBaseController<UserRequest, CreateUserRequest, UpdateUserRequest, DeleteUserRequest, String> {

    private final BaseController baseController;

    private final UserService userService;

    @PostMapping
    @Override
    public ResponseEntity<?> add(@Valid @RequestBody CreateUserRequest requestBody) {
        try {
            if (!baseController.checkIfSelf(SecurityHelper.getLoggedUser())) {
                baseController.processPermission(Permission.MANAGE_USER_CREATE);
            }
            return userService.store(requestBody);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<?> update(@RequestBody UpdateUserRequest requestBody, @PathVariable(name = "id") String Id) {
        try {
            return userService.edit(requestBody, Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable(name = "id") String Id) {
        try {
            return userService.destroy(Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping
    @Override
    public ResponseEntity<?> deleteAll(@RequestBody DeleteUserRequest request) {
        try {
            return userService.destroyAll(request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> getById(@PathVariable(name = "id") String s) {
        try {
            return userService.getById(s);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            return userService.getAll(pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override
    public ResponseEntity<?> search(@RequestParam(name = "query", defaultValue = "") String query) {
        return null;
    }

    @GetMapping("/search-by-name")
    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, @RequestParam(name = "query", defaultValue = "") String name) {
        try {
            return userService.searchByName(pageable, name);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override
    public ResponseEntity<?> sortByName(String name) {
        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/add-staff")
    public ResponseEntity<?> addStaff(@RequestBody StaffChangeRequest request) {
        try {
            return userService.addStaff(request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/remove-staff")
    public ResponseEntity<?> removeStaff(@RequestBody StaffChangeRequest request) {
        try {
            return userService.removeStaff(request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

}
