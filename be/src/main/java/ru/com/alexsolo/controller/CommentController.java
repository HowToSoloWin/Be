package ru.com.alexsolo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.com.alexsolo.Dto.CommentDto;
import ru.com.alexsolo.service.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping()
    public ResponseEntity addComment(@RequestBody CommentDto commentDto){
         return commentService.addComment(commentDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity getComment(@PathVariable long id){
        return commentService.getComment(id);
    }

    @GetMapping("/all/{id}")
    public ResponseEntity getMoreComment(@PathVariable long id){
        return commentService.getAllComment(id);
    }

}
