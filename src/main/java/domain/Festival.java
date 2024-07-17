package domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id")
@Getter
@Setter
@Table(name = "festival")
public class Festival {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int availableSeats;
}
