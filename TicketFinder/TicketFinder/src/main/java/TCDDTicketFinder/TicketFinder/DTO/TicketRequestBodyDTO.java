package TCDDTicketFinder.TicketFinder.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketRequestBodyDTO {

    private int departureStationId;
    private String departureStationName;
    private int arrivalStationId;
    private String arrivalStationName;
    private String cabinName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime departureDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime departureDateEnd;

}
