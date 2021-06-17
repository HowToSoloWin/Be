package ru.com.alexsolo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.com.alexsolo.Dto.CommentDto;
import ru.com.alexsolo.converter.ConverterComment;
import ru.com.alexsolo.domain.Comment;
import ru.com.alexsolo.domain.Ticket;
import ru.com.alexsolo.domain.User;
import ru.com.alexsolo.repository.CommentRepository;
import ru.com.alexsolo.repository.TicketRepository;
import ru.com.alexsolo.repository.UserRepository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private ConverterComment converterComment;
    private UserRepository userRepository;
    private TicketRepository ticketRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, ConverterComment converterComment, UserRepository userRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.converterComment = converterComment;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public ResponseEntity getComment(long id){
        Map<Object,Object> obj = new HashMap<>();
        obj.put("Comment",converterComment.getDtoComment(commentRepository.getComment(id)));
        obj.put("count",commentRepository.getAllCountComments(id));
        return ResponseEntity.ok(obj);
    }

    public ResponseEntity addComment(CommentDto commentDto){
        User user = userRepository.getUser(commentDto.getName());
        Ticket ticket = ticketRepository.getTicket((long) commentDto.getTicketId());
        Comment comment = new Comment(user,commentDto.getComment(),new Timestamp(System.currentTimeMillis()),ticket);
        commentRepository.addComment(comment);
        return getComment(commentDto.getTicketId());
    }

    public ResponseEntity getAllComment(long id){
        Map<Object,Object> obj = new HashMap<>();
        obj.put("Comment",converterComment.getDtoComment(commentRepository.getAllComment(id)));
        obj.put("count",commentRepository.getAllCountComments(id));
        return ResponseEntity.ok(obj);
    }
}
