package com.example.hulkstore.utils;

import org.springframework.stereotype.Component;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Component
public class Encryptor {
    //TODO: save key in a more secure place
    private final String encryptionKey = "secret!";

    private SecretKeySpec createKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = key.getBytes("UTF-8");

        MessageDigest sha = MessageDigest.getInstance("SHA-1");

        bytes = sha.digest(bytes);
        bytes = Arrays.copyOf(bytes, 16);

        return new SecretKeySpec(bytes, "AES");
    }

    public String encrypt(String data) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = this.createKey(encryptionKey);

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] dataToEncrypt = data.getBytes("UTF-8");
        byte[] encryptedBytes = cipher.doFinal(dataToEncrypt);

        return  Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = this.createKey(encryptionKey);

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(encryptedBytes);
        return new String(decryptedData);
    }
}
