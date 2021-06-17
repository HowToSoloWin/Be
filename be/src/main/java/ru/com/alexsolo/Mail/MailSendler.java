package ru.com.alexsolo.Mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.com.alexsolo.domain.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Locale;

@Component
public class MailSendler {

    private ApplicationContext applicationContext;
    private JavaMailSender mailSender;
    private TemplateEngine htmlTemplateEngine;

    @Autowired
    public MailSendler(ApplicationContext applicationContext, JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
        this.applicationContext = applicationContext;
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    private final String LinkTiTicket = "http://localhost:4200/overview/history/%d";


    public void sendSimpleMessage(Context ctx, List<User> userList, String subject, String emailTemplateName)  {

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

        userList.forEach(user -> {
            try {
                message.setSubject(subject);
                message.setFrom("ev548906@gmail.com");
                message.setTo("7ojjj@wimsg.com");
                String htmlContent = this.htmlTemplateEngine.process(emailTemplateName, ctx);
                message.setText(htmlContent, true );
                this.mailSender.send(mimeMessage);
            }catch (MessagingException e){
                e.printStackTrace();
            }
        });
    }

    public Context getContext(long id){
        Context context = new Context(Locale.ENGLISH);
        context.setVariable("Link",String.format(this.LinkTiTicket,id));
        context.setVariable("id",id);
        return context;
    }

    public Context getContext(long id, String firstName, String lastName ){
        Context context = new Context(Locale.ENGLISH);
        context.setVariable("id",id);
        context.setVariable("Link",String.format(this.LinkTiTicket,id));
        context.setVariable("Lname",lastName);
        context.setVariable("Fname",firstName);
        return context;
    }
}
