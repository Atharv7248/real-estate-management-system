package com.reindeer.trail.todo.ui.view;

import com.reindeer.trail.model.Property;
import com.reindeer.trail.service.PropertyService;
import com.reindeer.trail.util.DatabaseHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Route("properties")
@CssImport("./styles/property-card.css")
@PermitAll
public class PropertyView extends VerticalLayout {

    private final PropertyService propertyService;

    private ComboBox<String> locationFilter;
    private NumberField minPrice;
    private NumberField maxPrice;
    private Div cardRow;

    @Autowired
    public PropertyView(PropertyService propertyService) {
        this.propertyService = propertyService;

        setPadding(true);
        setSpacing(true);

        add(createHeader());
        add(createToolbar());
        cardRow = new Div();
        cardRow.getStyle().set("display", "flex");
        cardRow.getStyle().set("flex-wrap", "wrap");
        cardRow.getStyle().set("gap", "16px");
        add(cardRow);

        refreshPropertyCards();
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);
        header.addClassName("property-header");

        H2 title = new H2("MIT-WPU Project");
        title.getStyle().set("margin", "0").set("color", "var(--primary-color)");

        HorizontalLayout navigation = new HorizontalLayout();
        Anchor homeButton = new Anchor("/properties", "Home");
        Anchor aboutButton = new Anchor("/about", "About");
        Anchor signInButton = new Anchor("/login", "Sign in");

        homeButton.getElement().setAttribute("router-ignore", "true");
        aboutButton.getElement().setAttribute("router-ignore", "true");
        signInButton.getElement().setAttribute("router-ignore", "true");

        navigation.add(homeButton, aboutButton, signInButton);
        header.add(title, navigation);

        header.getStyle()
            .set("padding", "16px")
            .set("background", "white")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)")
            .set("border-radius", "8px")
            .set("margin-bottom", "24px");

        return header;
    }

    private HorizontalLayout createToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setAlignItems(Alignment.BASELINE);
        toolbar.setSpacing(true);

        locationFilter = new ComboBox<>("Location");
        locationFilter.setItems("Pune", "Mumbai", "Bangalore", "Delhi", "Lonavala", "Goa", "Manali", "Chennai");
        locationFilter.setPlaceholder("Select location");

        minPrice = new NumberField("Min Price");
        minPrice.setPlaceholder("Min ₹");
        minPrice.setMin(0);
        minPrice.setStep(1000);

        maxPrice = new NumberField("Max Price");
        maxPrice.setPlaceholder("Max ₹");
        maxPrice.setMin(0);
        maxPrice.setStep(1000);

        Button searchButton = new Button("Search", e -> refreshPropertyCards());
        searchButton.getStyle().set("margin-left", "16px");

        Button addButton = new Button("Add Property", event -> {
            Notification.show("Login As Property Seller [Contact Admin For login Credentials]", 5000, Notification.Position.MIDDLE);
        });

        toolbar.add(locationFilter, minPrice, maxPrice, searchButton, addButton);
        return toolbar;
    }

    private void refreshPropertyCards() {
        cardRow.removeAll();

        List<Property> properties = propertyService.listAll();

        if (locationFilter.getValue() != null) {
            properties = properties.stream()
                .filter(p -> p.getLocation().equalsIgnoreCase(locationFilter.getValue()))
                .collect(Collectors.toList());
        }

        if (minPrice.getValue() != null) {
            properties = properties.stream()
                .filter(p -> p.getPrice().compareTo(BigDecimal.valueOf(minPrice.getValue())) >= 0)
                .collect(Collectors.toList());
        }

        if (maxPrice.getValue() != null) {
            properties = properties.stream()
                .filter(p -> p.getPrice().compareTo(BigDecimal.valueOf(maxPrice.getValue())) <= 0)
                .collect(Collectors.toList());
        }

        for (Property property : properties) {
            cardRow.add(createPropertyCard(property));
        }
    }

    private Div createPropertyCard(Property property) {
        Div card = new Div();
        card.addClassName("property-card");

        Div imageWrapper = new Div();
        imageWrapper.addClassName("image-wrapper");

        String imagePath = "img/" + (property.getImage() != null ? property.getImage() : "default.jpg");
        Image image = new Image(imagePath, "Property Image");
        image.addClassName("property-image");

        Span photoCount = new Span("📷 5");
        photoCount.addClassName("photo-count");

        imageWrapper.add(image, photoCount);

        Span title = new Span(property.getTitle());
        Span price = new Span("₹" + property.getPrice());
        Span location = new Span(property.getLocation());
        Span status = new Span("Available");

        title.addClassName("title");
        price.addClassName("price-size");
        location.addClassName("location");
        status.addClassName("status");

        Button buyNowBtn = new Button("Buy Now", e -> showBuyerFormDialog(property));
        buyNowBtn.getStyle().set("margin-top", "10px");

        card.add(imageWrapper, title, price, location, status, buyNowBtn);
        return card;
    }

    private void showBuyerFormDialog(Property property) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Buyer Registration - " + property.getTitle());

        TextField nameField = new TextField("Full Name");
        EmailField emailField = new EmailField("Email");
        PasswordField passwordField = new PasswordField("Password");

        Button registerButton = new Button("Register and Buy", e -> {
            String name = nameField.getValue();
            String email = emailField.getValue();
            String password = passwordField.getValue();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Notification.show("Please fill in all fields");
                return;
            }

            boolean success = DatabaseHelper.registerUser(email, password, name, "buyer");

            if (success) {
                Notification.show("🎉 Purchase Registered Successfully!");
                dialog.close();
            } else {
                Notification.show("❌ Registration failed. Email might already exist.");
            }
        });

        dialog.add(nameField, emailField, passwordField, registerButton);
        dialog.open();
    }
}
