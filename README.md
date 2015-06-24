# ZcMailSender
An easy-to-use java e-mail sender, built on top of javamail.

# Usage

<pre><code>
ZcMailSender.getInstance("smtp_server_address")
					  .setAuth("email_address", "password")
					  .setFrom("from_email_address")
					  .enableSSLwith("sslPort_number")
					  .send("test subject", "test content", "target email address",true);
</code></pre>
