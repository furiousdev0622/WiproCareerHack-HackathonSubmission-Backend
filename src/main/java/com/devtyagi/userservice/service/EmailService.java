package com.devtyagi.userservice.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class EmailService {

    private final String senderEmail;

    private final String applicationUrl;

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Autowired
    public EmailService(@Value("${email.sender}") String senderEmail,
                        @Value("${application.url}") String applicationUrl,
                        AmazonSimpleEmailService amazonSimpleEmailService) {
        this.senderEmail = senderEmail;
        this.applicationUrl = applicationUrl;
        this.amazonSimpleEmailService = amazonSimpleEmailService;
    }

    public void sendEmailToUser(String email,  String fullName,String url) {
        String subject = "Welcome on board, your account has been created!";
        String emailContent = "<h2>Welcome onboard!</h2>\n" +
                "<p>Click on this link to create a password and activate your account.</p>\n" +
                "<p>"+ applicationUrl + "/activate?inviteCode=" + url + "&fullName=" + URLEncoder.encode(fullName, StandardCharsets.UTF_8) + "</p>";
        try {
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(email))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(emailContent)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(subject)))
                    .withSource(senderEmail);
            amazonSimpleEmailService.sendEmail(request);
        } catch (RuntimeException e) {
            log.error("Error occurred sending email to {} ", email, e);
        }
    }
}
