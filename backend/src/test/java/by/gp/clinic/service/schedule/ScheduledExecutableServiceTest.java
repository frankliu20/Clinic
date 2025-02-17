package by.gp.clinic.service.schedule;

import by.gp.clinic.AbstractSpringMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledExecutableServiceTest extends AbstractSpringMvcTest {

    @Autowired
    ScheduledExecutableService scheduledExecutableService;

    @Test
    public void performDailyExecutingTest() {
        scheduledExecutableService.performDailyExecuting();
    }
}
