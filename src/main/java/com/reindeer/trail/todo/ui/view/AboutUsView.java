package com.reindeer.trail.todo.ui.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("about")
@PermitAll
public class AboutUsView extends VerticalLayout {

    public AboutUsView() {
        setSpacing(true);
        setPadding(true);

        H1 heading = new H1("About Us");
        heading.getStyle().set("font-size", "36px");

        Div content = new Div();
        content.getStyle().set("font-size", "18px").set("line-height", "1.7");

        content.getElement().setProperty("innerHTML",
                "<p>This project is proudly developed as part of the <strong>MIT-WPU initiative</strong> for the <strong>MPJ (Mini Project Java)</strong> subject.</p>" +
                "<p>Our primary goal was to build a dynamic and user-friendly <strong>Property Listing Application</strong> that allows users to seamlessly browse, add, and manage property listings.</p>" +
                "<p><strong>We used the following technologies:</strong></p>" +
                "<ul>" +
                "<li><strong>Java</strong> for the core development</li>" +
                "<li><strong>SQL Database</strong> for robust and secure backend data storage</li>" +
                "<li><strong>Vaadin Java Framework</strong> for building a responsive and modern web UI</li>" +
                "</ul>" +
                "<p>This project is a hands-on demonstration of full-stack development using Java and related tools. Every line of code reflects our learning, teamwork, and dedication to crafting something functional and polished.</p>" +
                "<hr style='margin: 24px 0;'/>" +
                "<h2 style='font-size: 24px; font-weight: bold;'>👨‍💻 Group Members</h2>" +

                "<p style='font-size: 20px; font-weight: bold;'>1. Atharv Mahajan</p>" +
                "<p><strong>Role:</strong> 🛠️ UI Development & Backend Integration<br>" +
                "<strong>Roll No:</strong> 📘 55<br>" +
                "<strong>PRN:</strong> 🧾 1032230892</p>" +

                "<p style='font-size: 20px; font-weight: bold;'>2. Ronak</p>" +
                "<p><strong>Roll No:</strong> 📘 47<br>" +
                "<strong>PRN:</strong> 🧾 1032222803</p>" +

                "<p style='font-size: 20px; font-weight: bold;'>3. Shreya Shinde</p>" +
                "<p><strong>Roll No:</strong> 📘 49<br>" +
                "<strong>PRN:</strong> 🧾 1032222814</p>" +

                "<p style='font-size: 20px; font-weight: bold;'>4. Mahek Sayyad</p>" +
                "<p><strong>Roll No:</strong> 📘 50<br>" +
                "<strong>PRN:</strong> 🧾 1032230302</p>"
        );

        add(heading, content);
    }
}
