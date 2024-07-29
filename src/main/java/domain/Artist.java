package domain;

import jakarta.persistence.*;
import lombok.*;
import validator.ValidFestivalNumbersConstraint;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "artistId")
@Getter
@Setter
@Table(name = "artist")
@ValidFestivalNumbersConstraint
public class Artist {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;

    private String artistName;
}
