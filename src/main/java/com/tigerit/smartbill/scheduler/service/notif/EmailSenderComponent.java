//package com.tigerit.smartbill.scheduler.service.notif;
//
//import com.tigerit.smartbill.common.util.Utils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.mail.javamail.MimeMessagePreparator;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.util.Properties;
//
//import static com.tigerit.smartbill.common.util.Constants.LOG_MSG;
//
//@Slf4j
//@Component
//public class EmailSenderComponent implements CommandLineRunner {
//
//    public static final String FROM_ENTITY = "Monitoring Service";
//    public static final String FROM_EMAIL = "catkingerit@gmail.com";
//    public static final String TO_EMAIL = "moyeen.uddin@tigerit.com";
//    public static final String MSG_SUBJECT = "SmartMetering Notification";
//    public static final String MSG_BODY = "Testing email Notification sending feature";
//
//
//    @Autowired
//    @Qualifier("gmail")
//    private JavaMailSender mailSender;
//
//    @Override
//    public void run(String... args) {
//        sendMail(FROM_EMAIL, MSG_SUBJECT, TO_EMAIL, "", "", MSG_BODY,null);
//    }
//
//    public void sendMail(String from, String subject, String toAddresses, String ccAddresses,
//                         String bccAddresses, String body,String attachmentFilePath) {
//        MimeMessagePreparator preparatory = mimeMessage -> {
//            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
//            message.setTo(toAddresses.split("[,;]"));
//            message.setFrom(from, FROM_ENTITY);
//            message.setSubject(subject);
//            if (StringUtils.isNotBlank(ccAddresses))
//                message.setCc(ccAddresses.split("[;,]"));
//            if (StringUtils.isNotBlank(bccAddresses))
//                message.setBcc(bccAddresses.split("[;,]"));
//            message.setText(body, false);
//
//            message.addAttachment ("",new File (attachmentFilePath));
//
//        };
//        mailSender.send(preparatory);
//        LOG_MSG = "";
//        LOG_MSG += "Email sent successfully To {},{} with Subject {}";
//        log.info(Utils.prepareLogMessage(LOG_MSG), toAddresses, ccAddresses, subject);
//    }
//    @Bean("gmail")
//    public JavaMailSender gmailMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//
//        mailSender.setUsername("catkingerit@gmail.com");
//        //mailSender.setPassword("---app password or plain password(in case turned feature on on gmail)----");
//        //mailSender.setPassword("nas1481lasd");
//        mailSender.setPassword("lklk=-09");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "false");
//
//        return mailSender;
//    }
//}
