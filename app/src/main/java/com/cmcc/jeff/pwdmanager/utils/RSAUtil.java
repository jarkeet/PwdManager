package com.cmcc.jeff.pwdmanager.utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by jeff on 2017/3/20.
 */

public class RSAUtil {

    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String getPrivateKeyFromJni();

    private static final String TAG = "RSAUtil";
//    private static final  String CIPHER_ALGRITHM = "RSA/ECB/PKCS1Padding";
    private static final  String CIPHER_ALGRITHM = "RSA/ECB/OAEPWithSHA256AndMGF1Padding";//加密算法 防重放
    private final String PUB_KEY_NAME = "pubkey.pem";
//    private final String PRIVA_KEY_NAME = "private.pem";

    private PublicKey publicKey = null;
    private PrivateKey privateKey = null;

    private static Context mContext;

    public static final RSAUtil getInstance(Context context) {
        mContext = context.getApplicationContext();
        return SingletenHolder.INSTANCE;
    }

    private static class SingletenHolder {
        private static final RSAUtil INSTANCE = new RSAUtil();
    }

    private RSAUtil(){
        try {
            if(publicKey == null) {
                publicKey = generatePublicKey(mContext.getAssets().open(PUB_KEY_NAME));
            }
            if(privateKey == null) {
                privateKey = getPrivateKey(getPrivateKeyFromJni());
            }
//            KeyPair keyPair = generateRSAKeyPair(2048);
//            publicKey = keyPair.getPublic();
//            privateKey = keyPair.getPrivate();
//
        } catch (IOException e) {
            Log.e(TAG, "load key error...");
            e.printStackTrace();
        }
        if(publicKey == null || privateKey == null) {
            Log.e(TAG, "load key is null ...");
        }
    }

    private PrivateKey generatePrivateKey(InputStream in) {

        try {
            byte[] buffPri = Base64.decode(readKey(in), Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(buffPri);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    private PrivateKey getPrivateKey(String keyFromJNi) {
        try {
            byte[] buffPri = Base64.decode(keyFromJNi, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(buffPri);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;

    }

    private PublicKey generatePublicKey(InputStream in) {
        try {
            byte[] buffPub = Base64.decode(readKey(in), Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(buffPub);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return  null;

    }

    private String readKey(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readline = null;
        StringBuffer sb = new StringBuffer();
        while((readline = br.readLine()) != null) {
            sb.append(readline);
            sb.append("\r\n");//this line can delete
        }
        Log.i("readkey ", sb.toString());
        return sb.toString();
    }


    /**
     *  java use this method to generate RSA key pair
     * @param keyLength 密钥长度不要低于512位，建议使用2048位的密钥长度。
     * @return
     */
    public  KeyPair generateRSAKeyPair(int keyLength) {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(keyLength);
            keyPair = kpg.genKeyPair();
            Log.i("public key content", Base64.encodeToString(keyPair.getPublic().getEncoded(), Base64.DEFAULT));
            Log.i("private key content", Base64.encodeToString(keyPair.getPrivate().getEncoded(), Base64.DEFAULT));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return  keyPair;
    }


    /**
     * 数据加密, base64处理
     * @param source
     * @return
     */
    public String encryptData(String source) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGRITHM);
            cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
            return  Base64.encodeToString(cipher.doFinal(source.getBytes()), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据解密
     * @param encSource
     * @return
     */
    public String decodeData(String encSource) {
        try {
            byte[] data = Base64.decode(encSource.getBytes(), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGRITHM);
            cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
            return new String(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;

    }



}
