package ao.co.ensa.investor.util;

import ao.co.ensa.investor.config.AESEncryptionConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Utility wrapper around AESEncryptionConfig for convenient use in services.
 */
@Component
@RequiredArgsConstructor
public class AESUtil {

    private final AESEncryptionConfig aesConfig;

    public String encrypt(String plaintext) {
        try {
            return aesConfig.encrypt(plaintext);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String ciphertext) {
        try {
            return aesConfig.decrypt(ciphertext);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
