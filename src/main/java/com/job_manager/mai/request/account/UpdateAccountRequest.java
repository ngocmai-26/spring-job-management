package com.job_manager.mai.request.account;

import lombok.Data;

@Data
public class UpdateAccountRequest extends AccountRequest {
    private long roleId;
    private boolean isActive;
}
