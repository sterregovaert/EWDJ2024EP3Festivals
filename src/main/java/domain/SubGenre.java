package domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "subGenreId")
@Getter
@Setter
@Table(name = "sub_genre")
public class SubGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subGenreId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "genreId", nullable = false)
    private Genre genre;

    @ManyToMany(mappedBy = "subGenres")
    private Set<Performance> performances;
}