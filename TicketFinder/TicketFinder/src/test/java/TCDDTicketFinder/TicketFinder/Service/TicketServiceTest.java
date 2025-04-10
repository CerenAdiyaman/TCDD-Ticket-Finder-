package TCDDTicketFinder.TicketFinder.Service;


import TCDDTicketFinder.TicketFinder.DTO.TicketDTO;
import TCDDTicketFinder.TicketFinder.DTO.TicketRequestBodyDTO;
import TCDDTicketFinder.TicketFinder.DTO.TicketRequestDTO;
import TCDDTicketFinder.TicketFinder.DTO.TicketResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        try {
            openMocks.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void get_request() {
        TicketRequestBodyDTO requestBody = new TicketRequestBodyDTO();
        requestBody.setDepartureStationId(93);
        requestBody.setDepartureStationName("ESKİŞEHİR");
        requestBody.setArrivalStationId(48);
        requestBody.setArrivalStationName("İSTANBUL(PENDİK)");
        requestBody.setDepartureDate(LocalDateTime.of(2025, 3, 20, 21, 0));

        TicketResponseDTO expectedResponse = new TicketResponseDTO(); // Example response


        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(TicketRequestDTO.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), any(String[].class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TicketResponseDTO.class)).thenReturn(expectedResponse);


        TicketResponseDTO actualResponse = ticketService.getRequest(requestBody);


        assertNotNull(actualResponse, "Response should not be null");
        assertEquals(expectedResponse, actualResponse, "Excepted response does not match actual response");

        verify(restClient).post();
        verify(requestBodyUriSpec).uri(anyString());
        verify(requestBodySpec).retrieve();
        verify(responseSpec).body(TicketResponseDTO.class);
    }


    @Test
    void get_Filtered_Train() {
        TicketRequestBodyDTO requestBody = new TicketRequestBodyDTO();
        requestBody.setDepartureStationId(1);
        requestBody.setArrivalStationId(2);
        requestBody.setCabinName("EKONOMİ");
        requestBody.setDepartureDate(LocalDateTime.of(2025, 4, 10, 10, 0));
        requestBody.setDepartureDateEnd(LocalDateTime.of(2025, 4, 10, 14, 0));

        TicketResponseDTO.TrainSegment segment = new TicketResponseDTO.TrainSegment(
                1, 2,
                LocalDateTime.of(2025, 4, 10, 11, 0),
                LocalDateTime.of(2025, 4, 10, 13, 0)
        );

        TicketResponseDTO.CabinClass cabinClass = new TicketResponseDTO.CabinClass(
                new TicketResponseDTO.CabinClassDetail(101, "EKONOMİ", "EKO"), 5
        );

        TicketResponseDTO.Train train = new TicketResponseDTO.Train(
                123, "123", "Ankara Ekspresi", "Ankara Ekspresi", "Express", "BlueLine", false, 1,
                1, 2,
                List.of(new TicketResponseDTO.AvailableFareInfo(List.of(cabinClass))),
                List.of(segment)
        );

        TicketResponseDTO.TrainAvailability availability = new TicketResponseDTO.TrainAvailability(
                List.of(train), 100, 99.99, false, false
        );

        TicketResponseDTO.TrainLeg leg = new TicketResponseDTO.TrainLeg(List.of(availability), 1);

        TicketResponseDTO response = new TicketResponseDTO();
        response.setTrainLegs(List.of(leg));

        List<TicketDTO> tickets = ticketService.getFilteredTrain(response, requestBody);

        assertEquals(1, tickets.size(), "expected 1 ticket, but got " + tickets.size());
        TicketDTO ticket = tickets.get(0);
        assertEquals("EKONOMİ", ticket.getCabinName());
        assertEquals("Ankara Ekspresi", ticket.getName());
        assertEquals(5, ticket.getAvailabilityCount());
        assertNotNull(ticket.getDepartureTime());

        System.out.println("Ticket Info:");
        System.out.println("Train: " + ticket.getName());
        System.out.println("Cabin: " + ticket.getCabinName());
        System.out.println("Available Seats: " + ticket.getAvailabilityCount());
        System.out.println("Departure Time: " + ticket.getDepartureTime());
    }

}
