package org.hspconsortium.platform.service;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;

public class EncryptionService {

    @Value("${hspc.platform.jwt.key}")
    private String password;

    public String decrypt(String encryptedText) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(password);
        return textEncryptor.decrypt(encryptedText);
    }

    public String encrypt(String text) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(password);
        return textEncryptor.encrypt(text);
    }
}
