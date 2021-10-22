package uz.test.project.example11.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GenerateCodeUtil {

    public GenerateCodeUtil() {
    }

    public static String generateCode() {
        String code;
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();

            int c = random.nextInt(9000) + 1000;
            code = String.valueOf(c);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem when generating random code");
        }

        return code;
    }
}
