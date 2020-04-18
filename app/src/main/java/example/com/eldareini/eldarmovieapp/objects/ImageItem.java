package example.com.eldareini.eldarmovieapp.objects;

public class ImageItem {
    private String image;
    private boolean isSelected;

    public ImageItem(String image, boolean isSelected) {
        this.image = image;
        this.isSelected = isSelected;
    }

    public ImageItem(String image) {
        this.image = image;
        this.isSelected = false;
    }

    public String getImage() {
        return image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
