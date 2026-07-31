package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.db.Storage;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationService;
import core.basesyntax.service.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private static final String VALID_LOGIN = "mylogin";
    private static final String VALID_PASSWORD = "secret";
    private static final int VALID_AGE = 18;

    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        Storage.people.clear();
        registrationService = new RegistrationServiceImpl();
    }

    @Test
    void register_nullUser_notOk() {
        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(null)
        );
    }

    @Test
    void register_nullLogin_notOk() {
        User user = createValidUser();
        user.setLogin(null);

        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );
    }

    @Test
    void register_shortLogin_notOk() {
        User user = createValidUser();

        user.setLogin("");
        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );

        user.setLogin("abc");
        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );

        user.setLogin("abcde");
        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );
    }

    @Test
    void register_minLengthLogin_ok() {
        User user = createValidUser();
        user.setLogin("abcdef");

        User actual = registrationService.register(user);

        assertSame(user, actual);
        assertTrue(Storage.people.contains(user));
    }

    @Test
    void register_longLogin_ok() {
        User user = createValidUser();
        user.setLogin("abcdefgh");

        User actual = registrationService.register(user);

        assertSame(user, actual);
        assertTrue(Storage.people.contains(user));
    }

    @Test
    void register_nullPassword_notOk() {
        User user = createValidUser();
        user.setPassword(null);

        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );
    }

    @Test
    void register_shortPassword_notOk() {
        User user = createValidUser();

        user.setPassword("");
        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );

        user.setPassword("abc");
        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );

        user.setPassword("abcde");
        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );
    }

    @Test
    void register_minLengthPassword_ok() {
        User user = createValidUser();
        user.setPassword("abcdef");

        User actual = registrationService.register(user);

        assertSame(user, actual);
        assertTrue(Storage.people.contains(user));
    }

    @Test
    void register_longPassword_ok() {
        User user = createValidUser();
        user.setPassword("abcdefgh");

        User actual = registrationService.register(user);

        assertSame(user, actual);
        assertTrue(Storage.people.contains(user));
    }

    @Test
    void register_nullAge_notOk() {
        User user = createValidUser();
        user.setAge(null);

        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );
    }

    @Test
    void register_underage_notOk() {
        User user = createValidUser();

        user.setAge(-1);
        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );

        user.setAge(0);
        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );

        user.setAge(17);
        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(user)
        );
    }

    @Test
    void register_minAge_ok() {
        User user = createValidUser();
        user.setAge(18);

        User actual = registrationService.register(user);

        assertSame(user, actual);
        assertTrue(Storage.people.contains(user));
    }

    @Test
    void register_overMinAge_ok() {
        User user = createValidUser();
        user.setAge(25);

        User actual = registrationService.register(user);

        assertSame(user, actual);
        assertTrue(Storage.people.contains(user));
    }

    @Test
    void register_existingLogin_notOk() {
        User existingUser = createValidUser();
        Storage.people.add(existingUser);

        User newUser = createValidUser();
        newUser.setPassword("anotherPassword");
        newUser.setAge(30);

        assertThrows(
                RegistrationException.class,
                () -> registrationService.register(newUser)
        );

        assertEquals(1, Storage.people.size());
    }

    @Test
    void register_validUser_ok() {
        User user = createValidUser();

        User actual = registrationService.register(user);

        assertSame(user, actual);
        assertNotNull(actual.getId());
        assertEquals(1, Storage.people.size());
        assertTrue(Storage.people.contains(user));
    }

    private User createValidUser() {
        User user = new User();
        user.setLogin(VALID_LOGIN);
        user.setPassword(VALID_PASSWORD);
        user.setAge(VALID_AGE);
        return user;
    }
}