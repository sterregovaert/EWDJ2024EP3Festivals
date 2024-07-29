package perform;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class PerformRestFestival {

    // base URL of the server that this client will interact with
    private final String SERVER_URI = "http://localhost:8080/api";
    // send HTTP requests and receive responses
    private final WebClient webClient = WebClient.create();

    public PerformRestFestival() {
        System.out.println("\n---- ---- GET ARTISTS BY FESTIVAL ---- ----");
        getArtistsByFestival(1L);
        System.out.println("---- ---- ---- ---- ---- ---- ---- ----");

        System.out.println("\n---- ---- GET FESTIVALS BY GENRE ---- ----");
        getFestivalsByGenre("Rock");
        System.out.println("---- ---- ---- ---- ---- ---- ---- ----");
    }

    public static void main(String[] args) throws Exception {
        new PerformRestFestival();
    }

    private void getArtistsByFestival(Long festivalId) {
        webClient.get().uri(SERVER_URI + "/festival/" + festivalId + "/artists").retrieve().bodyToFlux(String.class).collectList().doOnNext(this::printList).block();
    }

    private void getFestivalsByGenre(String genre) {
        webClient.get().uri(uriBuilder -> uriBuilder.path(SERVER_URI + "/festivals").queryParam("genre", genre).build()).retrieve().bodyToFlux(String.class).collectList().doOnNext(this::printList).block();
    }

    private void printList(List<String> list) {
        list.forEach(System.out::println);
    }
}
