package com.job_manager.mai.service.message;

import com.job_manager.mai.request.messages.CreateMessageRequest;
import com.job_manager.mai.request.messages.DeleteMessageRequest;
import com.job_manager.mai.request.messages.MessageRequest;
import com.job_manager.mai.request.messages.UpdateMessageRequest;
import com.job_manager.mai.service.inteface.IBaseService;

public interface MessageService extends IBaseService<MessageRequest, CreateMessageRequest, UpdateMessageRequest, DeleteMessageRequest, String> {

    void readMessage(String memberId, String messageId) throws Exception;
}
