package com.reindeer.trail.base.ui.view;

import com.reindeer.trail.model.Property;
import com.reindeer.trail.service.PropertyService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@Route("admin/dashboard")
@PermitAll
public class AdminDashboardView extends VerticalLayout {

    private final PropertyService propertyService;
    private Div cardRow;

    @Autowired
    public AdminDashboardView(PropertyService propertyService) {
        this.propertyService = propertyService;

        setPadding(true);
        setSpacing(true);

        add(createHeader());
        add(createAddButton());
        add(createUserManagementSection());

        cardRow = new Div();
        cardRow.getStyle().set("display", "flex").set("flex-wrap", "wrap").set("gap", "24px");
        add(cardRow);

        refreshPropertyCards();
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);

        H2 title = new H2("Admin Dashboard");
        title.getStyle().set("margin", "0").set("color", "#2c3e50");

        HorizontalLayout nav = new HorizontalLayout();
        Anchor home = new Anchor("/properties", "Home");
        Anchor about = new Anchor("/about", "About");
        Anchor signOut = new Anchor("/login", "Sign out");

        nav.add(home, about, signOut);
        header.add(title, nav);

        header.getStyle()
            .set("padding", "16px")
            .set("background", "#ffffff")
            .set("box-shadow", "0 2px 6px rgba(0, 0, 0, 0.1)")
            .set("border-radius", "12px")
            .set("margin-bottom", "24px");

        return header;
    }

    private Button createAddButton() {
        Button add = new Button("➕ Add Property");
        add.getStyle()
            .set("margin-bottom", "20px")
            .set("background-color", "#4CAF50")
            .set("color", "white")
            .set("border-radius", "8px");

        add.addClickListener(e -> showAddDialog());
        return add;
    }

    private Div createUserManagementSection() {
        Div userSection = new Div();
        userSection.getStyle().set("margin-bottom", "20px");

        H4 userHeading = new H4("Manage Users (Coming Soon)");
        Span desc = new Span("You will be able to add/edit/delete buyers and sellers here.");

        userSection.add(userHeading, desc);
        return userSection;
    }

    private void refreshPropertyCards() {
        cardRow.removeAll();
        List<Property> allProperties = propertyService.listAll();

        for (Property property : allProperties) {
            cardRow.add(createPropertyCard(property));
        }
    }

    private Div createPropertyCard(Property property) {
        Div card = new Div();
        card.getStyle()
            .set("width", "300px")
            .set("border-radius", "12px")
            .set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)")
            .set("overflow", "hidden")
            .set("background-color", "#fff")
            .set("display", "flex")
            .set("flex-direction", "column");

        Image image = new Image("img/" + (property.getImage() != null ? property.getImage() : "default.jpg"), "Property Image");
        image.setWidth("100%");
        image.setHeight("200px");
        image.getStyle().set("object-fit", "cover");

        Div content = new Div();
        content.getStyle().set("padding", "16px");

        H4 title = new H4(property.getTitle());
        Span location = new Span("📍 " + property.getLocation());
        Span price = new Span("💰 ₹" + property.getPrice().toString());

        VerticalLayout infoLayout = new VerticalLayout(title, location, price);
        infoLayout.setPadding(false);
        infoLayout.setSpacing(false);

        HorizontalLayout buttonRow = new HorizontalLayout();
        buttonRow.setSpacing(true);

        Button edit = new Button("Edit", e -> showEditDialog(property));
        edit.getStyle().set("background-color", "#2196F3").set("color", "white").set("border-radius", "8px");

        Button delete = new Button("Delete", e -> {
            propertyService.delete(property.getId());
            Notification.show("Deleted: " + property.getTitle());
            refreshPropertyCards();
        });
        delete.getStyle().set("background-color", "#f44336").set("color", "white").set("border-radius", "8px");

        buttonRow.add(edit, delete);
        content.add(infoLayout, buttonRow);
        card.add(image, content);

        return card;
    }

    private void showAddDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        TextField title = new TextField("Title");
        TextField location = new TextField("Location");
        TextField price = new TextField("Price");
        TextField image = new TextField("Image Filename");

        Button save = new Button("Save", e -> {
            try {
                Property prop = new Property();
                prop.setTitle(title.getValue());
                prop.setLocation(location.getValue());
                prop.setPrice(new BigDecimal(price.getValue()));
                prop.setImage(image.getValue());
                propertyService.save(prop);
                dialog.close();
                refreshPropertyCards();
                Notification.show("Property added");
            } catch (Exception ex) {
                Notification.show("Invalid input");
            }
        });
        save.getStyle().set("background-color", "#4CAF50").set("color", "white").set("border-radius", "8px");

        VerticalLayout form = new VerticalLayout(title, location, price, image, save);
        form.setPadding(false);
        form.setSpacing(true);
        dialog.add(form);
        dialog.open();
    }

    private void showEditDialog(Property property) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        TextField title = new TextField("Title", property.getTitle());
        TextField location = new TextField("Location", property.getLocation());
        TextField price = new TextField("Price", property.getPrice().toString());
        TextField image = new TextField("Image Filename", property.getImage());

        Button save = new Button("Update", e -> {
            try {
                property.setTitle(title.getValue());
                property.setLocation(location.getValue());
                property.setPrice(new BigDecimal(price.getValue()));
                property.setImage(image.getValue());
                propertyService.save(property);
                dialog.close();
                refreshPropertyCards();
                Notification.show("Property updated");
            } catch (Exception ex) {
                Notification.show("Invalid input");
            }
        });
        save.getStyle().set("background-color", "#2196F3").set("color", "white").set("border-radius", "8px");

        VerticalLayout form = new VerticalLayout(title, location, price, image, save);
        form.setPadding(false);
        form.setSpacing(true);
        dialog.add(form);
        dialog.open();
    }
}
