package TCDDTicketFinder.TicketFinder.Service;


import TCDDTicketFinder.TicketFinder.DTO.TicketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cerenadiyamn@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);

    }

    public void sendTicketAvailabilityEmails(List<TicketDTO> tickets) {
        StringBuilder emailContent = new StringBuilder("Available Tickets:\n\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        tickets.stream()
                .filter(ticket -> ticket.getAvailabilityCount() > 0)
                .forEach(ticket -> {
                    emailContent.append("Ticket Details:\n")
                            .append("Name: ").append(ticket.getName()).append("\n")
                            .append("Cabin: ").append(ticket.getCabinName()).append("\n")
                            .append("Availability: ").append(ticket.getAvailabilityCount()).append("\n")
                            .append("Departure Time: ").append(ticket.getDepartureTime().format(formatter)).append("\n\n");
                });

        if (emailContent.length() > "Available Tickets:\n\n".length()) {
            sendEmail("cerenadiyamn@gmail.com", "Available Tickets", emailContent.toString());
        }
    }
}