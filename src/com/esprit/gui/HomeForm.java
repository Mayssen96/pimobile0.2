package com.esprit.gui;

import com.codename1.components.ImageViewer;
import com.codename1.db.Cursor;
import com.codename1.db.Row;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.esprit.entities.Blog;
import com.esprit.entities.Rate;
import com.esprit.services.ServiceBlog;
//import com.esprit.services.ServiceRate;
import java.io.IOException;
import java.util.List;
import com.codename1.ui.FontImage;
import com.codename1.ui.plaf.Style;

public class HomeForm extends BaseForm { // Extend Form instead of BaseForm

    private TextField searchField;
    private List<Blog> blogs;
    ServiceBlog sb = new ServiceBlog();

    public HomeForm() {
        super("Home", BoxLayout.y());
        createGUI();
        addActions();
//        setBackgroundImage();
    }

    private void createGUI() {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        searchField = new TextField();
        searchField.setHint("Search");
        searchField.setUIID("SearchField"); // Apply custom UIID to the search field

        add(searchField);

        blogs = sb.afficher();
        for (Blog blog : blogs) {
            addItem(blog);
        }
    }

    private void addItem(Blog blog) {
        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        Label lblNom = new Label("Nom Utilisateur: " + blog.getNomUtilisateur());
        Label lblTitre = new Label("Titre: " + blog.getTitre());
        Label lblDescription = new Label("Description: " + blog.getDescription());

        Container buttonsContainer = new Container(new FlowLayout(Component.CENTER));

        Button updateButton = new Button(FontImage.createMaterial(FontImage.MATERIAL_EDIT, "Label", 4));
        
        updateButton.addActionListener(e -> {
            UpdateBlogForm updateForm = new UpdateBlogForm(blog);
            updateForm.show();
        });
        // Create Font Awesome delete icon
        Button deleteButton = new Button(FontImage.createMaterial(FontImage.MATERIAL_DELETE, "Label", 4));
        deleteButton.addActionListener(e -> {
            boolean deleteConfirmation = Dialog.show("Confirmation", "Voulez-vous supprimer ce blog ?", "Oui", "Non");
            if (deleteConfirmation) {
                ServiceBlog sb = new ServiceBlog();
                boolean deleteResult = sb.supprimer(blog);
                if (deleteResult) {
                    Dialog.show("Succès", "Le blog a été supprimé avec succès.", "OK", null);
                    container.remove();
                    revalidate();
                } else {
                    Dialog.show("Erreur", "Échec de la suppression du blog.", "OK", null);
                }
            }
        });

        buttonsContainer.addAll(deleteButton,updateButton);
        container.addAll(lblNom, lblTitre, lblDescription, buttonsContainer);
        container.setUIID("BlogContainer"); // Apply custom UIID to the blog container

        add(container);
    }

    private void addActions() {
        searchField.addDataChangedListener((type, index) -> {
            String searchText = searchField.getText();
            filterBlogs(searchText);
        });
    }

    private void filterBlogs(String searchText) {
        removeAll();

        for (Blog blog : blogs) {
            if (blog.getTitre().toLowerCase().startsWith(searchText.toLowerCase())) {
                addItem(blog);
            }
        }

        revalidate();
    }
//
//    private void setBackgroundImage() {
//        try {
//            Image backgroundImage = Image.createImage("http://localhost/Pic/Blog.jpg"); // Replace with the path to your background image
//            getStyle().setBgImage(backgroundImage);
//            getStyle().setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
//        } catch (IOException ex) {
//                 System.out.println(ex.getMessage());
//        }
//    }
}
