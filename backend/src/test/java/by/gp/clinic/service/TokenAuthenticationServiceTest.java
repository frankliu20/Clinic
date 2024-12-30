package by.gp.clinic.service;

import by.gp.clinic.dbo.VerificationTokenDbo;
import by.gp.clinic.repository.UserRepository;
import by.gp.clinic.repository.VerificationTokenRepository;
import com.google.common.cache.Cache;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TokenAuthenticationServiceTest {

    private static final String ALIAS = "alias";
    private static final String AUTHORIZATION = "Authorization";

    @InjectMocks
    private TokenAuthenticationService service;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VerificationTokenRepository repository;

    @BeforeEach
    public void init() {
        doReturn(1L).when(userRepository).getIdByAlias(ALIAS);
    }

    @Test
    public void addAuthenticationTest() {
        addAuthentication();
    }

    @Test
    public void getAuthenticationEmptyHeaderTest() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        assertNull(service.getAuthentication(request));
    }

    @Test
    public void getAuthenticationWrongTokenTest() {
        assertThrows(SignatureException.class, () -> {
            final MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(AUTHORIZATION, "wrong");
            assertNull(service.getAuthentication(request));
        });
    }

    @Test
    public void getAuthenticationTest() {
        final String token = addAuthentication();
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AUTHORIZATION, token);

        Authentication authentication = service.getAuthentication(request);
        assertNotNull(authentication);
        checkCache(1);
    }

    @Test
    public void logoutTest() {
        final String token = addAuthentication();
        checkCache(1);

        service.logout(token);
        verify(repository, times(1)).deleteByToken(token);
        checkCache(0);
    }

    private String addAuthentication() {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        service.addAuthentication(response, ALIAS, "USER");
        final String token = response.getHeader(AUTHORIZATION);

        assertNotNull(token);
        verify(repository, times(1)).findByToken(token);
        return token;
    }

    @SuppressWarnings("unchecked")
    private void checkCache(final int size) {
        final Class<? extends TokenAuthenticationService> serviceClass = service.getClass();
        try {
            final Field cacheField = serviceClass.getDeclaredField("cache");
            cacheField.setAccessible(true);
            final Cache<String, VerificationTokenDbo> cache =
                (Cache<String, VerificationTokenDbo>) cacheField.get(service);
            assertEquals(cache.size(), size);
        } catch (final Exception e) {
            e.printStackTrace();
            fail("Can't get field");
        }
    }
}