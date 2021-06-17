package ru.com.alexsolo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.context.Context;
import ru.com.alexsolo.Dto.CreateTicketDto;
import ru.com.alexsolo.Dto.TicketDto;
import ru.com.alexsolo.Enum.Role;
import ru.com.alexsolo.Enum.State;
import ru.com.alexsolo.Mail.MailSendler;
import ru.com.alexsolo.converter.ConverterComment;
import ru.com.alexsolo.converter.ConverterTiket;
import ru.com.alexsolo.domain.Category;
import ru.com.alexsolo.domain.Ticket;
import ru.com.alexsolo.domain.User;
import ru.com.alexsolo.repository.*;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Validated
@Service
public class TicketService {

    private HistoryRepository    historyRepository;
    private TicketRepository     ticketRepository;
    private UserRepository       userRepository;
    private ConverterTiket       converterTiket;
    private CategoryRepository   categoryRepository;
    private CommentRepository    commentRepository;
    private ConverterComment     converterComment;
    private MailSendler          mailSendler;

    @Autowired
    public TicketService(HistoryRepository historyRepository, TicketRepository ticketRepository, UserRepository userRepository,
                         ConverterTiket converterTiket, CategoryRepository categoryRepository, CommentRepository commentRepository,
                         ConverterComment converterComment, MailSendler mailSendler) {
        this.historyRepository = historyRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.converterTiket = converterTiket;
        this.categoryRepository = categoryRepository;
        this.commentRepository = commentRepository;
        this.converterComment = converterComment;
        this.mailSendler = mailSendler;
    }

    public List<TicketDto> getAllTicketDto(Authentication authentication) {
        User user = userRepository.getUser(authentication.getName());

        if (user.getRole().equals(Role.Employee)) {
            return converterTiket.ticketToDtoEmployee(ticketRepository.getTicketsForRoleEmployee(user));
        }
        if (user.getRole().equals(Role.Manager)) {
            return converterTiket.ticketToDtoManager(ticketRepository.getTicketForRoleMangager(user));
        }
        if (user.getRole().equals(Role.Engineer)) {
            return converterTiket.ToTicketDtoAssigner(ticketRepository.getTicketForRoleEngineer(user));
        }
        return Collections.emptyList();
    }

    public ResponseEntity getSingleTicketDto(long id, Authentication authentication) {
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
        TicketDto ticketDto = converterTiket.toTicketDto(ticketRepository.getTicket(id));
        return new ResponseEntity(ticketDto,HttpStatus.OK);
    }

