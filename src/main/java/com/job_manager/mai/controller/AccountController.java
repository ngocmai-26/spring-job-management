package com.job_manager.mai.controller;

import com.job_manager.mai.controller.inteface.ICrudController;
import com.job_manager.mai.request.account.AccountRequest;
import com.job_manager.mai.request.account.AddAccountRequest;
import com.job_manager.mai.request.account.DeleteAccountRequest;
import com.job_manager.mai.request.account.UpdateAccountRequest;
import com.job_manager.mai.service.account.AccountService;
import com.job_manager.mai.util.ApiResponseHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(description = "rest api for account management", name = "Account APIs")
public class AccountController implements ICrudController<AccountRequest, AddAccountRequest, UpdateAccountRequest, DeleteAccountRequest, String> {

    private final AccountService accountService;

    @PostMapping
    @Override
    public ResponseEntity<?> add(@Valid @RequestBody AddAccountRequest requestBody) {
        try {
            return accountService.store(requestBody);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<?> update(@RequestBody UpdateAccountRequest requestBody, @PathVariable(name = "id") String Id) {
        try {
            return accountService.edit(requestBody, Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable(name = "id") String Id) {
        try {
            return accountService.destroy(Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping
    @Override
    public ResponseEntity<?> deleteAll(@RequestBody DeleteAccountRequest request) {
        try {
            return accountService.destroyAll(request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> getById(@PathVariable(name = "id") String s) {
        try {
            return accountService.getById(s);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            return accountService.getAll(pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override
    public ResponseEntity<?> search(String query) {
        return null;
    }
}
