package com.watson.monitor.utils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.watson.monitor.utils.TaskUtil.loadConfig;

public class EmailUtil {

    //发送邮件的邮箱
    public static String FROM_NAME;
    public static String FROM_MAIL;
    //授权密码,不是登录密码,授权密码是给第三方客户端授权发送邮件的授权码,
    //这个东西是在163服务器获取的,有了这东西才能发送邮件到163服务器,其他邮箱也类似
    public static String PWD_CODE;
    public static String MAIL_HOST;
//    public static final String MAIL_SMTP_AUTH = "true";

    static {
        Properties properties = loadConfig("mail");

        MAIL_HOST = properties.getProperty("mail_host");
        FROM_NAME = properties.getProperty("from_name");
        FROM_MAIL = properties.getProperty("from_mail");
        PWD_CODE = properties.getProperty("pwd_code");
    }

    /**
     * 发送纯文本邮件
     *
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param toMail  收件邮箱
     *                某些邮箱服务器为了增加邮箱本身密码的安全性，给SMTP客户端设置了独立密码（有的邮箱称为“授权码”），
     *                对于开启了独立密码的邮箱，这里的邮箱密码必需使用这个独立密码（授权码）。
     * @return
     * @throws Exception
     */
    public static void send(String subject, String content,
                            String toMail) throws Exception {
        Properties p = new Properties();
        p.setProperty("mail.transport.protocol", "smtp");// 使用的协议
        p.setProperty("mail.smtp.host", MAIL_HOST);
        p.setProperty("mail.smtp.localhost", "localhost");// 不加的话在linux报错：501 syntax:ehlo hostname
        p.setProperty("mail.smtp.auth", "true");// 需要请求认证

        Session session = Session.getInstance(p);
        //设置为debug模式，可以查看详细的发送log
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
        //发件人
        message.setFrom(new InternetAddress(FROM_MAIL, FROM_NAME, "UTF-8"));

        //收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toMail, toMail, "UTF-8"));

        //邮件主题
        message.setSubject(subject, "UTF-8");

        //邮件正文
        message.setContent(content, "text/html;charset=UTF-8");

        //设置显示的发件时间
        message.setSentDate(new Date());

        //保存前面的设置
        message.saveChanges();

        //根据session获取邮件传输对象
        Transport transport = session.getTransport();
        //连接邮箱服务器
        transport.connect(FROM_MAIL, PWD_CODE);
        //发送邮件到所有的收件地址
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    //和上边方法基本相同，只不过可以添加附件发送邮件
    public static void send(String subject, String content,
                            List<String> receiveMailList) throws Exception {
        Properties p = new Properties();
        p.setProperty("mail.transport.protocol", "smtp");
        p.setProperty("mail.smtp.host", MAIL_HOST);
        //p.setProperty("mail.smtp.localhost", "localhost");//不加的话在linux报错：501 syntax:ehlo hostname
        p.setProperty("mail.smtp.auth", "true");

        Session session = Session.getInstance(p);
        //设置为debug模式，可以查看详细的发送log
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
        //发件人
        message.setFrom(new InternetAddress(FROM_MAIL, FROM_NAME, "UTF-8"));

        //收件人
        for (String receiveAddress : receiveMailList) {
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveAddress, receiveAddress, "UTF-8"));
        }

        //邮件主题
        message.setSubject(subject, "UTF-8");

        //向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
        MimeMultipart multipart = new MimeMultipart();
        //设置邮件的文本内容
        MimeBodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(content, "text/html;charset=UTF-8");
        multipart.addBodyPart(contentPart);
        //添加附件
        MimeBodyPart filePart = new MimeBodyPart();
        DataSource source = new FileDataSource("E:\\file_path" + File.separator + "附件.xlsx");
        //添加附件的内容
        filePart.setDataHandler(new DataHandler(source));
        //添加附件的标题
        filePart.setFileName(MimeUtility.encodeText("附件.xlsx"));
        multipart.addBodyPart(filePart);
        multipart.setSubType("mixed");
        //将multipart对象放到message中
        message.setContent(multipart);

        //设置显示的发件时间
        message.setSentDate(new Date());

        //保存前面的设置
        message.saveChanges();

        //根据session获取邮件传输对象
        Transport transport = session.getTransport();
        //连接邮箱服务器
        transport.connect(FROM_MAIL, PWD_CODE);
        //发送邮件到所有的收件地址
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public static void main(String[] args) {
        String recipient = "1603441905@qq.com";
        String subject = "设备告警";
        String content = "UPS 电压异常";

        try {
            send(subject, content, recipient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
