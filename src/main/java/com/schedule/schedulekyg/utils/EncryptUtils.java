package com.schedule.schedulekyg.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES-256 암호화 유틸
 * {@link //http://wiki-digit.amorepacific.com:8090/pages/viewpage.action?pageId=79402104}
 * @author Amorepacific
 *
 */
public class EncryptUtils {
	 
    private static String SECRET_KEY = "9E05F51DD1C94C0FA91DDCB7822AF230";
 
    public static String encrypt(String source) {
        try {
            return new String(Base64.encodeBase64(encrypt(source.getBytes(), SECRET_KEY.getBytes())), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
 
    public static String decrypt(String source) {
        try {
            return new String(decrypt(Base64.decodeBase64(source.getBytes()), SECRET_KEY.getBytes()), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
 
    private static byte[] encrypt(byte[] source, byte[] key) {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
        SecretKeySpec newKey = new SecretKeySpec(key, "AES");
 
        try {
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
 
            return cipher.doFinal(source);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
 
    private static byte[] decrypt(byte[] source, byte[] key) {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
        SecretKeySpec newKey = new SecretKeySpec(key, "AES");
 
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return cipher.doFinal(source);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
