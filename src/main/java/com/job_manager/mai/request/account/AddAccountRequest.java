package com.job_manager.mai.request.account;

import lombok.Data;

@Data
public class AddAccountRequest extends AccountRequest {
    long roleId;
}
