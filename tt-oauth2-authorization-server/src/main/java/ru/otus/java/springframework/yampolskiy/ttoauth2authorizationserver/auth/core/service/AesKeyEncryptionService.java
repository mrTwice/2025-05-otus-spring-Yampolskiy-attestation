package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Service
// TODO @Profile("local")
@Primary
public class AesKeyEncryptionService implements KeyEncryptionService {

    private static final String ALGORITHM = "AES";

    private final SecretKeySpec secretKeySpec;

    //TODO  Через переменные окружения (ENV) export JWK_ENCRYPTION_SECRET=your-super-secret
    // || Spring Cloud Vault / AWS Secrets Manager / GCP Secret Manager
    public AesKeyEncryptionService(@Value("${jwk.encryption.secret}") String secret) {
        byte[] key = Arrays.copyOf(secret.getBytes(StandardCharsets.UTF_8), 32); // AES-256
        this.secretKeySpec = new SecretKeySpec(key, ALGORITHM);
    }

    @Override
    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка шифрования", e);
        }
    }

    @Override
    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка дешифровки", e);
        }
    }
}