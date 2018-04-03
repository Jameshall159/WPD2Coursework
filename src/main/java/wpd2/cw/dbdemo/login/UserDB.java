package wpd2.cw.dbdemo.login;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

/*
 *  In memory password hasher.  Needs to run with multiple threads, hence synchronization.
 */
@SuppressWarnings({"unused"})
public class UserDB {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(UserDB.class);

    private final Map<String,String> passwords;

    public UserDB() {
        passwords = new HashMap<>();
    }

    public synchronized boolean login(@NonNull String userName, @NonNull String password) {
        try {
            String storedHash = passwords.get(userName);
            return storedHash != null && PasswordHash.validatePassword(password, storedHash);
        } catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
            LOG.error("Can't validate password: " + e.getMessage());
            return false;
        }
    }

    public synchronized boolean register(@NonNull String userName, @NonNull String password) {
        String current = passwords.get(userName);
        if (current != null) {
            return false;
        }
        try {
            String hash = PasswordHash.createHash(password);
            passwords.put(userName, hash);
            return true;
        } catch (Exception e) {
            LOG.error("Can't hash password <" + password + ">: " + e.getMessage());
            return false;
        }
    }

}
