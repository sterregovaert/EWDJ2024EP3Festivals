package domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "genreId")
@Getter
@Setter
@Table(name = "genre")
@ToString(exclude = { "performances", "region", "festivals", "tickets" })
public class Genre {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;

    private String name;

    @OneToMany(mappedBy = "genre")
    private Set<Festival> festivals;

    @OneToMany(mappedBy = "genre")
    private Set<SubGenre> subGenres;
}
