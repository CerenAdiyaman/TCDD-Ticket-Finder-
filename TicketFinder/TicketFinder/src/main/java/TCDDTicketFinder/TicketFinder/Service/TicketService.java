package TCDDTicketFinder.TicketFinder.Service;


import TCDDTicketFinder.TicketFinder.DTO.TicketDTO;
import TCDDTicketFinder.TicketFinder.DTO.TicketRequestBodyDTO;
import TCDDTicketFinder.TicketFinder.DTO.TicketRequestDTO;
import TCDDTicketFinder.TicketFinder.DTO.TicketResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final RestClient restClient;

    public TicketResponseDTO getRequest(TicketRequestBodyDTO ticketRequestBody){
        TicketRequestDTO ticketRequest = new TicketRequestDTO();

        ticketRequest.setSearchRoutes(List.of(new TicketRequestDTO.SearchRoutes(
                ticketRequestBody.getDepartureStationId(),
                ticketRequestBody.getDepartureStationName(),
                ticketRequestBody.getArrivalStationId(),
                ticketRequestBody.getArrivalStationName(),
                ticketRequestBody.getDepartureDate())));
        ticketRequest.setPassengerTypeCounts(List.of(new TicketRequestDTO.PassengerTypeCounts(0, 1)));
        ticketRequest.setSearchReservation(false);
        ticketRequest.setSearchType("DOMESTIC");

        String url = "https://web-api-prod-ytp.tcddtasimacilik.gov.tr/tms/train/train-availability?environment=dev&userId=1";
        TicketResponseDTO response = restClient.post()
                .uri(url)
                .body(ticketRequest)
                .header("unit-id", "3895")
                .header("Authorization", "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlVFFicDhDMmpiakp1cnUzQVk2a0ZnV196U29MQXZIMmJ5bTJ2OUg5THhRIn0.eyJleHAiOjE3MjEzODQ0NzAsImlhdCI6MTcyMTM4NDQxMCwianRpIjoiYWFlNjVkNzgtNmRkZS00ZGY4LWEwZWYtYjRkNzZiYjZlODNjIiwiaXNzIjoiaHR0cDovL3l0cC1wcm9kLW1hc3RlcjEudGNkZHRhc2ltYWNpbGlrLmdvdi50cjo4MDgwL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMDAzNDI3MmMtNTc2Yi00OTBlLWJhOTgtNTFkMzc1NWNhYjA3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidG1zIiwic2Vzc2lvbl9zdGF0ZSI6IjAwYzM4NTJiLTg1YjEtNDMxNS04OGIwLWQ0MWMxMTcyYzA0MSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsInNpZCI6IjAwYzM4NTJiLTg1YjEtNDMxNS04OGIwLWQ0MWMxMTcyYzA0MSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoid2ViIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AIW_4Qws2wfwxyVg8dgHRT9jB3qNavob2C4mEQIQGl3urzW2jALPx-e51ZwHUb-TXB-X2RPHakonxKnWG6tDIP5aKhiidzXDcr6pDDoYU5DnQhMg1kywyOaMXsjLFjuYN5PAyGUMh6YSOVsg1PzNh-5GrJF44pS47JnB9zk03Pr08napjsZPoRB-5N4GQ49cnx7ePC82Y7YIc-gTew2baqKQPz9_v381Gbm2V38PZDH9KldlcWut7kqQYJFMJ7dkM_entPJn9lFk7R5h5j_06OlQEpWRMQTn9SQ1AYxxmZxBu5XYMKDkn4rzIIVCkdTPJNCt5PvjENjClKFeUA1DOg")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .retrieve()
                .body(TicketResponseDTO.class);
        return response;
    }

    public List<TicketDTO> getFilteredTrain(TicketResponseDTO response, TicketRequestBodyDTO requestBody) {
        List<TicketDTO> availableTickets = new ArrayList<>();
        response.getTrainLegs().forEach(trainLeg ->
                trainLeg.trainAvailabilities().forEach(trainAvailability ->
                        trainAvailability.trains().forEach(train ->
                                train.availableFareInfo().forEach(fareInfo ->
                                        fareInfo.cabinClasses().forEach(cabinClass -> {
                                            train.trainSegments().forEach(segment -> {
                                                if (!segment.departureTime().isBefore(requestBody.getDepartureDate()) &&
                                                        !segment.departureTime().isAfter(requestBody.getDepartureDateEnd()) && segment.departureStationId() == requestBody.getDepartureStationId()){
                                                    TicketDTO ticket = new TicketDTO();
                                                    ticket.setName(train.commercialName());
                                                    ticket.setCabinName(cabinClass.cabinClass().name());
                                                    ticket.setAvailabilityCount(cabinClass.availabilityCount());
                                                    ZonedDateTime departureTimeInTurkey = segment.departureTime().atZone(ZoneId.of("Europe/Istanbul"));
                                                    ticket.setDepartureTime(departureTimeInTurkey.toLocalDateTime());
                                                    if (requestBody.getCabinName() == null || requestBody.getCabinName().isEmpty() || requestBody.getCabinName().equals(ticket.getCabinName())) {
                                                        availableTickets.add(ticket);
                                                    }
                                                }
                                            });
                                        })
                                )
                        )
                )
        );

        return availableTickets;
    }
}