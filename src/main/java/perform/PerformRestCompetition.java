package perform;

import org.springframework.web.reactive.function.client.WebClient;

/*
 * overview of freeSeats via json of a competition
 * overview of competitions of a given sport is returned
 * */
public class PerformRestCompetition {

    // base URL of the server that this client will interact with
    private final String SERVER_URI = "http://localhost:8080/competition";
    // send HTTP requests and receive responses
    private WebClient webClient = WebClient.create();

    public PerformRestCompetition() {
        getCompetitionsBySport(1L);

        getFreeSeatsOfCompetition(1);
    }

    private void getCompetitionsBySport(Long sport_id) {
        webClient.get().uri(SERVER_URI + "/sport/" + sport_id).retrieve().bodyToFlux(Competition.class).doOnNext(this::printCompetitionData).blockLast();
    }

    private void getFreeSeatsOfCompetition(int competition_id) {
        webClient.get().uri(SERVER_URI + "/" + competition_id + "/freeSeats").retrieve().bodyToMono(Integer.class).doOnSuccess(this::printFreeSeats).block();
    }

    private void printCompetitionData(Competition competition) {
        System.out.printf("ID=%s, Name=%s, Sport=%s%n", competition.getCompetition_id(), competition.getSport().getName());
    }

    private void printFreeSeats(Integer freeSeats) {
        System.out.printf("Free Seats=%s%n", freeSeats);
    }

}
