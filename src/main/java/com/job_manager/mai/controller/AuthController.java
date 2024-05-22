package com.job_manager.mai.controller;

import com.job_manager.mai.contrains.Messages;
import com.job_manager.mai.request.account.*;
import com.job_manager.mai.service.account.AccountService;
import com.job_manager.mai.util.ApiResponseHelper;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/public/auth")
public class AuthController {
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AccountRequest accountRequest) {
        try {
            return ApiResponseHelper.resp(accountService.login(accountRequest), HttpStatus.OK, Messages.DEFAULT_SUCCESS_MESSAGE);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PostMapping("/login-with-token")
    public ResponseEntity<?> loginWithAuthToken(@Valid @RequestBody LoginWithAuthTokenRequest accountRequest) {
        try {
            return ApiResponseHelper.resp(accountService.loginWithAuthRok(accountRequest), HttpStatus.OK, Messages.DEFAULT_SUCCESS_MESSAGE);
        } catch (JwtException e) {
            return ApiResponseHelper.signature();
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest verifyEmailRequest) {
        try {
            return accountService.verifyEmail(verifyEmailRequest.getEmail(), verifyEmailRequest.getCode());
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STAFF','ROLE_USER','ROLE_MANAGER')")
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            return accountService.changePassword(request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PostMapping("/forgot-password/request/{email}")
    public ResponseEntity<?> forgotPasswordRequest(@PathVariable(name = "email") String email) {
        try {
            return ApiResponseHelper.success();
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/request-new-code")
    public ResponseEntity<?> newVerifyCode(@RequestParam(name = "email") String email) {
        try {
            return accountService.requestNewVerifyCode(email);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            return ApiResponseHelper.resp(accountService.register(registerRequest), HttpStatus.OK, Messages.DEFAULT_SUCCESS_MESSAGE);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<?> requestForgotPasswordCode(@PathVariable(name = "email") String email) {
        try {
            return accountService.sendForgotPasswordCode(email);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/confirm-forgot-password/{email}/{code}")
    public ResponseEntity<?> confirmForgotPasswordCode(@PathVariable(name = "email") String email, @PathVariable(name = "code") String code) {
        try {
            return accountService.confirmForgotCode(email, code);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }
}
