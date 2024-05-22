package com.job_manager.mai.request.kpi;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeleteKPIRequest extends KPIRequest {
    @NotNull(message = "ids not null")
    private List<String> ids = new ArrayList<>();
}
