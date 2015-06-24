package utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.Authenticator;  
import javax.mail.PasswordAuthentication;
import javax.mail.Session;  
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Chen Zhicheng
 * @date 2015-06-24
 * @dependency javax.mail.jar
 */
public class ZcMailSender {
	//some optional default setting, can be init with null or whatever
	private String smtpServer = "smtp.163.com";  
	private String   protocol = "smtp";  
	private String 	     from = "enieng@163.com";  
	private boolean enableSSL = false;
	private String 	  sslPort = "465";
	
	private Authenticator auth = null;
	    
    protected void setSmtpServer(String serv){
    	this.protocol = "smtp";
    	this.smtpServer = serv;
    }
    
    /**
     * @author Chen Zhicheng
     * @using default server: smtp.163.com
     */
    public static ZcMailSender getInstance(){
    	return new ZcMailSender();
    }
    
    /**
     * @author Chen Zhicheng
     * @using specified smtpServer
     */
    public static ZcMailSender getInstance(String smtpServer){
    	ZcMailSender sender = new ZcMailSender();
    	sender.setSmtpServer(smtpServer);
    	return sender;
    }
    	    
    /**
     * @author Chen Zhicheng
     * @called to enable Authentication while sending
     * @otherwise no Authentication
     */
    public ZcMailSender setAuth(String authUser,String pwd){
    	this.auth = new SimpleAuthenticator(authUser,pwd);
    	this.from = authUser;
    	return this;
    }
    
    /**
     * @author Chen Zhicheng
     * @called to use specified email account to send, call this after setAuth(...)
     * @otherwise use default email account set in setAuth(...)
     */
    public ZcMailSender setFrom(String email){
    	this.from = email;
    	return this;
    }
 
    /**
     * @author Chen Zhicheng
     * @called to enable ssl connection,if port is null, use default ssl port 465
     * 
     */
    public ZcMailSender enableSSLwith(String port){
    	this.enableSSL = true;
    	if(port!=null){
    		this.sslPort = port;
    	}
    	return this;
    }
 
    public boolean send(String subject,String content,String toEmailAddress){
    	return send(subject,content,toEmailAddress,false);
    }
    
    public boolean send(String subject,String content,String toEmailAddress,boolean debug){  
        Properties props = new Properties();  
        props.setProperty("mail.transport.protocol", protocol);
        props.setProperty("mail.smtp.auth", auth==null ? "false" : "true");  
        props.setProperty("mail.host", smtpServer);
        if(enableSSL){
	        props.setProperty("mail.smtp.ssl.enable", "true");
	        props.setProperty("mail.smtp.port", sslPort);
        }
 
        Session session = Session.getInstance(props, auth);  
        session.setDebug(debug);  
 
        try{
	        MimeMessage msg = new MimeMessage(session);  
	        msg.setFrom(new InternetAddress(from));  
	        msg.setRecipient(RecipientType.BCC, new InternetAddress(toEmailAddress));  
	        msg.setSubject(subject);  
	        msg.setSentDate(new Date());  
	        msg.setContent(content, "text/html;charset=utf-8");
	        msg.saveChanges();  	 
	        Transport.send(msg); 
	        return true;
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }
    }
    
    class SimpleAuthenticator extends Authenticator {  
        private String user;  
        private String pwd;  
     
        public SimpleAuthenticator(String user, String pwd) {  
            this.user = user;  
            this.pwd = pwd;  
        }  
     
        @Override 
        protected PasswordAuthentication getPasswordAuthentication() {  
            return new PasswordAuthentication(user, pwd);  
        }  
    } 
    
    // test
    public static void main(String[] args) throws Exception {  
		ZcMailSender.getInstance("smtp_server_address")
					.setAuth("email_address", "password")
					.enableSSLwith("sslPort_number")
					.send("test subject", "test content", "target email address",true);
    }  
}