    public ResponseEntity createTicket(@Valid  CreateTicketDto ticketDto, Authentication authentication) {
        Ticket ticket = converterTiket.OfTicketDto(ticketDto);
        User user = userRepository.getUser(authentication.getName());
        if (user.getRole() == Role.Engineer){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        long id = ticketRepository.addTicket(ticket);
        if (ticketDto.getComment() != null && ticketDto.getComment() != ""){
            commentRepository.addComment(converterComment.fromDto(ticketDto.getComment(),user,ticket));
        }
        return ResponseEntity.ok(id);
    }

    public ResponseEntity editTicket(TicketDto ticketDto,Authentication authentication)  {
        User user = userRepository.getUser(authentication.getName());
        Ticket ticket;
        try {
            ticket = ticketRepository.getTicket(ticketDto.getId());
        }catch (NoResultException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (ticket.getState() != State.Draft || !ticket.getOwner().equals(user) || user.getRole() == Role.Engineer){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        Category category = categoryRepository.getCategory(ticketDto.getCategory());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = format.parse(ticketDto.getDesiredResolutionDate() );
        } catch (ParseException e) {
            return new ResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        Timestamp timestamp = new Timestamp(date.getTime()+86400000);

        ticket.setState(State.valueOf(ticketDto.getState()));
        ticket.setName(ticketDto.getName());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setUrgency(ticketDto.getUrgency());
        ticket.setCategory(category);
        ticket.setResolutionDate(timestamp);

        ticketRepository.editTicket(ticket);
        System.out.println(ticketDto.getComment());
        if (ticketDto.getComment() != null && ticketDto.getComment() != ""){
            commentRepository.addComment(converterComment.fromDto(ticketDto.getComment(),user,ticket));
        }

        return ResponseEntity.ok().build();
    }

    /**
     *
     * @param id
     * @param authentication
     * @return ResposEntity<resoult>
     * @throws MessagingException
     *
     */
    public ResponseEntity submit(long id, Authentication authentication)  {
        User user = userRepository.getUser(authentication.getName());
        Ticket ticket;
        try {
            ticket = ticketRepository.getTicket(id);
        }catch (NoResultException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (ticket.getOwner().getEmail() != authentication.getName()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        String state = ticket.getState().toString();
        ticket.setState(State.New);
        ticketRepository.updateState(ticket);
        if(user.getRole() == Role.Employee || user.getRole() == Role.Manager){
            Context context = mailSendler.getContext(id);
//            Email block
            try {
                mailSendler.sendSimpleMessage(context, userRepository.getListManager(), " New ticket for approval","Template1");
            }catch (Exception e){

            }
        }
        historyRepository.addHistory(ticket,"Ticket Status is changed",String.format("Ticket Status is changed from %s to %s.",state,State.New));
        return ResponseEntity.ok().build();
    }

    /**
     *
     * @param id
     * @param authentication
     * @return
     */
    public ResponseEntity cancel(long id, Authentication authentication){
        User user = userRepository.getUser(authentication.getName());
        Ticket ticket;
        try {
            ticket = ticketRepository.getTicket(id);
        }catch (NoResultException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        State lastState = ticket.getState();
        if (ticket.getOwner().getEmail() == authentication.getName() || ticket.getState() == State.New && user.getRole() == Role.Manager
                || ticket.getState()==State.Approved && user.getRole() ==Role.Engineer) {
            String state = ticket.getState().toString();
            ticket.setState(State.Canceled);
            if (user.getRole() == Role.Manager){
                ticket.setApproved(user);
            }if(user.getRole() == Role.Engineer){
                ticket.setAssignee(user);
            }
            ticketRepository.updateState(ticket);
            //            Email block
            if (lastState == State.New || lastState == State.Approved){
                Context context = mailSendler.getContext(id,ticket.getOwner().getFirstName(),ticket.getOwner().getFirstName());
                List<User> userList = Stream.of(ticket.getOwner()).collect(Collectors.toList());
                if (lastState == State.Approved) {
                    userList.add(ticket.getApproved());
                    try {
                        mailSendler.sendSimpleMessage(context, userList, "Ticket was cancelled","Template5");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    try {
                        mailSendler.sendSimpleMessage(context, userList, "Ticket was cancelled","Template4");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            historyRepository.addHistory(ticket, "Ticket Status is changed", String.format("Ticket Status is changed from %s to %s.", state, State.Canceled));
            return ResponseEntity.ok().build();
        }else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    /**
     *
     * @param id
     * @param authentication
     * @return ResponseEntity
     */
    public ResponseEntity approve(long id, Authentication authentication) {
        User user = userRepository.getUser(authentication.getName());
        Ticket ticket;
        try{
            ticket = ticketRepository.getTicket(id);
        }catch (NoResultException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(user.getRole() != Role.Manager && ticket.getState() != State.New){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        String state = ticket.getState().toString();
        ticket.setApproved(user);
        ticket.setState(State.Approved);
        ticketRepository.updateState(ticket);
        //            Email block
        if(user.getRole() == Role.Manager){
            Context context = mailSendler.getContext(id);
            List<User> userList = userRepository.getListEngineer();
            userList.add(ticket.getOwner());
            try {
                mailSendler.sendSimpleMessage(context, userList , "Ticket was approved ","Template2");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        historyRepository.addHistory(ticket,"Ticket Status is changed",String.format("Ticket Status is changed from %s to %s.",state,State.Approved));
        return ResponseEntity.ok().build();
    }

    /**
     *
     * @param id
     * @param authentication
     * @return
     */
    public ResponseEntity decline(Long id,Authentication authentication) {
        User user = userRepository.getUser(authentication.getName());
        Ticket ticket;
        try {
            ticket = ticketRepository.getTicket(id);
        }catch (NoResultException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (user.getRole() != Role.Manager && ticket.getState() != State.New){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        String state = ticket.getState().toString();
        ticket.setState(State.Declined);
        ticket.setApproved(user);
        ticketRepository.updateState(ticket);
//        Email BLock
        if (user.getRole() == Role.Manager){
            Context context = mailSendler.getContext(id,ticket.getOwner().getFirstName(),ticket.getOwner().getLastName());
            List<User> userList = Stream.of(ticket.getOwner()).collect(Collectors.toList());
            try {
                mailSendler.sendSimpleMessage(context, userList , "Ticket was declined ","Template3");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        historyRepository.addHistory(ticket,"Ticket Status is changed",String.format("Ticket Status is changed from %s to %s.",state,State.Declined));
        return ResponseEntity.ok().build();
    }

    public ResponseEntity assign(Long id,Authentication authentication) {
        User user = userRepository.getUser(authentication.getName());
        Ticket ticket;
        try {
            ticket = ticketRepository.getTicket(id);
        }catch (NoResultException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (user.getRole() != Role.Engineer){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        String state = ticket.getState().toString();
        ticket.setAssignee(user);
        ticket.setState(State.In_Progress);
        ticketRepository.updateState(ticket);
        historyRepository.addHistory(ticket,"Ticket Status is changed",String.format("Ticket Status is changed from %s to %s.",state,State.In_Progress.label));
        return ResponseEntity.ok().build();
    }

    public ResponseEntity done(Long id,Authentication authentication) {
        User user = userRepository.getUser(authentication.getName());
        Ticket ticket ;
        try {
            ticket = ticketRepository.getTicket(id);
        }catch (NoResultException e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (user.getRole() != Role.Engineer){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        String state = ticket.getState().label;
        ticket.setState(State.Done);
        ticketRepository.updateState(ticket);
        if (user.getRole() == Role.Engineer){
            Context context = mailSendler.getContext(id,ticket.getOwner().getFirstName(),ticket.getOwner().getLastName());
            List<User> userList = Stream.of(ticket.getOwner()).collect(Collectors.toList());
            try {
                mailSendler.sendSimpleMessage(context, userList , "Ticket was done ","Template6");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        historyRepository.addHistory(ticket,"Ticket Status is changed",String.format("Ticket Status is changed from %s to %s.",state,State.Done));
        return ResponseEntity.ok().build();
    }

    private boolean forbidden(User user, Ticket ticket){
        if (user.getRole() == Role.Employee){
            if (ticket.getOwner().equals(user)){
                return true;
            }else {
                return false;
            }
        }if(user.getRole() == Role.Manager) {
            if (ticket.getOwner().equals(user) || (ticket.getOwner().getRole() == Role.Employee && ticket.getState() == State.New ||
                    ticket.getOwner().getRole() == Role.Manager && ticket.getState() == State.New|| ticket.getApproved().equals(user)) ){
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
