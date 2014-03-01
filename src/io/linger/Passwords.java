package io.linger;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom; 
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 * Source: user 'assylias' http://stackoverflow.com/questions/18142745
 * 
 * A utility class to hash passwords and check passwords vs hashed values.
 * It uses a combination of hashing and unique salt. The algorithm used is 
 * PBKDF2WithHmacSHA1 which, although not the best for hashing password 
 * (vs. bcrypt) is still considered robust and 
 * <a href="http://security.stackexchange.com/a/6415/12614"> recommended by 
 * NIST </a>.
 * The hashed value has 256 bits.
 */
public class Passwords
{
    // important to use SecureRandom, not just random:
	// https://crackstation.net/hashing-security.htm#attacks
	private static final Random random = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    /**
     * Returns a random salt to be used to hash a password.
     * @return a 16 bytes random salt
     */
    public static byte[] getNextSalt()
    {
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Returns a salted and hashed password using the provided hash.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     * @param password the password to be hashed
     * @param salt a 16 bytes salt, ideally obtained with the getNextSalt method
     * @return the hashed password with a pinch of salt
     */
    public static byte[] hash(char[] password, byte[] salt)
    {
        char[] pwd = cloneArrayAndEraseOriginal(password);
        KeySpec spec = new PBEKeySpec(pwd, salt, ITERATIONS, KEY_LENGTH);
        try
        {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return f.generateSecret(spec).getEncoded();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        }
        catch (InvalidKeySpecException e)
        {
        	throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        }
    }
    
    /**
     * Return UTF-8 encoded String form of password.
     * @param password
     * @param salt
     * @return
     */
    public static String hashToString(char[] password, byte[] salt)
    {
    	return bytesArrayToString(hash(password, salt));
    }

    private static char[] cloneArrayAndEraseOriginal(char[] password)
    {
        char[] pwd = password.clone();
        Arrays.fill(password, Character.MIN_VALUE);
        return pwd;
    }
    
    /**
     * Encode a bytes array into a String.
     * @param bytes
     * @return
     */
    public static String bytesArrayToString(byte[] bytes)
    {
    	String stringForm = "";
    	for (byte each : bytes)
    		stringForm += each;
    	return stringForm;
    }
    
//    /**
//     * Returns true if the given password and salt match the hashed value, false otherwise.<br>
//     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
//     * @param password the password to check
//     * @param salt the salt used to hash the password
//     * @param expectedHash the expected hashed value of the password
//     * @return true if the given password and salt match the hashed value, false otherwise
//     */
//    public static boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash)
//    {
//        char[] pwd = cloneArrayAndEraseOriginal(password);
//        byte[] pwdHash = hash(pwd, salt);
//        if (pwdHash.length != expectedHash.length) return false;
//        for (int i = 0; i < pwdHash.length; i++) {
//            if (pwdHash[i] != expectedHash[i]) return false;
//        }
//        return true;
//    }
}