package myFiles;

import myFiles.User;
import myFiles.Admin;
import myFiles.Customer;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
    private HashMap<String, User> users;

    @Before
    public void setUp() {
        users = new HashMap<>();
        users.put("adminUser", new Admin("adminUser", "adminPass"));
        users.put("customerUser", new Customer("customerUser", "customerPass"));
    }

    @Test
    public void testValidAdminLogin() {
        String username = "adminUser";
        String password = "adminPass";
        String role = "Admin";

        User user = users.get(username);
        assertTrue(user != null && user.authenticate(username, password, role), "Admin login should succeed.");
    }

    @Test
    public void testValidCustomerLogin() {
        String username = "customerUser";
        String password = "customerPass";
        String role = "Customer";

        User user = users.get(username);
        assertTrue(user != null && user.authenticate(username, password, role), "Customer login should succeed.");
    }

    @Test
    public void testInvalidLogin() {
        String username = "invalidUser";
        String password = "wrongPass";
        String role = "Admin";

        User user = users.get(username);
        assertFalse(user != null && user.authenticate(username, password, role), "Invalid login should fail.");
    }

    @Test
    public void testIncorrectPassword() {
        String username = "customerUser";
        String password = "wrongPass";
        String role = "Customer";

        User user = users.get(username);
        assertFalse(user != null && user.authenticate(username, password, role), "Login with incorrect password should fail.");
    }
}
