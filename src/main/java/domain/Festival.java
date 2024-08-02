package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import utils.LocalDateTimeDeserializer;
import utils.LocalDateTimeSerializer;
import utils.TicketPriceDeserializer;
import utils.TicketPriceSerializer;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "festivalId")
@Getter
@Setter
@JsonPropertyOrder({"festivalId", "name", "startDateTime", "region", "genre", "availablePlaces", "performances", "ticketPrice", "tickets"})
@Table(name = "festival")
public class Festival {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonProperty("festivalId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festivalId;

    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDateTime;

    private int availablePlaces;

    @JsonSerialize(using = TicketPriceSerializer.class)
    @JsonDeserialize(using = TicketPriceDeserializer.class)
    private double ticketPrice;

    @JsonIgnore
    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Performance> performances;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "regionId")
    private Region region;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "genreId")
    private Genre genre;

    @JsonIgnore
    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

}
