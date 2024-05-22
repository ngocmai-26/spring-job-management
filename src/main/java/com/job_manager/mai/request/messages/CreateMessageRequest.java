package com.job_manager.mai.request.messages;

import com.job_manager.mai.model.Media;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class CreateMessageRequest extends MessageRequest {

    @NotBlank
    private String memberId;

    @NotBlank
    private String content;

    private Set<Media> media;

    @NotBlank
    private String roomId;
}
