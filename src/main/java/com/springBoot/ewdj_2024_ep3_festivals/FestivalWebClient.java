//package com.springBoot.ewdj_2024_ep3_festivals;
//
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@Component
//public class FestivalWebClient {
//
//    private final WebClient webClient;
//
//    public FestivalWebClient(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api").build();
//    }
//
//    public Mono<List<String>> getArtistsByFestival(Long festivalId) {
//        return this.webClient.get().uri("/festivals/{festivalId}/artists", festivalId).retrieve().bodyToMono(new ParameterizedTypeReference<List<String>>() {
//        });
//    }
//
//    public Mono<List<String>> getFestivalsByGenre(String genre) {
//        return this.webClient.get().uri(uriBuilder -> uriBuilder.path("/festivals").queryParam("genre", genre).build()).retrieve().bodyToMono(new ParameterizedTypeReference<List<String>>() {
//        });
//    }
//}