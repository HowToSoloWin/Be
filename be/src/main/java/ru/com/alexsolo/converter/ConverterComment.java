package ru.com.alexsolo.converter;

import org.springframework.stereotype.Component;
import ru.com.alexsolo.Dto.CommentDto;
import ru.com.alexsolo.domain.Comment;
import ru.com.alexsolo.domain.Ticket;
import ru.com.alexsolo.domain.User;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
@Component
public class ConverterComment {

    public List<CommentDto> getDtoComment(List<Comment> commentList){

        List<CommentDto> commentResponseDtoList = new ArrayList<>();
        commentList.forEach(comment -> commentResponseDtoList.add(new CommentDto(new SimpleDateFormat("dd/MM/yyyy").
                format(comment.getDate()),comment.getUser().getFirstName(),comment.getText())));
        return commentResponseDtoList;
    }

    public Comment fromDto(String text, User user, Ticket ticket){
        return new Comment(user,text,new Timestamp(System.currentTimeMillis()),ticket);
    }
}
