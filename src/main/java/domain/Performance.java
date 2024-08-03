package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import validator.ValidFestivalNumber1;
import validator.ValidFestivalNumber2RangeConstraint;
import validator.ValidPerformanceStartDateTime;

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
@ValidFestivalNumber2RangeConstraint(maxDifference = 1000)
public class Performance {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long performanceId;

    @NotNull(message = "{performance.artistName.notNull}")
    @NotBlank(message = "{performance.artistName.notBlank}")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "{performance.artistName.pattern}")
    private String artistName;

    @ValidPerformanceStartDateTime(start = "10:00", end = "23:00")
    @NotNull(message = "{performance.startDateTime.notNull}")
    private LocalDateTime startDateTime;

    @NotNull(message = "{performance.endDateTime.notNull}")
    private LocalDateTime endDateTime;

    // TODO double check how it is stored
    @ManyToMany
    @JoinTable(name = "performance_sub_genre", joinColumns = @JoinColumn(name = "performanceId"), inverseJoinColumns = @JoinColumn(name = "subGenreId"))
    @Size(max = 2, message = "{performance.subGenres.size}")
    private Set<SubGenre> subGenres;

    @ValidFestivalNumber1
    @Range(min = 1000, max = 9999, message = "{performance.festivalNumber1.range}")
    private int festivalNumber1;

    private int festivalNumber2;

    @ManyToOne
    @JoinColumn(name = "festivalId", nullable = false)
    private Festival festival;
}