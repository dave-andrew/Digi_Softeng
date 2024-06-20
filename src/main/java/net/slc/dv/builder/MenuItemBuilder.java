package net.slc.dv.builder;

import java.util.function.Consumer;
import javafx.scene.control.MenuItem;

public class MenuItemBuilder {

    private MenuItem menuItem;

    private MenuItemBuilder() {
        this.menuItem = new MenuItem();
    }

    public static MenuItemBuilder create() {
        return new MenuItemBuilder();
    }

    public MenuItemBuilder setText(String text) {
        menuItem.setText(text);
        return this;
    }

    public MenuItemBuilder setStyleClass(String styleClass) {
        menuItem.getStyleClass().add(styleClass);

        return this;
    }

    public MenuItemBuilder setOnAction(Consumer<MenuItem> consumer) {
        menuItem.setOnAction(e -> consumer.accept(this.menuItem));
        return this;
    }

    public MenuItem build() {
        return menuItem;
    }
}
