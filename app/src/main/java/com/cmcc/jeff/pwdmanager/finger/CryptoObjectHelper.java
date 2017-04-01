package com.cmcc.jeff.pwdmanager.finger;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by jeff on 2017/3/31.
 */

public class CryptoObjectHelper {

    static final String KEY_NAME = "com.cmcc.jeff.pwdmanager";
    static final String KEYSTORE_NAME = "AndroidKeyStore";

    static final String KEY_ALGRITHM = KeyProperties.KEY_ALGORITHM_AES;
    static final String BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC;
    static final String ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7;
    static final String TRANSFORMATION = KEY_ALGRITHM + "/" + BLOCK_MODE + "/" + ENCRYPTION_PADDING;

    private KeyStore keyStore;

    public CryptoObjectHelper() {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_NAME);
            keyStore.load(null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get CryptoObject for encrypt and decode, but it is not used in this demo
     *
     * @param purpose
     * @param IV
     * @return
     */
    public FingerprintManager.CryptoObject buildCryptoObject(int purpose, byte[] IV) {
        try {
            final Key key = getKey();
            if (key == null) {
                return null;
            }
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            if (purpose == KeyProperties.PURPOSE_ENCRYPT) {
                cipher.init(purpose, key);
            } else {
                cipher.init(purpose, key, new IvParameterSpec(IV));
            }
            return new FingerprintManager.CryptoObject(cipher);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * return CryptoObject with cipher，this case just use fingerprint for authn but not use key to encrypt or decode
     *
     * @return
     */
    public FingerprintManager.CryptoObject buildCryptoObject() {
        try {
            final Key key = getKey();
            if (key == null) {
                return null;
            }
            // Set up the crypto object. The object will be authenticated by use of the fingerprint.
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(KeyProperties.PURPOSE_ENCRYPT, key);
            return new FingerprintManager.CryptoObject(cipher);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Key getKey() throws Exception {
        Key secretKey;
        if (!keyStore.isKeyEntry(KEY_NAME)) {
            generateKey(false);
        }

        secretKey = keyStore.getKey(KEY_NAME, null);
        return secretKey;
    }

    private void generateKey(boolean invalidatedByBiometricEnrollment) {

        try {
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGRITHM, KEYSTORE_NAME);
            int purposes = KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT;
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(KEY_NAME, purposes)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            // This is a workaround to avoid crashes on devices whose API level is < 24
            // because KeyGenParameterSpec.Builder#setInvalidatedByBiometricEnrollment is only
            // visible on API level +24.
            // Ideally there should be a compat library for KeyGenParameterSpec.Builder but
            // which isn't available yet.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
            }
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
