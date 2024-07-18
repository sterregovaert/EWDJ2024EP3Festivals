package domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "performanceId")
@Getter
@Setter
@Table(name = "performance")
public class Performance {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long performanceId;

    private String artistName;
    private LocalDateTime startDateTime;
    private int endDateTime;

    @ManyToOne
    @JoinColumn(name = "festivalId", nullable = false)
    private Festival festival;

}
