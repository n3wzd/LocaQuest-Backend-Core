package com.example.locaquest.component;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.locaquest.lib.JavaMail;

@Service
public class EmailComponent {

    private final JavaMail javaMail;
    private final SpringTemplateEngine templateEngine;

    public EmailComponent(JavaMail javaMail, SpringTemplateEngine templateEngine) {
        this.javaMail = javaMail;
        this.templateEngine = templateEngine;
    }

    public void sendAuthMail(String email, String linkUrl) {
        Context context = new Context();
        context.setVariable("linkUrl", linkUrl);

        String htmlContents = templateEngine.process("EmailVerification", context);
        javaMail.send(email, "Email Verification", htmlContents); 
    }
}
