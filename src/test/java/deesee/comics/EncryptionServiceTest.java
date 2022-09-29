package deesee.comics;

import deesee.comics.service.encryption.EncryptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {
    private final EncryptionService encryptionService = new EncryptionService();

    @Test
    void encryptionTest(){
        String encryptedStr = encryptionService.encrypt("clark", 5);
        assertEquals("hqfwp", encryptedStr);
    }

    @Test
    void encryptionWithLastAlphabetLettersTest(){
        String encryptedStr = encryptionService.encrypt("cherry blossom", 3);
        assertEquals("fkhuub eorvvrp", encryptedStr);

        encryptedStr = encryptionService.encrypt("xyz", 3);
        assertEquals("abc", encryptedStr);
    }

    @Test
    void encryptionWithBigShiftCharsTest(){
        String encryptedStr = encryptionService.encrypt("cherry blossom", 1234);
        assertEquals("otqddk nxaeeay", encryptedStr);
    }

    @Test
    void encryptionWithNegativeShiftCharsTest(){
        String encryptedStr = encryptionService.encrypt("cherry blossom", -3);
        assertEquals("fkhuub eorvvrp", encryptedStr);
    }

    @Test
    void encryptionWithOtherLettersAndSymbolsTest(){
        String encryptedStr = encryptionService.encrypt("CHERRY blossom", 3);
        assertEquals("CHERRY eorvvrp", encryptedStr);
        encryptedStr = encryptionService.encrypt("1Q!4!@#$a", 1);
        assertEquals("1Q!4!@#$b", encryptedStr);
    }
}