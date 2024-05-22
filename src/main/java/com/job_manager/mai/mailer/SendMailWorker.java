package com.job_manager.mai.mailer;


public class SendMailWorker implements Runnable {

    private final JavaMail javaMail;

    String to, subject, text;

    public SendMailWorker(JavaMail javaMail, String to, String subject, String text) {
        this.to = to;
        this.javaMail = javaMail;
        this.subject = subject;
        this.text = text;
    }

    @Override
    public void run() {
        javaMail.sendMail(to, subject, text);
    }
}
