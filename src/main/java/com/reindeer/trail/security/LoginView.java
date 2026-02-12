package com.reindeer.trail.security;

import com.reindeer.trail.util.DatabaseHelper;
import com.reindeer.trail.util.CurrentUser;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | Real Estate App")
public class LoginView extends VerticalLayout {

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background-color", "#f9f9f9");

        // Header
        VerticalLayout header = new VerticalLayout();
        header.setAlignItems(Alignment.CENTER);
        header.setSpacing(false);

        Icon homeIcon = new Icon(VaadinIcon.HOME);
        homeIcon.setSize("48px");
        homeIcon.setColor("#0077cc");

        H2 title = new H2("Welcome to Real Estate [MIT-WPU Project]");
        Paragraph subtitle = new Paragraph("Login with your username and password to access your dashboard.");
        subtitle.getStyle().set("text-align", "center").set("max-width", "400px");

        header.add(homeIcon, title, subtitle);

        // Login Form
        VerticalLayout loginForm = new VerticalLayout();
        loginForm.setWidth("400px");
        loginForm.setPadding(true);
        loginForm.setSpacing(true);
        loginForm.setAlignItems(Alignment.STRETCH);
        loginForm.getStyle()
                .set("background-color", "white")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.05)")
                .set("border-radius", "10px")
                .set("padding", "2rem");

        H3 loginTitle = new H3("Login to Your Account");

        TextField usernameField = new TextField("Username");
        usernameField.setPlaceholder("Enter your username");

        PasswordField passwordField = new PasswordField("Password");
        passwordField.setPlaceholder("Enter your password");

        Button loginButton = new Button("Login");
        loginButton.getStyle().set("background-color", "#0077cc").set("color", "white");

        loginButton.addClickListener(e -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            try {
                if (DatabaseHelper.getUserByUsernameAndPassword(username, password).next()) {
                    String role = DatabaseHelper.getUserRole(username);
                    if (role != null) {
                        CurrentUser.set(username, role);
                        switch (role.toLowerCase()) {
                            case "buyer" -> UI.getCurrent().navigate("buyer-dashboard");
                            case "seller" -> UI.getCurrent().navigate("seller-dashboard");
                            case "admin" -> UI.getCurrent().navigate("admin/dashboard");
                            default -> Notification.show("Unknown role: " + role, 3000, Notification.Position.MIDDLE);
                        }
                    } else {
                        Notification.show("Role not found", 3000, Notification.Position.MIDDLE);
                    }
                } else {
                    Notification.show("Invalid username or password", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Notification.show("Login failed: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        loginForm.add(loginTitle, usernameField, passwordField, loginButton);
        add(header, loginForm);
    }
}
