package com.job_manager.mai.service.account;

import com.job_manager.mai.contrains.Messages;
import com.job_manager.mai.contrains.Roles;
import com.job_manager.mai.exception.*;
import com.job_manager.mai.model.Account;
import com.job_manager.mai.model.Role;
import com.job_manager.mai.provider.JwtProvider;
import com.job_manager.mai.provider.UCodeProvider;
import com.job_manager.mai.repository.AccountRepository;
import com.job_manager.mai.repository.RoleRepository;
import com.job_manager.mai.repository.UserRepository;
import com.job_manager.mai.request.account.*;
import com.job_manager.mai.response.account.AccountResponse;
import com.job_manager.mai.service.base.BaseService;
import com.job_manager.mai.service.inteface.ICrudService;
import com.job_manager.mai.service.util.MailService;
import com.job_manager.mai.util.ApiResponseHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService extends BaseService implements ICrudService<AccountRequest, AddAccountRequest, UpdateAccountRequest, DeleteAccountRequest, String> {
    private final MailService mailService;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UCodeProvider uCodeProvider;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    private final RoleRepository roleRepository;

    public ModelMapper getMapper() {
        return new ModelMapper();
    }

    public final long TIME_CODE_EXPIRED = (24 * 60 * 60) * 1000;

    public AccountResponse login(AccountRequest accountRequest) throws Exception {
        Account account = accountRepository.findByUsername(accountRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
        if (!passwordEncoder.matches(accountRequest.getPassword(), account.getPassword())) {
            throw new PasswordErrorException(Messages.PASSWORD_INCORRECT);
        }
        return getAccountResponse(account);
    }

    public AccountResponse loginWithAuthRok(LoginWithAuthTokenRequest accountRequest) throws Exception {
        String username = jwtProvider.extractUserName(accountRequest.getToken());
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        return getAccountResponse(account);
    }

    private AccountResponse getAccountResponse(Account account) throws AccountNotVerify, AccountNotActive {
        if (!account.isVerify()) {
            throw new AccountNotVerify(Messages.ACCOUNT_NOT_VERIFY);
        }
        if (!account.isActive()) {
            throw new AccountNotActive(Messages.ACCOUNT_NOT_ACTIVE);
        }
        AccountResponse accountResponse = getMapper().map(account, AccountResponse.class);
        accountResponse.setToken(jwtProvider.generateToken(account));
        account.setLastLoginTime(new Date(System.currentTimeMillis()));
        accountRepository.save(account);
        return accountResponse;
    }

    public ResponseEntity<?> verifyEmail(String email, String code) throws Exception {
        Account account = accountRepository.findByUsername(email).orElseThrow(() -> new UserNotFoundException(Messages.USER_NOT_FOUND));
        if (account.isVerify() || account.isActive()) {
            throw new VerifyErrorException(Messages.ACCOUNT_HAS_VERIFY);
        }
        if (Objects.equals(account.getVerifyCode(), null)) {
            throw new VerifyErrorException(Messages.VERIFY_CODE_NOT_FOUND);
        }
        if (!Objects.equals(account.getVerifyCode(), code)) {
            throw new VerifyErrorException(Messages.VERIFY_CODE_NOT_MATCH);
        }
        if (!account.getVerifyCodeExpired().after(new Date(System.currentTimeMillis()))) {
            throw new VerifyErrorException(Messages.VERIFY_CODE_EXPIRED);
        }
        account.setVerify(true);
        account.setVerifyCodeExpired(null);
        account.setVerifyCode(null);
        accountRepository.save(account);
        return ApiResponseHelper.success();
    }

    public ResponseEntity<?> requestNewVerifyCode(String email) throws Exception {
        Account account = accountRepository.findByUsername(email).orElseThrow(() -> new UserNotFoundException(Messages.USER_NOT_FOUND));
        String code = uCodeProvider.generateUCode(UCodeProvider.STANDARD_LENGTH);
        account.setVerifyCode(code);
        account.setVerifyCodeExpired(new Date(System.currentTimeMillis() + TIME_CODE_EXPIRED));
        accountRepository.save(account);
        mailService.sendVerifyMail(email, code);
        return ApiResponseHelper.success();
    }

    public AccountResponse register(RegisterRequest registerRequest) throws Exception {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new PasswordConfirmIncorrectException(Messages.PASSWORD_CONFIRM_INCORRECT);
        }
        Optional<Account> accountEx = accountRepository.findByUsername(registerRequest.getUsername());
        if (accountEx.isPresent()) {
            throw new UserExited(Messages.USER_EXITED);
        }
        Account account = getMapper().map(registerRequest, Account.class);
        account.setVerifyCode(uCodeProvider.generateUCode(UCodeProvider.STANDARD_LENGTH));
        account.setVerifyCodeExpired(new Date(System.currentTimeMillis() + TIME_CODE_EXPIRED));
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        account.setVerify(false);
        account.setActive(false);
        account.setLastLoginTime(new Date(System.currentTimeMillis()));
        Role role = roleRepository.findByRoleName(String.valueOf(Roles.ROLE_STAFF)).orElseThrow();
        account.setRole(role);
        accountRepository.save(account);
        AccountResponse accountResponse = getMapper().map(account, AccountResponse.class);
        accountResponse.setToken(jwtProvider.generateToken(account));
        mailService.sendVerifyMail(account.getUsername(), account.getVerifyCode());
        return accountResponse;
    }

    public ResponseEntity<?> sendForgotPasswordCode(String email) {
        Account account = accountRepository.findByUsername(email).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
        String code = uCodeProvider.generateUCode();
        account.setVerifyCode(code);
        accountRepository.save(account);
        mailService.sendVerifyMail(account.getUsername(), code);
        return ApiResponseHelper.success();
    }

    public ResponseEntity<?> confirmForgotCode(String email, String code) throws Exception {
        Account account = accountRepository.findByUsername(email).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
        if (!Objects.equals(account.getVerifyCode(), code)) {
            throw new VerifyErrorException(Messages.VERIFY_CODE_NOT_MATCH);
        }
        String newPassword = uCodeProvider.generateUCode(UCodeProvider.STANDARD_LENGTH);
        account.setPassword(passwordEncoder.encode(newPassword));
        account.setVerifyCode(null);
        accountRepository.save(account);
        mailService.sendForgotPasswordMail(account.getUsername(), newPassword);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> store(AddAccountRequest request) throws Exception {
        Account account = new Account();
        BeanUtils.copyProperties(request, account, getNullPropertyNames(request));
        account.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        // check email exits
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new Exception("Username has taken");
        }
        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(() -> new UserNotFoundException(Messages.ROLE_NOT_FOUND));
        account.setRole(role);
        account.setActive(true);
        account.setVerify(true);
        return ApiResponseHelper.success(accountRepository.save(account));
    }

    @Override
    public ResponseEntity<?> edit(UpdateAccountRequest request, String s) throws Exception {
        Account account = accountRepository.findById(s).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
        BeanUtils.copyProperties(request, account, getNullPropertyNames(request));
        Role role = roleRepository.findById(request.getRoleId()).orElse(null);
        if (role != null) {
            account.setRole(role);
        }
        return ApiResponseHelper.success(accountRepository.save(account));
    }

    @Override
    public ResponseEntity<?> destroy(String s) throws Exception {
        Account account = accountRepository.findById(s).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
        account.setActive(false);
        accountRepository.save(account);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> destroyAll(DeleteAccountRequest request) throws Exception {
        List<Account> accounts = accountRepository.findAllById(request.getAccountIds());
        accounts.forEach((r) -> {
            r.setActive(false);
            accountRepository.save(r);
        });
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ApiResponseHelper.success(accountRepository.findAll(pageable));
    }

    @Override
    public ResponseEntity<?> getById(String s) throws Exception {
        return ApiResponseHelper.success(accountRepository.findById(s).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND)));
    }

    public ResponseEntity<?> changePassword(ChangePasswordRequest request) {
        Account account = accountRepository.findByUsername(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
        if (!new BCryptPasswordEncoder().matches(request.getOldPassword(), account.getPassword())) {
            return ApiResponseHelper.fallback(new PasswordErrorException("Old password incorrect"));
        }
        if (request.getOldPassword().equals(request.getNewPassword())) {
            return ApiResponseHelper.fallback(new PasswordErrorException("New password is same old password"));
        }
        account.setPassword(new BCryptPasswordEncoder().encode(request.getNewPassword()));
        accountRepository.save(account);
        return ApiResponseHelper.success();
    }
}