package ru.com.alexsolo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.com.alexsolo.Dto.FeedbackDto;
import ru.com.alexsolo.Enum.State;
import ru.com.alexsolo.domain.Feedback;
import ru.com.alexsolo.domain.Ticket;
import ru.com.alexsolo.domain.User;
import ru.com.alexsolo.repository.FeedbackRepository;
import ru.com.alexsolo.repository.TicketRepository;
import ru.com.alexsolo.repository.UserRepository;

import javax.persistence.NoResultException;
import java.sql.Timestamp;

@Service
public class FeedbackService {

    private UserRepository userRepository;
    private TicketRepository ticketRepository;
    private FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(UserRepository userRepository, TicketRepository ticketRepository, FeedbackRepository feedbackRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public ResponseEntity addFeedback(FeedbackDto feedbackDto, Authentication authentication){
        User user = userRepository.getUser(authentication.getName());
        Ticket ticket = null;
        try {
            ticket = ticketRepository.getTicket(feedbackDto.getTicketId());
        }catch (NoResultException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Feedback feedback = null;
        try {
            feedback = feedbackRepository.getFeedback(feedbackDto.getTicketId());
        }catch (NoResultException e) {

        }

        if (feedback != null) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        if (!ticket.getOwner().equals(user) || ticket.getState()!= State.Done) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        feedbackRepository.addFeedback(new Feedback(user,feedbackDto.getRate(),new Timestamp(System.currentTimeMillis()),feedbackDto.getText(),ticket));

        return ResponseEntity.ok().build();
    }

    public ResponseEntity getFeedback(long id){
        Feedback feedback;
        try {
            feedback = feedbackRepository.getFeedback(id);
        }catch (NoResultException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        FeedbackDto feedbackDto = new FeedbackDto(feedback.getTicket().getName(), feedback.getRate(), feedback.getText(), feedback.getTicket().getId());
        return ResponseEntity.ok(feedbackDto);
    }


}
