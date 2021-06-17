package ru.com.alexsolo.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.com.alexsolo.Dto.AttacmentDto;
import ru.com.alexsolo.Dto.CreateTicketDto;
import ru.com.alexsolo.Dto.TicketDto;

import ru.com.alexsolo.Enum.Action;
import ru.com.alexsolo.Enum.Role;
import ru.com.alexsolo.Enum.State;
import ru.com.alexsolo.Enum.Urgency;
import ru.com.alexsolo.domain.Attachment;
import ru.com.alexsolo.domain.Category;
import ru.com.alexsolo.domain.Ticket;
import ru.com.alexsolo.domain.User;
import ru.com.alexsolo.repository.AttachmentRepository;
import ru.com.alexsolo.repository.CategoryRepository;
import ru.com.alexsolo.repository.UserRepository;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ConverterTiket {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private AttachmentRepository attachmentRepository;

    @Autowired
    public ConverterTiket(UserRepository userRepository, CategoryRepository categoryRepository, AttachmentRepository attachmentRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public Ticket OfTicketDto(CreateTicketDto ticketDto){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp timestamp = null;
        Date date;
        try {
            date = format.parse(ticketDto.getDesiredResolutionDate());
            timestamp = new Timestamp(date.getTime() + 86400000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        User user = userRepository.getUser(ticketDto.getUsername());
        Category category = categoryRepository.getCategory(ticketDto.getCategory());
        Ticket ticket = new Ticket(
                ticketDto.getName(),ticketDto.getDescription(),new Timestamp(System.currentTimeMillis()),timestamp,user, ticketDto.getState(),category, Urgency.valueOf(ticketDto.getUrgency()));

        return ticket;
    }

    public TicketDto toTicketDto(Ticket ticket){
        addNullble(ticket);
        List<Attachment> attacmentList = attachmentRepository.getAttachmentList(ticket.getId());
        List<AttacmentDto> attacmentDtoList = new ArrayList<>();
        attacmentList.forEach(attachment -> attacmentDtoList.add(new AttacmentDto(attachment.getTicket().getId(),attachment.getName())));
        TicketDto ticketDto = new TicketDto(
                ticket.getId(),
                ticket.getName(),
                ticket.getDescription(),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(ticket.getCreatedOn()),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(ticket.getResolutionDate()),
                ticket.getAssignee().getFirstName(),
                ticket.getOwner().getFirstName(),
                ticket.getState().toString(),
                ticket.getUrgency(),
                ticket.getCategory().getName(),
                ticket.getApproved().getFirstName(),attacmentDtoList);
        return ticketDto;
     }

     public List<TicketDto> ticketToDtoEmployee(List<Ticket> ticketList){
         List<TicketDto> ticketDtoList = new ArrayList<>();

         List<Action> actionsOff = new ArrayList<>();
         List<Action> actionsOn = Stream.of(
                 Action.Submit,
                 Action.Cancel
         ).collect(Collectors.toList());

       ticketList.forEach(ticket -> {
         if (ticket.getState() == State.Draft || ticket.getState() == State.Declined){
             ticketDtoList.add(localConvertor(ticket,actionsOn)
             );
         }else {
             ticketDtoList.add(localConvertor(ticket,actionsOff));
         }
         });

         return ticketDtoList;
     }

    public List<TicketDto> ticketToDtoManager(List<Ticket> ticketList){
        List<TicketDto> ticketDtoList = new ArrayList<>();

        List<Action> actionsOff = new ArrayList<>();
        List<Action> actionsOne = Stream.of(
                Action.Approve,
                Action.Decline,
                Action.Cancel
        ).collect(Collectors.toList());

        List<Action> actionsTwo = Stream.of(
                Action.Submit,
                Action.Cancel
        ).collect(Collectors.toList());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ticketList.forEach(ticket -> {
            if (ticket.getState() == State.Draft && ticket.getOwner().getRole() == Role.Manager || ticket.getState() == State.Declined && ticket.getOwner().getRole() == Role.Manager ){
                ticketDtoList.add(localConvertor(ticket,actionsTwo));
            }else if(ticket.getState() == State.New && ticket.getOwner().getRole() == Role.Manager && ticket.getOwner().getEmail() != auth.getName() || ticket.getOwner().getRole() == Role.Employee && ticket.getState() == State.New){
                ticketDtoList.add(localConvertor(ticket,actionsOne));
            }else {
                ticketDtoList.add(localConvertor(ticket,actionsOff));
            }
        });
        return ticketDtoList;
    }

    public List<TicketDto> ToTicketDtoAssigner(List<Ticket> ticketList){
        List<TicketDto> ticketDtoList = new ArrayList<>();

        List<Action> actionsOff = new ArrayList<>();

        List<Action> actionsOne = Stream.of(
                Action.Assign_to_Me,
                Action.Cancel
        ).collect(Collectors.toList());

        List<Action> actionsTwo = Stream.of(
                Action.Done
                ).collect(Collectors.toList());

        ticketList.forEach(ticket -> {
            if (ticket.getState() == State.Approved){
                ticketDtoList.add(localConvertor(ticket,actionsOne));
            }else if(ticket.getState() == State.In_Progress){
                ticketDtoList.add(localConvertor(ticket,actionsTwo));
            }else {
                ticketDtoList.add(localConvertor(ticket,actionsOff));
            }
        });
        return ticketDtoList;
    }

    private TicketDto localConvertor(Ticket ticket, List<Action> actions){
        addNullble(ticket);
        TicketDto ticketDto = new TicketDto(
                ticket.getId(),ticket.getName(),ticket.getDescription(),new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(ticket.getCreatedOn()),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(ticket.getResolutionDate()),
                ticket.getAssignee().getFirstName(),ticket.getApproved().getFirstName(),
                ticket.getOwner().getFirstName(),ticket.getState().toString(),ticket.getUrgency(),
                ticket.getCategory().getName(),actions);
                return ticketDto;
    }

    private Ticket addNullble(Ticket ticket){
        User userNull = new User("","",Role.Nullble,"","");
            if(ticket.getApproved() == null){
                ticket.setApproved(userNull);
            }if(ticket.getAssignee() == null){
                ticket.setAssignee(userNull);
            }
        return ticket;
    }
}
