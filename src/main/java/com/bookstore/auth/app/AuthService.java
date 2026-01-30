package com.bookstore.auth.app;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {
    private final Map<String, String> users = Map.of(
            "admin", "admin",
            "user", "user"
    );

    public List<String> permissionsFor(String username) {
        if ("admin".equals(username)) {
            return List.of("BOOK_READ","BOOK_WRITE","CUSTOMER_READ","CUSTOMER_WRITE","ORDER_READ","ORDER_CREATE");
        }
        return List.of("BOOK_READ","CUSTOMER_READ","ORDER_READ","ORDER_CREATE");
    }

    public boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
