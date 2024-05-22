package com.job_manager.mai.event;

import com.job_manager.mai.model.Contact;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

public class NewContactRequestEvent extends ApplicationEvent {

    private Contact contact;

    public NewContactRequestEvent(Object source, Contact contact) {
        super(source);
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}
