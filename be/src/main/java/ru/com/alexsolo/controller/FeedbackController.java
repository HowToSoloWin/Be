package ru.com.alexsolo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.com.alexsolo.Dto.FeedbackDto;
import ru.com.alexsolo.service.FeedbackService;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping()
    public ResponseEntity addFeedback(@RequestBody FeedbackDto feedbackDto, Authentication authentication){
        return feedbackService.addFeedback(feedbackDto,authentication);
    }

    @GetMapping("/{id}")
    public ResponseEntity getFeedback(@PathVariable long id){
            return feedbackService.getFeedback(id);
    }
}
