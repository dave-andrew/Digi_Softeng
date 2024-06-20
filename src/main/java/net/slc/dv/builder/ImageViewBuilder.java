package net.slc.dv.builder;

import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageViewBuilder {
    private final ImageView imageView;

    private ImageViewBuilder(ImageView imageView) {
        this.imageView = imageView;
    }

    private ImageViewBuilder() {
        this.imageView = new ImageView();
    }

    public static ImageViewBuilder create() {
        return new ImageViewBuilder();
    }

    public static ImageViewBuilder modify(ImageView imageView) {
        return new ImageViewBuilder(imageView);
    }

    public ImageViewBuilder setImage(Image image) {
        this.imageView.setImage(image);

        return this;
    }

    public ImageViewBuilder setX(double x) {
        this.imageView.setX(x);

        return this;
    }

    public ImageViewBuilder setFitWidth(double width) {
        this.imageView.setFitWidth(width);

        return this;
    }

    public ImageViewBuilder setFitHeight(double height) {
        this.imageView.setFitHeight(height);

        return this;
    }

    public ImageViewBuilder setPreserveRatio(boolean preserveRatio) {
        this.imageView.setPreserveRatio(preserveRatio);

        return this;
    }

    public ImageViewBuilder bindImageProperty(ObjectProperty<Image> ObjectP) {
        this.imageView.imageProperty().bind(ObjectP);

        return this;
    }

    public ImageView build() {
        return this.imageView;
    }
}
