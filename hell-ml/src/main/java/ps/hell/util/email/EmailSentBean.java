package ps.hell.util.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * email处理类 处理为静态类
 * 
 * @author Administrator
 *
 */
public class EmailSentBean {

	public static class EmailSentThread extends Thread {

		public String[] ips;
		public String title;
		public String content;

		public EmailSentThread(String[] ips, String title, String content) {
			this.ips = ips;
			this.title = title;
			this.content = content;
		}

		public void run() {
			try {
				sendMail(title, content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void sendMail(String title, String content) throws Exception {
			Properties properties = new Properties();
			properties.put("mail.smtp.host", "smtp.163.com");
			properties.put("mail.smtp.port", "25");
			properties.put("mail.smtp.auth", "true");
			Authenticator authenticator = new EmailAuthenticator(
					"tianlie71@163.com", "woainihack1");
			javax.mail.Session session = javax.mail.Session.getDefaultInstance(
					properties, authenticator);
			Message message = createSimpleMail(session, title, content);
			// //5、发送邮件
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
		public MimeMessage createSimpleMail(Session session, String title,
				String content) throws Exception {
			// 创建邮件对象
			MimeMessage message = new MimeMessage(session);
			// 指明邮件的发件人
			message.setFrom(new InternetAddress("tianlie71@163.com"));
			// 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
			InternetAddress[] addres = new InternetAddress[ips.length];
			for (int i = 0; i < ips.length; i++) {
				addres[i] = new InternetAddress(ips[i]);
			}
			message.setRecipients(Message.RecipientType.TO, addres);
			// 邮件的标题
			message.setSubject(title, "UTF-8");
			message.setSentDate(new Date());
			// 邮件的文本内容

			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			html.setContent(content, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			message.setContent(mainPart);
			// 返回创建好的邮件对象
			return message;
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

	}

	public static void sendMail(String title, String content) throws Exception {
		System.out.println("发送邮件："+title);
		System.out.println("内容:"+content);
//		Thread thread = new Thread(new EmailSentThread(sendMails, title,
//				content));
//		thread.start();
//		thread.join();
	}

	public static String[] sendMails = new String[] { "lie.tian@fengjr.com" };

}
