package TCDDTicketFinder.TicketFinder.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class TicketRequestDTO {

    private String searchType = "DOMESTIC";
    private boolean searchReservation = false;
    List<PassengerTypeCounts> passengerTypeCounts;
    List<SearchRoutes> searchRoutes;

    public static record PassengerTypeCounts(int id, int count){
    }

    public static record SearchRoutes(int departureStationId,
                                      String departureStationName,
                                      int arrivalStationId,
                                      String arrivalStationName,
                                      @JsonFormat(shape= JsonFormat.Shape.STRING, pattern= "dd-MM-yyyy HH:mm:ss") java.time.LocalDateTime departureDate){
    }
}
