package com.job_manager.mai.request.contact;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddContactRequest {
    @NotBlank
    private String from;

    @NotBlank
    private String to;
}
