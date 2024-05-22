package com.job_manager.mai.pusher;

import com.job_manager.mai.event.NewContactRequestEvent;
import com.job_manager.mai.event.ResponseContactRequestEvent;
import com.job_manager.mai.model.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContactPusher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void pushNewContactRequest(Contact contact) {
        applicationEventPublisher.publishEvent(new NewContactRequestEvent(this, contact));
    }

    public void pushContactRequestResponse(Contact contact) {
        applicationEventPublisher.publishEvent(new ResponseContactRequestEvent(this, contact));
    }
}
