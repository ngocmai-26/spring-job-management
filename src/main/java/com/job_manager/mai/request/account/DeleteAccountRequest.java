package com.job_manager.mai.request.account;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class DeleteAccountRequest extends AccountRequest {
    Set<String> accountIds = new HashSet<>();
}
