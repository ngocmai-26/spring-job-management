package com.job_manager.mai.util;

import com.job_manager.mai.contrains.Messages;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiResponseHelper {


    public static ResponseEntity<?> authTokenExpired() {
        List<String> msgs = new ArrayList<>();
        msgs.add(Messages.JTW_EXPIRED);
        return new ResponseEntity<>(new Response(new HashMap<>(), msgs, true, false, true), HttpStatus.FORBIDDEN);
    }

    @Data
    @AllArgsConstructor
    static class Response {
        private Object data;
        private List<String> message;
        private boolean isValid;
        private boolean tokenValid;
        private boolean isExpired;
    }

    public static ResponseEntity<?> success() {
        List<String> msgs = new ArrayList<>();
        msgs.add(Messages.DEFAULT_SUCCESS_MESSAGE);
        return new ResponseEntity<>(new Response(new HashMap<>(), msgs, true, true, false), HttpStatus.OK);
    }

    public static ResponseEntity<?> signature() {
        List<String> msgs = new ArrayList<>();
        msgs.add(Messages.SINATURE_EX);
        return new ResponseEntity<>(new Response(new HashMap<>(), msgs, false, false, false), HttpStatus.FORBIDDEN);
    }


    public static ResponseEntity<?> unAuthorized() {
        List<String> msgs = new ArrayList<>();
        msgs.add(Messages.UN_AUTHORIZED);
        return new ResponseEntity<>(new Response(new HashMap<>(), msgs, true, true, false), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<?> accessDenied() {
        List<String> msgs = new ArrayList<>();
        msgs.add(Messages.ACCESS_DEINED);
        return new ResponseEntity<>(new Response(new HashMap<>(), msgs, true, true, false), HttpStatus.FORBIDDEN);
    }


    public static ResponseEntity<?> success(Object data) {
        List<String> msgs = new ArrayList<>();
        msgs.add(Messages.DEFAULT_SUCCESS_MESSAGE);
        return new ResponseEntity<>(new Response(data, msgs, true, true, false), HttpStatus.OK);
    }

    public static ResponseEntity<?> resp(Object data, HttpStatus httpStatus, String msg) {
        List<String> msgs = new ArrayList<>();
        msgs.add(msg);
        return new ResponseEntity<>(new Response(data, msgs, true, true, false), httpStatus);
    }

    public static ResponseEntity<?> notFound(String msg) {
        List<String> msgs = new ArrayList<>();
        msgs.add(msg);
        return new ResponseEntity<>(new Response(new HashMap<>(), msgs, true, true, false), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> fallback(Exception e) {
        List<String> msgs = new ArrayList<>();
        msgs.add(e.getMessage());
        return new ResponseEntity<>(new Response(new HashMap<>(), msgs, true, true, false), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> invalid(List<String> messages) {
        return new ResponseEntity<>(new Response(new HashMap<>(), messages, false, true, false), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> invalid(Map<String, String> messages) {
        return new ResponseEntity<>(new Response(messages, new ArrayList<>(), false, true, false), HttpStatus.BAD_REQUEST);
    }
}
