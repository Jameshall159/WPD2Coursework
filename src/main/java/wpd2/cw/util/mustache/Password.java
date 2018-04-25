package wpd2.cw.util.mustache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Secure password hash generation
 */
public class Password {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(Password.class);

    // hash passwords slowly
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    // The following constants may be changed without breaking existing hashes.
    private static final int SALT_BYTES = 24;
    private static final int HASH_BYTES = 24;
    private static final int PBKDF2_ITERATIONS = 1000;

    private static final int ITERATION_INDEX = 0;
    private static final int SALT_INDEX = 1;
    private static final int PBKDF2_INDEX = 2;

    private Password() {}

    /**
     * Create a password hash
     *
     * @param password The password
     * @return The hashed password, which is a salt and a hash
     * @throws PasswordException If the pbdkdf2 algorithm is not available or there is an internal key spec error
     */
    public static String createHash(String password) {
        try {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_BYTES];
            random.nextBytes(salt);

            // Hash the password
            byte[] hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTES);
            // format iterations:salt:hash
            return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new PasswordException(e);
        }
    }

    /**
     * Check a password against a hash to see if it matched
     *
     * @param password The pasword to check
     * @param goodHash The hash (salt and hash)
     * @return true iff the password matched the hash
     * @throws PasswordException If the pbdkdf2 algorithm is not available or there is an internal key spec error
     */
    public static boolean validatePassword(String password, String goodHash) {
        try {
            // Decode the hash into its parameters
            String[] params = goodHash.split(":");
            int iterations = Integer.parseInt(params[ITERATION_INDEX]);
            byte[] salt = fromHex(params[SALT_INDEX]);
            byte[] hash = fromHex(params[PBKDF2_INDEX]);
            // Compute the hash of the provided password, using the same salt,
            // iteration count, and hash length
            byte[] testHash = pbkdf2(password.toCharArray(), salt, iterations, hash.length);
            // Compare the hashes in constant time. The password is correct if
            // both hashes match.
            return slowEquals(hash, testHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new PasswordException(e);
        }
    }

    // check that two byte arrays are equal, taking constant time, to prevent timing attacks
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    // runs the algorithm, generates the hash according to the built-in PBKDF2 algorithm
    // hashes slowly
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    // convert from hex to bytes
    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    // convert bytes to hex
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

    public static class PasswordException extends RuntimeException {
        PasswordException(Exception e) {
            super(e);
        }
    }
}
