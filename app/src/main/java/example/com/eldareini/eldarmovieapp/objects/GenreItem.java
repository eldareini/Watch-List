package example.com.eldareini.eldarmovieapp.objects;

public class GenreItem {
    private int id;
    private String genre;

    public GenreItem(int id, String genre) {
        this.id = id;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }
}
