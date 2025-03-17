package TCDDTicketFinder.TicketFinder.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDTO {
    private String name;
    private String cabinName;
    private int availabilityCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime departureTime;
}
