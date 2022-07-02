package com.fip.cbt.service;

import com.fip.cbt.model.EmailType;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.Message;
import javax.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EmailServiceTest {
    @Autowired
    private EmailService emailService;
    
    @Value("${mail.store.host}")
    private String storeHost;
    
    @Value("${spring.mail.host}")
    private String smtpHost;
    
    @Value("${mail.store.protocol}")
    private String storeProtocol;
    
    @Value("${mail.store.port}")
    private Integer storePort;
    
    @Value("${spring.mail.port}")
    private Integer smtpPort;
    
    @Value("${spring.mail.username}")
    private String username;
    
    @Value("${spring.mail.password}")
    private String password;
    
    @Test
    public void testSend() throws MessagingException, IOException, FolderException {
        final ServerSetup[] setup = {
                new ServerSetup(storePort, storeHost, storeProtocol),
                new ServerSetup(smtpPort, smtpHost, "smtp")
        };
        final GreenMail greenMail = new GreenMail(setup);
        greenMail.setUser(username, password);
        greenMail.start();
        
        final String recipient = GreenMailUtil.random();
        String reminderTime = LocalDateTime.now().toString();
        String examNumber = "MATH101";
        String candidateName = "Alice Alex";
        String testOwner = "FlexiSAF";
        
        //Registration confirmation
        emailService.sendMail(recipient, username, examNumber, reminderTime,
                              candidateName, testOwner, EmailType.REGISTRATION_CONFIRMATION);
        
        assertThat(greenMail.waitForIncomingEmail(5000, 1)).isTrue();
        final Message[] messagesRegistration = greenMail.getReceivedMessages();
        
        assertThat(messagesRegistration.length).isEqualTo(1);
        assertThat(messagesRegistration[0].getContent()).isNotNull();
        greenMail.purgeEmailFromAllMailboxes();
        
        //Approval confirmation
        emailService.sendMail(recipient, username, examNumber, reminderTime,
                              candidateName, testOwner, EmailType.APPROVAL_CONFIRMATION);
        
        assertThat(greenMail.waitForIncomingEmail(5000, 1)).isTrue();
        final Message[] messagesApproval = greenMail.getReceivedMessages();
        
        assertThat(messagesApproval.length).isEqualTo(1);
        assertThat(messagesApproval[0].getContent()).isNotNull();
        greenMail.purgeEmailFromAllMailboxes();
        
        //Reminder
        emailService.sendMail(recipient, username, examNumber, reminderTime,
                              candidateName, testOwner, EmailType.EXAM_REMINDER);
        
        assertThat(greenMail.waitForIncomingEmail(5000, 1)).isTrue();
        final Message[] messagesReminder = greenMail.getReceivedMessages();
        
        assertThat(messagesReminder.length).isEqualTo(1);
        assertThat(messagesReminder[0].getContent()).isNotNull();
        
        greenMail.stop();
    }
}
