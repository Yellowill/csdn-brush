package com.hw.csdn_brush.utils;

import java.beans.Beans;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class MailUtil {


    private String emailHost = null;
    private String emailUser = null;
    private String emailPassword = null;
    private String emailAddress = null;
//    private String emailAlias = null;
           
//    private static Map<String, MailUtil> mailInstanceMap = new HashMap<String, MailUtil>();
//    
    private MailUtil(String emailHost,String emailUser,String emailPassword,String emailAddress) {
       this.emailHost = emailHost;
       this.emailUser = emailUser;
       this.emailPassword = emailPassword;
       this.emailAddress = emailAddress;
//       this.emailAlias = emailAlias;
   }

//    public static synchronized MailUtil getInstance(String mailKey) {
//        MailUtil mailInstance = mailInstanceMap.get(mailKey);
//        if (Beans.isEmpty(mailInstance)) {
//            mailInstance = new MailUtil(mailKey);
//            mailInstanceMap.put(mailKey, mailInstance);
//        }
//        logger.info(mailInstance.toString());
//        return mailInstance;
//    }
    private InternetAddress[] getAddressArr(String addrs) throws AddressException {
        if (null == addrs)
            return null;
        String[] toArr = addrs.split(";");
        InternetAddress[] toAddrs = new InternetAddress[toArr.length];
        for (int i = 0; i < toAddrs.length; i++) {
            toAddrs[i] = new InternetAddress(toArr[i]);
        }
        return toAddrs;
    }
    
    /**
     * @param to 接收者 多个以;分隔
     * @param cc 抄送者 多个以;分隔
     * @param subject 邮件主题
     * @param is 附件输入流
     * @param fileName 附件文件名
     * @param content 邮件正文内容
     * @return
     */
     public boolean sendHtmlMail(String to, String cc, String subject, String content) {
         Properties props = new Properties();
//         Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.83.224.234", 8080));
         props.put("mail.smtp.host", "smtp.qq.com"); 
         props.put("mail.smtp.auth", "false"); 
         props.put("mail.smtp.port", "465");
         props.put("socksProxyHost", "10.83.224.234");
         props.put("socksProxyPort", "8080");
         File file = null;
         try {
             Session mailSession = Session.getInstance(props);
             mailSession.setDebug(false);
             Message message = new MimeMessage(mailSession);
             message.setFrom(new InternetAddress(emailAddress));
             InternetAddress[] toAddrs = getAddressArr(to);
             InternetAddress[] ccAddrs = getAddressArr(cc);
             
             message.addRecipients(Message.RecipientType.TO, toAddrs); // 邮件接受者
             message.addRecipients(Message.RecipientType.CC, ccAddrs); // 抄送人
             message.setSubject(MimeUtility.encodeText(subject,"UTF-8","B")); // 邮件主题
             Multipart multipart = new MimeMultipart("mixed");
             //设置邮件的文本内容
             BodyPart contentPart = new MimeBodyPart();
             contentPart.setContent(content, "text/html;charset=utf-8");
             multipart.addBodyPart(contentPart);
             
             message.setContent(multipart); // 邮件内容
             message.saveChanges();
             Transport transport = mailSession.getTransport("smtp");
             transport.connect(emailHost, emailUser, emailPassword);
             transport.sendMessage(message, message.getAllRecipients());
             transport.close();
             
             return true;
       } catch (Exception e) {
    	   e.printStackTrace();
             return false;
        } finally {
             if (file != null) {
                 file.delete();
             }
        }
    }

	
	public static void main(String[] args) {
		//MailUtil(String emailHost,String emailUser,String emailPassword,String emailAddress)
        MailUtil mailUtil = new MailUtil("SMTP.QQ.COM","173520580","1234567891011.","qq.com");//Weihuang.extern@allianz.cn
        String str[] = {"Weihuang.extern@allianz.cn","173520580@qq.com"};
        for (int i = 0; i < str.length; i++) {
        	if(mailUtil.sendHtmlMail(str[i], null, "邮件测试", "邮件内容")) {
        		System.out.println(str[i] + "邮件发送成功！！！");
        	}else {
        		System.out.println(str[i] + "邮件发送失败！！！");
        	}
		}
	}
}
