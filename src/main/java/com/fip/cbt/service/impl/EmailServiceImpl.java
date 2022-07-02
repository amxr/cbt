package com.fip.cbt.service.impl;

import com.fip.cbt.model.EmailType;
import com.fip.cbt.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    JavaMailSender javaMailSender;
    
    @Autowired
    TemplateEngine templateEngine;
    
    public void sendMail(String recipient, String sender,
                         String examNumber, String reminderTime,
                         String candidateName, String testOwner,
                         EmailType emailType) {
        
        Context context = new Context();
        context.setVariable("examNumber", examNumber);
        context.setVariable("candidateName", candidateName);
        context.setVariable("reminderTime", reminderTime);
        context.setVariable("testOwner", testOwner);
        
        String content = null;
        if (emailType.equals(EmailType.REGISTRATION_CONFIRMATION)) {
            content = templateEngine.process("registrationConfirmationMail", context);
        } else if(emailType.equals(EmailType.APPROVAL_CONFIRMATION)) {
            content = templateEngine.process("approvalConfirmationMail", context);
        } else if(emailType.equals(EmailType.EXAM_REMINDER)){
            content = templateEngine.process("examReminderMail", context);
        }
        String htmlContent = content;
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(sender);
            messageHelper.setTo(recipient);
            //messageHelper.setSubject(subject);
            messageHelper.setText(htmlContent, true);
        };
        
        try{
            javaMailSender.send(messagePreparator);
        } catch(MailException e){
            System.out.println("Caught mail exception: "+e);
        }
    }
}
