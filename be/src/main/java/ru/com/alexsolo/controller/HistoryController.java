package ru.com.alexsolo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.com.alexsolo.service.HistoryService;

@RestController
@RequestMapping("/history")
public class HistoryController {


    @Autowired
    private HistoryService historyService;

    @GetMapping("/{id}")
    public ResponseEntity getHistory(@PathVariable long id, Authentication authentication){
      return historyService.getHistory(id, authentication);
    }

    @GetMapping("/all/{id}")
    public ResponseEntity getHistoryAll(@PathVariable long id, Authentication authentication){
        return historyService.getAllHistory(id,authentication);
    }
}
