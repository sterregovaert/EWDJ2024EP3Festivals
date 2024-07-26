package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import validator.ValidFestivalNumbersConstraint;
import validator.ValidPerformanceTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "performanceId")
@Getter
@Setter
@Table(name = "performance")
@ValidFestivalNumbersConstraint
@ValidPerformanceTime(message = "Start time must be between 10:00 and 23:00 with minutes as 00, and not overlap with other performances")
public class Performance {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long performanceId;

    @NotNull(message = "Name can not be empty")
    @NotBlank(message = "Name can not be empty")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name should only exist out of letters or spaces, for example “The Rolling Stones”")
    private String artistName;


    @NotNull(message = "Start date/time cannot be empty")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date/time cannot be empty")
    private LocalDateTime endDateTime;

    @ManyToMany
    @JoinTable(
            name = "performance_sub_genre",
            joinColumns = @JoinColumn(name = "performanceId"),
            inverseJoinColumns = @JoinColumn(name = "subGenreId")
    )
    @Size(max = 2, message = "Maximum 2 sub-genres are allowed")
    private Set<SubGenre> subGenres;

    @Range(min = 1000, max = 9999, message = "Festival number must be between 1000 and 9999")
    private int festivalNumber1;

    private int festivalNumber2;

    @ManyToOne
    @JoinColumn(name = "festivalId", nullable = false)
    private Festival festival;

}
