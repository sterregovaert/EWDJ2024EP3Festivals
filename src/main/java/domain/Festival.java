package domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "festivalId")
@Getter
@Setter
@Table(name = "festival")
public class Festival {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festivalId;

    private LocalDateTime startDateTime;
    private String region;
    private String genre;
    private int availableSeats;
    private double ticketPrice;
//    private List<Performance> performances;

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;
}
