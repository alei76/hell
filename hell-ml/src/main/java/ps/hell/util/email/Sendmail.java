package ps.hell.util.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
* 发送一封只包含文本的简单邮件
*
*/ 
public class Sendmail {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        
//        Properties prop = new Properties();
//        prop.setProperty("mail.host", "smtp.qq.com");
//        prop.setProperty("mail.transport.protocol", "smtp");
//        prop.put("mail.smtp.port", "25");
//        prop.setProperty("mail.smtp.auth", "true");
//        //使用JavaMail发送邮件的5个步骤
//        //1、创建session
//        Session session = Session.getInstance(prop);
//        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
//        session.setDebug(true);
//        //2、通过session得到transport对象
//        Transport ts = session.getTransport();
//        //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
//        ts.connect("smtp.qq.com", "1455367000@qq.com", "woaini123!@#hack");
//        //4、创建邮件
//        Message message = createSimpleMail(session);
//        //5、发送邮件
//        ts.sendMessage(message, message.getAllRecipients());
//        ts.close();
    	
        Properties properties = new Properties();
        
        properties.put("mail.smtp.host", "smtp.163.com");
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");
        Authenticator authenticator = new EmailAuthenticator("tianlie71@163.com", "woainihack1");
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(properties, authenticator);
        
        Message message = createSimpleMail(session);
//      //5、发送邮件
        Transport.send(message);
    }
    
    /**
    * @Method: createSimpleMail
    * @Description: 创建一封只包含文本的邮件
    * @Anthor:孤傲苍狼
    *
    * @param session
    * @return
    * @throws Exception
    */ 
    public static MimeMessage createSimpleMail(Session session)
            throws Exception {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress("tianlie71@163.com"));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("lie.tian@fengjr.com"));
        //邮件的标题
        message.setSubject("只包含文本的简单邮件","UTF-8");
        message.setSentDate(new Date());
        //邮件的文本内容
        
        // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
        Multipart mainPart = new MimeMultipart();
        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart html = new MimeBodyPart();
        html.setContent("mail 测试类!", "text/html; charset=utf-8");
        mainPart.addBodyPart(html);
        message.setContent(mainPart);
        //返回创建好的邮件对象
        return message;
    }
}