package TCDDTicketFinder.TicketFinder.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TicketResponseDTO {
        private List<TrainLeg> trainLegs;
        private Integer legCount;
        private Integer roundTripDiscount;
        private Long maxRegionalTrainsRoundTripDays;
        public record TrainLeg(List<TrainAvailability> trainAvailabilities, int resultCount) {
        }
        public record TrainAvailability(List<Train> trains, int totalTripTime, double minPrice, boolean connection, boolean dayChanged) {
        }
        public record Train(int id, String number, String name, String commercialName, String type, String line, boolean reversed, int scheduleId,
                            int departureStationId, int arrivalStationId, List<AvailableFareInfo> availableFareInfo, List<TrainSegment> trainSegments) {
        }
        public record AvailableFareInfo(List<CabinClass> cabinClasses) {
        }
        public record TrainSegment(int departureStationId, int arrivalStationId, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        }
        public record CabinClass(CabinClassDetail cabinClass, int availabilityCount) {
        }
        public record CabinClassDetail(int id, String name, String code) {
        }
}
