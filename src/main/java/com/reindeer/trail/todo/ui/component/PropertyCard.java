package com.reindeer.trail.todo.ui.component;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./styles/property-card.css")
public class PropertyCard extends VerticalLayout {

    public PropertyCard(String imageUrl, String title, String price, String size, String location, String status, int photoCount) {
        // Wrapper class for whole card
        addClassName("property-card");
        setPadding(false);
        setSpacing(false);
        setWidth("260px");
        setHeightFull();

        // --- Image Wrapper ---
        Div imageWrapper = new Div();
        imageWrapper.addClassName("image-wrapper");

        Image image = new Image(imageUrl, "Property image");
        image.addClassName("property-image");
        image.setWidthFull();

        Span imageCount = new Span("📷 " + photoCount);
        imageCount.addClassName("photo-count");

        imageWrapper.add(image, imageCount);

        // --- Text Info ---
        Span titleLabel = new Span(title);
        titleLabel.addClassName("title");

        Span priceSize = new Span(price + "   |   " + size);
        priceSize.addClassName("price-size");

        Span locationLabel = new Span(location);
        locationLabel.addClassName("location");

        Span statusLabel = new Span(status);
        statusLabel.addClassName("status");

        // Add components to card layout
        add(imageWrapper, titleLabel, priceSize, locationLabel, statusLabel);
    }
}
