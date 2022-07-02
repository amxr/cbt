package com.fip.cbt.service;

import com.fip.cbt.model.EmailType;

public interface EmailService {
    void sendMail(String recipient, String sender, String examNumber, String reminderTime,
                  String candidateName, String testOwner, EmailType emailType);
}
