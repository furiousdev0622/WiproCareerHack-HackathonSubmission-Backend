package com.devtyagi.userservice.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsSesConfig {

    /**
     * This method creates a bean for AWS SES Client, the AWS Credentials are
     * acquired from the Environment Variable AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY.
     * @return AmazonSimpleEmailService client, will be used to send emails wherever required.
     */
    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .build();
    }

}
