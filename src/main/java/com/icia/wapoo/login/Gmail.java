package com.icia.wapoo.login;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Gmail extends Authenticator
{
	@Override
	protected PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication("kkw982698@gmail.com", "rjsekavmfk1@");
	}
}
