package io.github.javathought.devoxx;

import io.github.javathought.devoxx.security.PasswordStorage;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.util.UUID;

import static io.github.javathought.devoxx.dao.Connexion.uuidToBytes;
import static io.github.javathought.devoxx.security.PasswordStorage.createHash;
import static java.lang.System.out;

public class InitDBTest {
    
    @Test
    public void shouldHaveBadPassword() throws PasswordStorage.CannotPerformOperationException {
        out.println(createHash("admin"));
        out.println(Hex.encodeHexString(uuidToBytes(UUID.randomUUID())));
        out.println(Hex.encodeHexString(uuidToBytes(UUID.randomUUID())));
    }
    
}
