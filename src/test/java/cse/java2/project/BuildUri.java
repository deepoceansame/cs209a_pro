package cse.java2.project;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

public class BuildUri {
    public final static String BASE_URI = "https://api.stackexchange.com/2.3/questions";

    public static void main(String[] args) {
        URI uri = UriComponentsBuilder.fromUriString(BASE_URI).build().toUri();
        System.out.println(uri);
        URI uri1 = UriComponentsBuilder.fromUri(uri).path("/"+123L).path("/answers").build().toUri();
        System.out.println(uri1);
    }
}
