package dto;

import domain.Festival;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FestivalDto {
    private Festival festival;
    private int ticketsBought;

}
