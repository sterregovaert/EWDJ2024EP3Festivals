package perform;

import org.springframework.web.reactive.function.client.WebClient;

/*
 * overview of freeSeats via json of a festival
 * overview of festivals of a given sport is returned
 * */
public class PerformRestFestival {

    // base URL of the server that this client will interact with
    private final String SERVER_URI = "http://localhost:8080/festival";
    // send HTTP requests and receive responses
    private WebClient webClient = WebClient.create();

    public PerformRestFestival() {
//        getFestivalsBySport(1L);

        getFreeSeatsOfFestival(1);
    }

//    private void getFestivalsBySport(Long sport_id) {
//        webClient.get().uri(SERVER_URI + "/sport/" + sport_id).retrieve().bodyToFlux(Festival.class).doOnNext(this::printFestivalData).blockLast();
//    }

    private void getFreeSeatsOfFestival(int festival_id) {
        webClient.get().uri(SERVER_URI + "/" + festival_id + "/freeSeats").retrieve().bodyToMono(Integer.class).doOnSuccess(this::printFreeSeats).block();
    }

//    private void printFestivalData(Festival festival) {
//        System.out.printf("ID=%s, Name=%s, Sport=%s%n", festival.getFestival_id(), festival.getSport().getName());
//    }

    private void printFreeSeats(Integer freeSeats) {
        System.out.printf("Free Seats=%s%n", freeSeats);
    }

}
