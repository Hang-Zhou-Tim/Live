package org.hang.live.common.interfaces.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/11
 * @Description
 */
public class DESUtils {

    // Algorithm Name
    public static final String KEY_ALGORITHM = "DES";
    public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";
    public static final String PUBLIC_KEY = "BAS9j2C3D4E5F60708";

    /**
     * Generate key
     *
     * @param keyStr Encrypted Keys
     * @return Encryption Object
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws Exception
     */
    private static SecretKey keyGenerator(String keyStr) throws Exception {
        byte input[] = HexString2Bytes(keyStr);
        DESKeySpec desKey = new DESKeySpec(input);
        // Create Encryption Keys
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        return securekey;
    }

    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }


    public static String encrypt(String data) {
        Key deskey = null;
        try {
            deskey = keyGenerator(PUBLIC_KEY);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            SecureRandom random = new SecureRandom();
            cipher.init(Cipher.ENCRYPT_MODE, deskey, random);
            byte[] results = cipher.doFinal(data.getBytes());
            return Base64.encodeBase64String(results);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String decrypt(String data) {
        Key deskey = null;
        try {
            deskey = keyGenerator(PUBLIC_KEY);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, deskey);
            return new String(cipher.doFinal(Base64.decodeBase64(data)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        String phone = "17889289032";
        String encryptStr = DESUtils.encrypt(phone);
        String decryStr = DESUtils.decrypt(encryptStr);
        System.out.println(encryptStr);
        System.out.println(decryStr);
    }

}