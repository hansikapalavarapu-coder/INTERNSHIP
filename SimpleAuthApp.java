public class SimpleAuthApp {

    static class User {
        long id;
        String username;
        String email;
        String password;

        User(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }

    static java.util.Map<String, User> users = new java.util.HashMap<>();

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     Authentication Service Started     ║");
        System.out.println("║          Running on Port 8080          ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        System.out.println("Available API Endpoints:");
        System.out.println("  POST   /auth/register  - Register a new user");
        System.out.println("  POST   /auth/login     - Login user");
        System.out.println("  PUT    /auth/forgot    - Forgot password");
        System.out.println("  PUT    /auth/change    - Change password\n");

        System.out.println("Testing Authentication Service:\n");

        // Test: Register
        System.out.println("1. Testing Register:");
        String result1 = register("john_doe", "john@example.com", "password123");
        System.out.println("   Result: " + result1);

        // Test: Duplicate registration
        System.out.println("\n2. Testing Duplicate Register:");
        String result2 = register("jane_doe", "john@example.com", "pass456");
        System.out.println("   Result: " + result2);

        // Test: Login
        System.out.println("\n3. Testing Login (correct password):");
        String result3 = login("john@example.com", "password123");
        System.out.println("   Result: " + result3);

        // Test: Login with wrong password
        System.out.println("\n4. Testing Login (wrong password):");
        String result4 = login("john@example.com", "wrongpassword");
        System.out.println("   Result: " + result4);

        // Test: Forgot Password
        System.out.println("\n5. Testing Forgot Password:");
        String result5 = forgotPassword("john@example.com", "newpassword456");
        System.out.println("   Result: " + result5);

        // Test: Login with new password
        System.out.println("\n6. Testing Login (with new password):");
        String result6 = login("john@example.com", "newpassword456");
        System.out.println("   Result: " + result6);

        // Test: Change Password
        System.out.println("\n7. Testing Change Password:");
        String result7 = changePassword("john@example.com", "newpassword456", "finalpassword789");
        System.out.println("   Result: " + result7);

        // Test: Login with final password
        System.out.println("\n8. Testing Login (with final password):");
        String result8 = login("john@example.com", "finalpassword789");
        System.out.println("   Result: " + result8);

        System.out.println("\n═════════════════════════════════════════");
        System.out.println("✓ Authentication Service Test Complete!");
        System.out.println("═════════════════════════════════════════");
    }

    static String register(String username, String email, String password) {
        if (username == null || email == null || password == null) {
            return "Error: Missing required fields";
        }

        if (users.containsKey(email)) {
            return "Error: Email already registered";
        }

        User user = new User(username, email, password);
        users.put(email, user);
        return "User registered successfully";
    }

    static String login(String email, String password) {
        if (email == null || password == null) {
            return "Error: Email and password are required";
        }

        if (!users.containsKey(email)) {
            return "Error: User not found";
        }

        User user = users.get(email);
        if (user.password.equals(password)) {
            return "Login successful";
        } else {
            return "Error: Invalid password";
        }
    }

    static String forgotPassword(String email, String newPassword) {
        if (email == null || newPassword == null) {
            return "Error: Email and new password are required";
        }

        if (!users.containsKey(email)) {
            return "Error: User not found";
        }

        User user = users.get(email);
        user.password = newPassword;
        return "Password reset successfully";
    }

    static String changePassword(String email, String oldPassword, String newPassword) {
        if (email == null || oldPassword == null || newPassword == null) {
            return "Error: Missing required fields";
        }

        if (!users.containsKey(email)) {
            return "Error: User not found";
        }

        User user = users.get(email);
        if (!user.password.equals(oldPassword)) {
            return "Error: Old password is incorrect";
        }

        user.password = newPassword;
        return "Password changed successfully";
    }
}
