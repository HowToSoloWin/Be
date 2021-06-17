package ru.com.alexsolo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.com.alexsolo.Enum.Role;
import ru.com.alexsolo.Enum.State;
import ru.com.alexsolo.converter.ConverterHistory;
import ru.com.alexsolo.domain.Ticket;
import ru.com.alexsolo.domain.User;
import ru.com.alexsolo.repository.HistoryRepository;
import ru.com.alexsolo.repository.TicketRepository;
import ru.com.alexsolo.repository.UserRepository;

import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.Map;

@Service
public class HistoryService {

    private ConverterHistory converterHistory;
    private HistoryRepository historyRepository;
    private TicketRepository ticketRepository;
    private UserRepository userRepository;

    @Autowired
    public HistoryService(ConverterHistory converterHistory, HistoryRepository historyRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.converterHistory = converterHistory;
        this.historyRepository = historyRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity getHistory(long id, Authentication authentication){
        Ticket ticket;
        try {
            ticket = ticketRepository.getTicket(id);
        }catch (NoResultException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        User user = userRepository.getUser(authentication.getName());
        if (!forbidden(user,ticket)){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        Map<Object,Object> obj = new HashMap<>();
        obj.put("history",(converterHistory.converter(historyRepository.getLastFiveHistory(id))));
        obj.put("count",historyRepository.getAllCountHistory(id));

        return ResponseEntity.ok(obj);
    }

    public ResponseEntity getAllHistory(long id, Authentication authentication){
        Ticket ticket;
        try {
            ticket = ticketRepository.getTicket(id);
        }catch (NoResultException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        User user = userRepository.getUser(authentication.getName());
        if (!forbidden(user,ticket)){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        Map<Object,Object> obj = new HashMap<>();
        obj.put("history",(converterHistory.converter(historyRepository.getAllHistory(id))));
        obj.put("count",historyRepository.getAllCountHistory(id));

        return ResponseEntity.ok(obj);
    }

    private boolean forbidden(User user, Ticket ticket){
        if (user.getRole() == Role.Employee){
            if (ticket.getOwner().equals(user)){
                return true;
            }else {
                return false;
            }
        }if(user.getRole() == Role.Manager) {
            if (ticket.getOwner().equals(user) || (ticket.getOwner().getRole() == Role.Employee && ticket.getState() == State.New || ticket.getOwner().getRole() == Role.Manager && ticket.getState() == State.New|| ticket.getApproved().equals(user)) ){
                return true;
            }else {
                return false;
            }
        }if (user.getRole() == Role.Engineer){
            if (ticket.getState() == State.Approved || ticket.getAssignee().equals(user)){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
}
