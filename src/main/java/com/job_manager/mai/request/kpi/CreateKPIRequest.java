package com.job_manager.mai.request.kpi;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class CreateKPIRequest extends KPIRequest {
    @NotBlank(message = "Name not null")
    private String name;

    @NotBlank(message = "Description not null")
    private String description;

    @Min(value = 0, message = "min is 0")
    private int target;

    @NotNull(message = "Type not null")
    private String kpiTypeId;

    @NotBlank(message = "Note not null")
    private String note;

    @NotBlank(message = "Comment not blank")
    private String comment;

    @NotNull(message = "Time start not null")
    private Date timeStart;

    @NotNull(message = "Time end not null")
    private Date timeEnd;
}
