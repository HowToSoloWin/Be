package ru.com.alexsolo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.com.alexsolo.Dto.CreateTicketDto;

import ru.com.alexsolo.Dto.TicketDto;

import ru.com.alexsolo.Mail.MailSendler;
import ru.com.alexsolo.repository.UserRepository;
import ru.com.alexsolo.service.TicketService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/ticket")
public class TicketController {

    private UserRepository userRepository;
    private TicketService ticketService;
    private MailSendler mailSendler;

    @Autowired
    public TicketController(UserRepository userRepository, TicketService ticketService, MailSendler mailSendler) {
        this.userRepository = userRepository;
        this.ticketService = ticketService;
        this.mailSendler = mailSendler;
    }

    @GetMapping()
    public List<TicketDto> getTicket(Authentication authentication) throws MessagingException {
        return ticketService.getAllTicketDto(authentication);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTicket(@PathVariable long id, Authentication authentication){
        return ticketService.getSingleTicketDto(id,authentication);
    }

    @PostMapping("/create")
    public ResponseEntity createTiket(@Valid @RequestBody CreateTicketDto tiketDto, Authentication authentication){
       return ticketService.createTicket(tiketDto, authentication);
    }

    @PutMapping("/edit")
    public void editTicket(@Valid @RequestBody TicketDto ticketDto,BindingResult bindingResult, Authentication authentication){
            ticketService.editTicket(ticketDto, authentication);
    }

    @PutMapping("/{id}/submit")
    public ResponseEntity submit(@PathVariable long id, Authentication authentication){
        return ticketService.submit(id,authentication);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity approve(@PathVariable long id, Authentication authentication){
        return  ticketService.approve(id,authentication);
    }
    @PutMapping("/{id}/decline")
    public ResponseEntity decline(@PathVariable Long id, Authentication authentication){
        return  ticketService.decline(id,authentication);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity cancel(@PathVariable Long id, Authentication authentication) {
        return  ticketService.cancel(id,authentication);
    }

    @PutMapping("/{id}/assign_to_me")
    public ResponseEntity assign(@PathVariable Long id, Authentication authentication) {
        return  ticketService.assign(id,authentication);
    }

    @PutMapping(value = "/{id}/done")
    public ResponseEntity done(@PathVariable Long id, Authentication authentication) {
       return ticketService.done(id,authentication);
    }
}
