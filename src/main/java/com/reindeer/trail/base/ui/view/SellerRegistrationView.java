package com.reindeer.trail.base.ui.view;

import com.reindeer.trail.util.DatabaseHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("seller-registration")
public class SellerRegistrationView extends VerticalLayout {

    public SellerRegistrationView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setPadding(true);
        setSpacing(true);

        TextField nameField = new TextField("Full Name");
        EmailField emailField = new EmailField("Email");
        PasswordField passwordField = new PasswordField("Password");

        Button registerButton = new Button("Register as Seller", e -> {
            String name = nameField.getValue();
            String email = emailField.getValue();
            String password = passwordField.getValue();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Notification.show("Please fill in all fields");
                return;
            }

            boolean success = DatabaseHelper.registerUser(email, password, name, "seller");

            if (success) {
                Notification.show("✅ Registration successful!");
                nameField.clear();
                emailField.clear();
                passwordField.clear();
            } else {
                Notification.show("❌ Registration failed. Email might already be registered.");
            }
        });

        add(nameField, emailField, passwordField, registerButton);
    }
}
