package com.job_manager.mai.response.room;

import com.job_manager.mai.model.Message;
import com.job_manager.mai.model.Room;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomResponse extends Room {

    private Message lastMessage = new Message();
    private Page<Message> allMessages;
}
