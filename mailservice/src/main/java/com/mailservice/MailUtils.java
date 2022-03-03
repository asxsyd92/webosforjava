package com.mailservice;

import com.asxsydutils.utils.WebosResponse;

public  interface  MailUtils  {
    /**
     * 发送邮件
     *
     * @param senderAddress
     * @param senderPassword
     * @param senderAccount
     * @param recipientAddress
     * @param title
     * @param content
     * @return
     */
     WebosResponse SendMessage(String senderAddress, String senderPassword, String senderAccount, String recipientAddress, String title, String content);
}
