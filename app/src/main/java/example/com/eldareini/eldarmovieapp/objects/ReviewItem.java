package example.com.eldareini.eldarmovieapp.objects;

public class ReviewItem {
    private String author, content;


    public ReviewItem(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
