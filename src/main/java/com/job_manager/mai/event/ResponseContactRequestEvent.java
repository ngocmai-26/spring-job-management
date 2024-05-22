package com.job_manager.mai.event;

import com.job_manager.mai.model.Contact;
import org.springframework.context.ApplicationEvent;

public class ResponseContactRequestEvent extends ApplicationEvent {


    private Contact contact;

    public ResponseContactRequestEvent(Object source, Contact contact) {
        super(source);
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}
