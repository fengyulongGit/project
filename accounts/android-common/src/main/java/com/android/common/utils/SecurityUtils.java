package com.android.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具类
 * Created by fengyulong on 2016/7/22.
 */
public class SecurityUtils {

    public static final String DEFAULT_CHARSET = "utf-8";

    private static Provider provider = new BouncyCastleProvider();

    static {
        Security.addProvider(provider);
    }

    /**
     * MD5摘要
     */
    public static String md5(String str) {
        if (str == null) {
            return null;
        }

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return byte2HexStr(md5.digest(str.getBytes(DEFAULT_CHARSET)));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * SHA512摘要
     */
    public static String sha512(String str) {
        if (str == null) {
            return null;
        }

        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA-512");
            return byte2HexStr(md5.digest(str.getBytes(DEFAULT_CHARSET)));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * AES加密
     */
    public static byte[] aes128Encrypt(String string, String key, String aesIv) {
        try {
            return aes128(string.getBytes(DEFAULT_CHARSET), key, aesIv, Cipher.ENCRYPT_MODE);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * AES解密
     */
    public static String aes128Decrypt(byte[] data, String key, String aesIv) {
        try {
            return new String(aes128(data, key, aesIv, Cipher.DECRYPT_MODE), DEFAULT_CHARSET);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * BASE64加密
     */
    public static String base64Encode(byte[] data) {
        try {
            return new String(Base64.encodeBase64(data), DEFAULT_CHARSET);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * BASE64解密
     */
    public static byte[] base64Decode(String string) {
        try {
            return Base64.decodeBase64(string.getBytes(DEFAULT_CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES和新方法
     */
    private static byte[] aes128(byte[] data, String key, String aesIv, int opMode) {
        try {

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(DEFAULT_CHARSET), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", provider);
            IvParameterSpec iv = new IvParameterSpec(aesIv.getBytes(DEFAULT_CHARSET));
            cipher.init(opMode, keySpec, iv);

            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将二进制转换成16进制
     */
    public static String byte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 名字加密
     */
    public static String name(String text) {
        if (ValidateUtil.isEmpty(text)) {
            return text;
        }

        char[] chars = text.toCharArray();
        if (chars.length == 1) {
            return text;
        } else if (chars.length == 2) {
            return "*" + chars[1];
        } else if (chars.length > 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                if (chars.length - i > 1) {
                    sb.append("*");
                } else {
                    sb.append(chars[i]);
                }
            }
            return sb.toString();
        }
        return text;
    }

    /**
     * 身份证加密
     */
    public static String idcard(String text) {
        if (ValidateUtil.isEmpty(text)) {
            return text;
        }

        char[] chars = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (i < 3) {
                sb.append(chars[i]);
            } else if (chars.length - i > 3) {
                sb.append("*");
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 手机号加密
     */
    public static String mobile(String text) {
        if (ValidateUtil.isEmpty(text) || text.length() != 11) {
            return text;
        }

        char[] chars = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (i < 3) {
                sb.append(chars[i]);
            } else if (chars.length - i > 4) {
                sb.append("*");
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 银行卡加密
     */
    public static String bankcard(String text) {
        if (ValidateUtil.isEmpty(text)) {
            return text;
        }

        char[] chars = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (i < 4) {
                sb.append(chars[i]);
            } else if (chars.length - i > 4) {
                sb.append("*");
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     */
    public static byte[] hexStr2Byte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

//     public static void main(String[] args) {
//        String mess = "{'nickname':'给点饭饭儿'," +
//                "'avatar':''," +
//                "'uuid':'869095026904748'," +
//                "'platform':1," +
//                "'ts':1492509717201," +
//                "'version':'1.0'," +
//                "'token':'2a0ee5922ecb46038dd945f47cc9bfcc'," +
//                "'channel':'dev'}";
//         String s = CodecUtil.base64Encode(CodecUtil.aes128Encrypt(mess, "1111111111111111"));
//         System.out.println(URLEncoder.encode(s));
//         System.out.println(CodecUtil.md5(s + "1111111111111111"));
//
//     }
}