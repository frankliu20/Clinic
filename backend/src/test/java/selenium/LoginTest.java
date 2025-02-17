package selenium;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Random;

public class LoginTest extends AbstractUITest {

    private static final String ALIAS = "new_user" + new RandomString(10).nextString();
    private static final String PASSWORD = "Password1" + new RandomString(10).nextString();
    private static final String FIRST_NAME = "FirstName" + new RandomString(10).nextString();
    private static final String LAST_NAME = "LastName" + new RandomString(10).nextString();
    private static final LocalDate BIRTH_DATE = LocalDate.now().minusYears(18);
    private static final String SEX = new Random().nextBoolean() ? "Male" : "Female";

    @Test
    @Disabled
    public void loginAsAdmin() {
        loginAsAdmin();
        logout();
    }

    @Test
    @Disabled
    public void createUser() {
        loginAsAdmin();
        createPatientLikeAdmin(FIRST_NAME, LAST_NAME, BIRTH_DATE, SEX);
        logout();
    }

    @Test
    @Disabled
    public void sighUpAsUser() {
        loginAsAdmin();
        createPatientLikeAdmin(FIRST_NAME, LAST_NAME, BIRTH_DATE, SEX);
        logout();

        signUp(ALIAS, PASSWORD, FIRST_NAME, LAST_NAME);
        logout();
    }
}
