package com.eightbit.biz.user.impl;

import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
@Service("gmailAuth")
public class Gmail extends Authenticator {
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("theloopholesnk@gmail.com","dbstjqdl7*!");
    }

}
