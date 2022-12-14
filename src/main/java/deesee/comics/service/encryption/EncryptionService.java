package deesee.comics.service.encryption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    @Value("${deeseecomics.encryption.default-shift-only-positive}")
    private int shiftCharToDefault;

    public String encrypt(String toEncrypt, Integer shiftCharTo) {
        final int shiftTo = shiftCharTo == null || shiftCharTo == 0 ? Math.abs(shiftCharToDefault) : Math.abs(shiftCharTo);
        char[] chars = toEncrypt.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'a' && chars[i] <= 'z') {
                chars[i] = (char) ('a' + (chars[i] - 'a' + shiftTo) % 26);
            }
        }

        return String.valueOf(chars);
    }
}
