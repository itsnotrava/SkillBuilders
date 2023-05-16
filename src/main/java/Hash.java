import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;

public class Hash {
       public static String creaHash(String originalPassword) throws NoSuchAlgorithmException
        {
            String generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
            return generatedSecuredPasswordHash;
        }
    }
