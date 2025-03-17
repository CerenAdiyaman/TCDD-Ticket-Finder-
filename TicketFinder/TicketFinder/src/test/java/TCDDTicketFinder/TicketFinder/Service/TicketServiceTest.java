package TCDDTicketFinder.TicketFinder.Service;


import TCDDTicketFinder.TicketFinder.DTO.TicketDTO;
import TCDDTicketFinder.TicketFinder.DTO.TicketRequestBodyDTO;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private RestClient restClient;

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
    void getRequest_ShouldReturnTicketResponse() {
        // Arrange
        TicketRequestBodyDTO requestBody = new TicketRequestBodyDTO();
        requestBody.setDepartureStationId(93);
        requestBody.setDepartureStationName("ESKİŞEHİR");
        requestBody.setArrivalStationId(48);
        requestBody.setArrivalStationName("İSTANBUL(PENDİK)");
        requestBody.setDepartureDate(LocalDateTime.of(2025, 3, 20, 21, 0));

        TicketResponseDTO expectedResponse = new TicketResponseDTO(); // Örnek response
        when(restClient.post()).thenReturn(mock(RestClient.RequestBodyUriSpec.class));

        // Act
        TicketResponseDTO actualResponse = ticketService.getRequest(requestBody);

        // Assert
        assertNotNull(actualResponse);
    }

    @Test
    void getTrainAvailability_ShouldReturnAvailableTickets() {
        // Arrange
        TicketResponseDTO response = new TicketResponseDTO();
        response.setTrainLegs(Collections.emptyList()); // Boş response simülasyonu

        TicketRequestBodyDTO requestBody = new TicketRequestBodyDTO();
        requestBody.setDepartureDate(LocalDateTime.of(2025, 3, 20, 21, 0));
        requestBody.setDepartureDateEnd(LocalDateTime.of(2025, 3, 20, 23, 0));
        requestBody.setDepartureStationId(93);
        requestBody.setCabinName("EKONOMİ");

        // Act
        List<TicketDTO> availableTickets = ticketService.getFilteredTrain(response, requestBody);

        // Assert
        assertNotNull(availableTickets);
        assertTrue(availableTickets.isEmpty());
    }
}
