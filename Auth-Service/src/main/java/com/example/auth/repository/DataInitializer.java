package com.example.auth.repository;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.auth.entity.Permission;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

 
    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create permissions
        Permission readProducts = createPermissionIfNotExists("read:products", "Read product information");
        Permission writeProducts = createPermissionIfNotExists("write:products", "Create/Update products");
        Permission deleteProducts = createPermissionIfNotExists("delete:products", "Delete products");

        Permission readCart = createPermissionIfNotExists("read:cart", "Read cart");
        Permission writeCart = createPermissionIfNotExists("write:cart", "Modify cart");

        Permission readOrders = createPermissionIfNotExists("read:orders", "Read orders");
        Permission writeOrders = createPermissionIfNotExists("write:orders", "Create orders");
        Permission manageOrders = createPermissionIfNotExists("manage:orders", "Manage all orders");

        Permission sendEmail = createPermissionIfNotExists("send:email", "Send emails");

        // Create roles
        Role userRole = createRoleIfNotExists("USER", Set.of(
            readProducts, readCart, writeCart, readOrders, writeOrders
        ));

        Role managerRole = createRoleIfNotExists("MANAGER", Set.of(
            readProducts, writeProducts, readCart, writeCart,
            readOrders, writeOrders, manageOrders
        ));

        Role adminRole = createRoleIfNotExists("ADMIN", Set.of(
            readProducts, writeProducts, deleteProducts,
            readCart, writeCart, readOrders, writeOrders,
            manageOrders, sendEmail
        ));

        // Bootstrap users
        createUserIfNotExists("admin1@example.com", "Admin123!", "Admin", "One", Set.of(adminRole));
        createUserIfNotExists("admin2@example.com", "Admin123!", "Admin", "Two", Set.of(adminRole));
        createUserIfNotExists("manager1@example.com", "Manager123!", "Manager", "One", Set.of(managerRole));

        System.out.println("=================================================");
        System.out.println("Bootstrap users created successfully:");
        System.out.println("Admin 1: admin1@example.com / Admin123!");
        System.out.println("Admin 2: admin2@example.com / Admin123!");
        System.out.println("Manager: manager1@example.com / Manager123!");
        System.out.println("=================================================");
    }

    private Permission createPermissionIfNotExists(String name, String description) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> {
                    Permission permission = new Permission();
                    permission.setName(name);
                    permission.setDescription(description);
                    return permissionRepository.save(permission);
                });
    }

    private Role createRoleIfNotExists(String name, Set<Permission> permissions) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    role.setPermissions(permissions);
                    return roleRepository.save(role);
                });
    }

    private User createUserIfNotExists(String email, String password, String firstName, 
                                       String lastName, Set<Role> roles) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setRoles(roles);
                    return userRepository.save(user);
                });
    }

}