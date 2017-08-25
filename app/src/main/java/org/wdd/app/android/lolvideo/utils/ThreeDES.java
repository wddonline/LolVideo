package org.wdd.app.android.lolvideo.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by richard on 6/5/17.
 */

public class ThreeDES {

    private String strKey = String.valueOf(new char[]{'L', 'S', 'X', 'I', 'o', 'N', 'F', 'K'});//密钥
    private final String TRANSFORMATION = "DES/CBC/PKCS5Padding";
    private final byte[] iv = {1,2,3,4,5,6,7,8};

    /**
     * 加密String明文输入,String密文输出
     * @param strMing String明文
     * @return String密文
     */
    public String getEncString(String strMing) throws UnsupportedEncodingException {
        byte[] byteMing = strMing.getBytes("UTF8");
        byte[] byteMi = getEncCode(byteMing);
        String strMi = Base64.encodeToString(byteMi, Base64.URL_SAFE | Base64.NO_WRAP);
        return strMi;
    }

    /**
     * 解密 以String密文输入,String明文输出
     * @param strMi String密文
     * @return String明文
     */
    public String getDesString(String strMi) throws UnsupportedEncodingException {
        byte[] byteMi = Base64.decode(strMi, Base64.URL_SAFE | Base64.NO_WRAP);
        byte[] byteMing = getDesCode(byteMi);
        String strMing = new String(byteMing, "UTF8");
        return strMing;
    }

    /**
     * 加密以byte[]明文输入,byte[]密文输出
     * @param byteS byte[]明文
     * @return byte[]密文
     */
    private byte[] getEncCode(byte[] byteS) {
        byte[] byteFina = null;
        try {
            DESKeySpec dks = new DESKeySpec(strKey.getBytes());
            Key secretKey = SecretKeyFactory.getInstance("DES").generateSecret(dks);
            IvParameterSpec paramSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }

    /**
     * 解密以byte[]密文输入,以byte[]明文输出
     * @param byteD byte[]密文
     * @return byte[]明文
     */
    private byte[] getDesCode(byte[] byteD) {
        byte[] byteFina = null;
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key = new SecretKeySpec(strKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }

}
