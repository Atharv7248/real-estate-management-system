package com.reindeer.trail.base.ui.view;

import com.reindeer.trail.model.Property;
import com.reindeer.trail.service.PropertyService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("buyer-dashboard")
@PermitAll
public class BuyerDashboardView extends VerticalLayout {

    private final PropertyService propertyService;

    @Autowired
    public BuyerDashboardView(PropertyService propertyService) {
        this.propertyService = propertyService;

        getStyle().set("background-color", "white");

        // Header with title and logout button
        H1 title = new H1("Welcome, Buyer!");
        title.getStyle().set("color", "#1f2937");

        Button logoutButton = new Button("Logout", e -> {
            getUI().ifPresent(ui -> ui.getPage().setLocation("/logout"));
        });
        logoutButton.getStyle()
                .set("background-color", "#f44336")
                .set("color", "white");

        HorizontalLayout header = new HorizontalLayout(title, logoutButton);
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);

        Paragraph subtitle = new Paragraph("Browse and purchase properties of your choice.");
        subtitle.getStyle().set("margin-bottom", "24px");

        add(header, subtitle);

        List<Property> properties = propertyService.listAll();

        Div cardRow = new Div();
        cardRow.getStyle()
                .set("display", "flex")
                .set("flex-wrap", "wrap")
                .set("gap", "20px")
                .set("justify-content", "flex-start");

        for (Property property : properties) {
            cardRow.add(createPropertyCard(property));
        }

        add(cardRow);
        setPadding(true);
        setSpacing(true);
    }

    private Div createPropertyCard(Property property) {
        Div card = new Div();
        card.getStyle()
                .set("border", "1px solid #ddd")
                .set("border-radius", "10px")
                .set("width", "300px")
                .set("box-shadow", "0 4px 10px rgba(0,0,0,0.07)")
                .set("background-color", "white")
                .set("transition", "transform 0.2s, box-shadow 0.2s");

        card.addClickListener(e -> showDetailsDialog(property));
        card.getElement().getStyle().set("overflow", "hidden");

        card.getElement().setAttribute("onmouseover", "this.style.transform='scale(1.02)'; this.style.boxShadow='0 8px 20px rgba(0,0,0,0.15)'");
        card.getElement().setAttribute("onmouseout", "this.style.transform='scale(1)'; this.style.boxShadow='0 4px 10px rgba(0,0,0,0.07)'");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(false);
        content.setSizeFull();
        content.getStyle().set("height", "100%");

        Image image = new Image("img/" + (property.getImage() != null ? property.getImage() : "default.jpg"), "Property Image");
        image.setWidth("100%");
        image.getStyle().set("border-radius", "10px").set("margin-bottom", "10px");

        Span title = new Span("🏠 " + property.getTitle());
        title.getStyle().set("font-weight", "bold").set("font-size", "18px").set("color", "#333");

        Span price = new Span("💰 ₹" + property.getPrice());
        price.getStyle().set("font-size", "16px").set("color", "#4CAF50");

        Span location = new Span("📍 " + property.getLocation());
        location.getStyle().set("font-size", "14px").set("color", "#666");

        Button buyBtn = new Button("Buy Now", e -> {
            Notification.show("Buying process started for: " + property.getTitle(), 3000, Notification.Position.TOP_CENTER);
        });
        buyBtn.getStyle()
                .set("margin-top", "auto")
                .set("background-color", "#1E88E5")
                .set("color", "white")
                .set("width", "100%");

        content.add(image, title, price, location, buyBtn);
        content.setAlignSelf(Alignment.END, buyBtn);
        card.add(content);

        return card;
    }

    private void showDetailsDialog(Property property) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnOutsideClick(true);

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);

        H3 heading = new H3("Property Details");
        heading.getStyle().set("color", "#1f2937");

        Image image = new Image("img/" + (property.getImage() != null ? property.getImage() : "default.jpg"), "Property Image");
        image.setWidth("100%");
        image.getStyle().set("border-radius", "10px");

        Span title = new Span("Title: " + property.getTitle());
        Span location = new Span("Location: " + property.getLocation());
        Span price = new Span("Price: ₹" + property.getPrice());

        title.getStyle().set("font-weight", "bold");
        location.getStyle().set("color", "#555");
        price.getStyle().set("color", "#4CAF50");

        Button contactOwnerBtn = new Button("Contact Owner", event -> {
            getUI().ifPresent(ui ->
                    ui.getPage().setLocation("https://wa.me/917248953585")
            );
        });
        contactOwnerBtn.getStyle()
                .set("background-color", "#25D366")
                .set("color", "white");

        Button closeBtn = new Button("Close", e -> dialog.close());
        closeBtn.getStyle().set("background-color", "#f44336").set("color", "white");

        layout.add(heading, image, title, location, price, contactOwnerBtn, closeBtn);
        dialog.add(layout);
        dialog.open();
    }
}
