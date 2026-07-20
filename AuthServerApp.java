import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AuthServerApp {

    static class User {
        String username;
        String email;
        String password;

        User(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }

    static Map<String, User> users = new HashMap<>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        
        // Create contexts
        server.createContext("/", new StaticFileHandler());
        server.createContext("/auth/register", new RegisterHandler());
        server.createContext("/auth/login", new LoginHandler());
        server.createContext("/auth/forgot", new ForgotHandler());
        server.createContext("/auth/change", new ChangeHandler());
        server.createContext("/auth/users", new UsersHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  Authentication Service Web Server     ║");
        System.out.println("║      Running on http://localhost:8080  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        System.out.println("✓ Open your browser and visit:");
        System.out.println("  → http://localhost:8080\n");
        System.out.println("Press Ctrl+C to stop the server\n");
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            
            if (path.equals("/") || path.isEmpty()) {
                path = "/index.html";
            }
            
            String filePath = "c:\\Users\\hansi\\OneDrive\\Desktop\\INTERNSHIP" + path;
            
            try {
                File file = new File(filePath);
                if (file.exists() && file.isFile()) {
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    
                    String contentType = "text/html; charset=UTF-8";
                    if (filePath.endsWith(".css")) contentType = "text/css; charset=UTF-8";
                    else if (filePath.endsWith(".js")) contentType = "application/javascript; charset=UTF-8";
                    else if (filePath.endsWith(".png")) contentType = "image/png";
                    else if (filePath.endsWith(".jpg")) contentType = "image/jpeg";
                    
                    exchange.getResponseHeaders().set("Content-Type", contentType);
                    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    exchange.sendResponseHeaders(200, fileContent.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(fileContent);
                    os.close();
                } else {
                    send404(exchange);
                }
            } catch (Exception e) {
                send404(exchange);
            }
        }
        
        void send404(HttpExchange exchange) throws IOException {
            String response = "404 - File Not Found";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(404, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                return;
            }
            
            if (exchange.getRequestMethod().equals("POST")) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);
                
                String username = params.get("username");
                String email = params.get("email");
                String password = params.get("password");
                
                String result = register(username, email, password);
                sendResponse(exchange, result, 200);
            } else {
                sendResponse(exchange, "Method not allowed", 405);
            }
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                return;
            }
            
            if (exchange.getRequestMethod().equals("POST")) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);
                
                String email = params.get("email");
                String password = params.get("password");
                
                String result = login(email, password);
                sendResponse(exchange, result, 200);
            } else {
                sendResponse(exchange, "Method not allowed", 405);
            }
        }
    }

    static class ForgotHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "PUT, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                return;
            }
            
            if (exchange.getRequestMethod().equals("PUT")) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);
                
                String email = params.get("email");
                String newPassword = params.get("newPassword");
                
                String result = forgotPassword(email, newPassword);
                sendResponse(exchange, result, 200);
            } else {
                sendResponse(exchange, "Method not allowed", 405);
            }
        }
    }

    static class ChangeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "PUT, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                return;
            }
            
            if (exchange.getRequestMethod().equals("PUT")) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseJson(body);
                
                String email = params.get("email");
                String oldPassword = params.get("oldPassword");
                String newPassword = params.get("newPassword");
                
                String result = changePassword(email, oldPassword, newPassword);
                sendResponse(exchange, result, 200);
            } else {
                sendResponse(exchange, "Method not allowed", 405);
            }
        }
    }

    static void sendResponse(HttpExchange exchange, String response, int code) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        String jsonResponse = "{\"message\": \"" + response.replace("\"", "\\\"") + "\"}";
        exchange.sendResponseHeaders(code, jsonResponse.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes());
        os.close();
    }

    static Map<String, String> parseJson(String json) {
        Map<String, String> map = new HashMap<>();
        json = json.replace("{", "").replace("}", "").replace("\"", "");
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return map;
    }

    static String register(String username, String email, String password) {
        if (username == null || email == null || password == null) {
            return "Error: Missing required fields";
        }
        if (users.containsKey(email)) {
            return "Error: Email already registered";
        }
        users.put(email, new User(username, email, password));
        System.out.println("[REGISTER] ✓ User registered: " + email + " (" + username + ")");
        return "User registered successfully";
    }

    static String login(String email, String password) {
        if (email == null || password == null) {
            return "Error: Email and password are required";
        }
        if (!users.containsKey(email)) {
            return "Error: User not found";
        }
        if (users.get(email).password.equals(password)) {
            System.out.println("[LOGIN] ✓ Successful login: " + email);
            return "Login successful";
        }
        System.out.println("[LOGIN] ✗ Failed login attempt: " + email);
        return "Error: Invalid password";
    }

    static String forgotPassword(String email, String newPassword) {
        if (email == null || newPassword == null) {
            return "Error: Email and new password are required";
        }
        if (!users.containsKey(email)) {
            return "Error: User not found";
        }
        users.get(email).password = newPassword;
        System.out.println("[FORGOT] ✓ Password reset: " + email);
        return "Password reset successfully";
    }

    static String changePassword(String email, String oldPassword, String newPassword) {
        if (email == null || oldPassword == null || newPassword == null) {
            return "Error: Missing required fields";
        }
        if (!users.containsKey(email)) {
            return "Error: User not found";
        }
        if (!users.get(email).password.equals(oldPassword)) {
            System.out.println("[CHANGE] ✗ Failed password change (wrong old password): " + email);
            return "Error: Old password is incorrect";
        }
        users.get(email).password = newPassword;
        System.out.println("[CHANGE] ✓ Password changed: " + email);
        return "Password changed successfully";
    }
}
