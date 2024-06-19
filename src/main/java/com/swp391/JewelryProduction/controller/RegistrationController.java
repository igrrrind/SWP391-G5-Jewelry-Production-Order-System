package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.dto.UserInfoDTO;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.UserInfo;
import com.swp391.JewelryProduction.security.services.AuthenticationService;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.email.EmailService;
import com.swp391.JewelryProduction.util.Response;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registration")
public class RegistrationController {

    private final AccountService accountService;
    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(
            @Valid @RequestBody AccountDTO accountDTO,
            BindingResult bindingResult
    ) {
        log.info("/api/registration/register endpoint called: " + accountDTO);
        ResponseEntity<Response> errorMsg = getResponseError(bindingResult);
        if (errorMsg != null) return errorMsg;

        if (accountService.saveAccountIfNew(accountDTO) == null)
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Register account unsuccessfully.")
                    .response("Reason", "Account already exists")
                    .buildEntity();
        String otp = authenticationService.generateOTP(accountDTO.getEmail());
        log.info("OTP code: " + otp);
        try {
            emailService.sendOtpTextEmail(accountDTO.getEmail(), otp);
        } catch (MessagingException e) { throw new RuntimeException(e); }

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Register account successfully, please verify the account")
                .buildEntity();
    }

    @PostMapping("/verify")
    public ResponseEntity<Response> confirmRegister(
            @RequestParam("otp") String otp,
            @RequestHeader("Key") String emailKey) {
        boolean isVerified = authenticationService.verifyOTP(emailKey, otp);

        try {
            log.info(otp + ": " + isVerified);
            if (!isVerified)
                throw new RuntimeException("The OTP is wrong, please try again");
            Optional<Account> registerAcc = accountService.updateAccountStatusActive(emailKey);
        } catch (Exception e) {
            return Response.builder().status(HttpStatus.BAD_REQUEST).message(e.getMessage()).buildEntity();
        }
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Verify email account successfully")
                .buildEntity();
    }

    @RequestMapping("/resend-otp")
    public ResponseEntity<Response> resendOTP (@RequestHeader("Key") String email) {
        String otp = authenticationService.generateOTP(email);

        log.info("OTP code: " + otp);
        try {
            emailService.sendOtpTextEmail(email, otp);
        } catch (MessagingException e) { throw new RuntimeException(e); }

        return Response.builder()
                .status(HttpStatus.OK)
                .message("The OTP have been resend successfully, please check your email")
                .buildEntity();
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login (@Valid @RequestBody AccountDTO account, BindingResult bindingResult) {
        ResponseEntity<Response> errorMsg = getResponseError(bindingResult);
        if (errorMsg != null) return errorMsg;

        String jwtToken = authenticationService.authenticate(modelMapper.map(account, AccountDTO.class));
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Login successfully")
                .response("Token", jwtToken)
                .buildEntity();
    }

    @PostMapping("/user-info")
    public ResponseEntity<Response> userInfo (
            @RequestBody UserInfo info,
            @RequestHeader(name = "key") String email) {
        Account acc = accountService.saveUserInfo(info, email);
//        UserInfo info = accountService.findAccountByEmail(email).getUserInfo();
//        UserInfoDTO dto = modelMapper.map(info, UserInfoDTO.class);

        return Response.builder()
                .message("User info added successfully")
                .response("info", modelMapper.map(acc.getUserInfo(), UserInfoDTO.class))
                .buildEntity();
    }

    @PostMapping("/forget-password")
    public ResponseEntity<Response> forgetPassword (
            @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Email is invalid")
            @NotEmpty
            @RequestParam("email") String email
    ) {
        try {
            Account acc = accountService.findAccountByEmail(email);
            String otp = authenticationService.generateOTP(email);
            log.info("OTP code: " + otp);

            emailService.sendOtpTextEmail(email, otp);
        } catch (MessagingException e) { throw new RuntimeException(e); }

        return Response.builder()
                .message("Please verify your email account")
                .buildEntity();
    }

    @PostMapping("/update-password")
    public ResponseEntity<Response> updatePassword (
            @RequestBody @NotEmpty @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password is invalid") String newPassword,
            @RequestHeader("key") String email) {
        Optional<Account> updatedAcc = accountService.updateAccount(
                AccountDTO.builder()
                        .email(email)
                        .password(newPassword)
                        .build()
        );
        if (updatedAcc.isEmpty()) throw new RuntimeException();
        return Response.builder()
                .message("Password updated successfully for account with email "+email+", please log in again.")
                .buildEntity();
    }


    private ResponseEntity<Response> getResponseError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> errorMsg= new HashMap<>();
            for(FieldError err: bindingResult.getFieldErrors()){
                errorMsg.put(err.getField(), err.getDefaultMessage());
            }
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Error while processing the request")
                    .responseList(errorMsg)
                    .buildEntity();
        }
        return null;
    }
}
