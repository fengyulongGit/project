package com.android.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by fengyulong on 2016/7/22.
 */
public class SecurityUtils {

    private static Provider provider = new BouncyCastleProvider();

    static {
        Security.addProvider(provider);
    }

    public static String aesEncode(String data, String key) {
        try {
            return new String(Base64.encodeBase64(aes(data.getBytes("UTF-8"), key, Cipher.ENCRYPT_MODE)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String aesDecode(String dataBase64, String key) {
        if (dataBase64 == null) {
            return null;
        }
        try {
            return new String(aes(Base64.decodeBase64(dataBase64.getBytes("UTF-8")), key, Cipher.DECRYPT_MODE), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static byte[] aes(byte[] data, String key, int opMode) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES", provider); // ECB/PKCS5Padding
            cipher.init(opMode, skeySpec);

            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public final static String Md5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes("UTF-8");
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}