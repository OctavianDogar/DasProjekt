package edu.msg.jbook.backend.util;

import edu.msg.jbook.backend.exception.EncryptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by iacobd on 26.07.2016.
 */
public class PasswordEncryptorSHA1 {
    private static final Logger LOG = LoggerFactory.getLogger(PasswordEncryptorSHA1.class);

    public static String encryptPassword(String password) throws EncryptionException {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Password encription failed: No such algorithm.", e);
            throw new EncryptionException("Password encryption failed: No such algorithm.",e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Password encription failed: Encoding not supported", e);
            throw new EncryptionException("Password encription failed: Encoding not supported", e);
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
