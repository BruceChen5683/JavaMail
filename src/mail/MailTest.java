package mail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class MailTest {
    private String to,
                    from;

    private String smtpServer;

    private String username,
                    password,
                    subject,
                    content;

    private List<String> list = new ArrayList<>();

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void sendMail(){
        Properties properties = new Properties();
        properties.put("mail.smtp.host",smtpServer);
        properties.put("mail.stmp.auth","true");
        properties.put("mail.transport.protocol", "smtp");


        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });
        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            InternetAddress[] addresses = {new InternetAddress(to)};
            message.setRecipients(Message.RecipientType.TO,addresses);
            message.setSubject(subject);


            Multipart multipart = new MimeMultipart();


            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(content);
            multipart.addBodyPart(mimeBodyPart);

            //handle attachment
            for (String fileNme : list){
                MimeBodyPart mbpFile = new MimeBodyPart();
                FileDataSource fileDataSource = new FileDataSource(fileNme);
                mbpFile.setDataHandler(new DataHandler(fileDataSource));
                mbpFile.setFileName("myFileName_"+fileNme);

                multipart.addBodyPart(mbpFile);
            }

            message.setContent(multipart);
            message.setSentDate(new Date());


            Transport transport = session.getTransport();
            transport.connect(smtpServer,"chenjianliang456@126.com","123456a");
//            transport.connect("chenjianliang456@126.com","Cc123456");
            transport.send(message);

//            spring javamail 支持


        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public static void main(String[] args) {
        MailTest mail = new MailTest();
        mail.setSmtpServer("smtp.126.com");
        mail.setUsername("chenjianliang456");
        mail.setPassword("CHEN12688618592!");
//        mail.setPassword("Cc123456");
        mail.setTo("chenjianliang456@126.com");
        mail.setFrom("chenjianliang456@126.com");
        mail.setSubject("hello java mail");
        mail.setContent(" Java Mail API");

        mail.getList().add("~/run.sh");
        mail.getList().add("~/temp.txt");

        mail.sendMail();

    }
}
