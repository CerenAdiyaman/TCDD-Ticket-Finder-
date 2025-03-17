package TCDDTicketFinder.TicketFinder.Controller;


import TCDDTicketFinder.TicketFinder.DTO.TicketDTO;
import TCDDTicketFinder.TicketFinder.DTO.TicketRequestBodyDTO;
import TCDDTicketFinder.TicketFinder.DTO.TicketResponseDTO;
import TCDDTicketFinder.TicketFinder.Service.EmailSenderService;
import TCDDTicketFinder.TicketFinder.Service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final EmailSenderService emailSenderService;

    @GetMapping("/get")
    public ResponseEntity<TicketResponseDTO> getBilet(@RequestBody TicketRequestBodyDTO ticketRequestBody) {
        TicketResponseDTO response = ticketService.getRequest(ticketRequestBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getFilteredTickets")
    public ResponseEntity<List<TicketDTO>> getFilteredTickets(@RequestBody TicketRequestBodyDTO requestBody){
        TicketResponseDTO response = ticketService.getRequest(requestBody);
        List<TicketDTO> tickets = ticketService.getFilteredTrain(response, requestBody); // Getting available tickets
        emailSenderService.sendTicketAvailabilityEmails(tickets);

        return ResponseEntity.ok(tickets);
    }


}
