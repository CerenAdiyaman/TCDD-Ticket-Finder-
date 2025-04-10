package TCDDTicketFinder.TicketFinder.Controller;


import TCDDTicketFinder.TicketFinder.DTO.TicketDTO;
import TCDDTicketFinder.TicketFinder.DTO.TicketRequestBodyDTO;
import TCDDTicketFinder.TicketFinder.DTO.TicketResponseDTO;
import TCDDTicketFinder.TicketFinder.Service.EmailSenderService;
import TCDDTicketFinder.TicketFinder.Service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final EmailSenderService emailSenderService;

    @GetMapping("/get")
    public ResponseEntity<TicketResponseDTO> getTicket(@RequestBody TicketRequestBodyDTO ticketRequestBody) {
        TicketResponseDTO response = ticketService.getRequest(ticketRequestBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getFilteredTickets")
    public ResponseEntity<List<TicketDTO>> getFilteredTickets(@RequestBody TicketRequestBodyDTO requestBody){
        List<TicketDTO> tickets = ticketService.getAvailableTickets(requestBody);
        emailSenderService.sendTicketAvailabilityEmails(tickets);
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Get paginated tickets", description = "Get paginated tickets based on the request body and pageable parameters.")
    @PostMapping("/getFilteredPaginatedTickets")
    public ResponseEntity<Page<TicketDTO>> getFilteredPaginatedTickets(
            @RequestBody TicketRequestBodyDTO requestBody,
            Pageable pageable) {

        Page<TicketDTO> paginatedTickets = ticketService.getPaginatedTickets(requestBody, pageable);
        emailSenderService.sendTicketAvailabilityEmails(paginatedTickets.getContent()); // sadece bu sayfaya ait biletleri gönder

        return ResponseEntity.ok(paginatedTickets);
    }


}
