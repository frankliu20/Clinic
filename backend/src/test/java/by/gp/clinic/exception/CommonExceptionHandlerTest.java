package by.gp.clinic.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
public class CommonExceptionHandlerTest {

    @InjectMocks
    private CommonExceptionHandler commonExceptionHandler;

    @Test
    public void handleCommonException() {
        final MockitoException exception = new MockitoException("Exception");
        final ResponseEntity<String> response = commonExceptionHandler.handleCommonException(exception);

        assertSame(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}