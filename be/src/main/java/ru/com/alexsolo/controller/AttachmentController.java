package ru.com.alexsolo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.com.alexsolo.exceptions.IllegalFormatException;
import ru.com.alexsolo.service.AttachmentService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("/attachments")
public class AttachmentController {

    private AttachmentService attachmentService;

    @Autowired
    public AttachmentController( AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping()
    public ResponseEntity addAttachment(@RequestParam("file")List<MultipartFile>file, @RequestParam("id") long id){
        try {
            attachmentService.addattacment(file,id);
        }catch (IllegalFormatException e){
            return new ResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/{name}/{id}")
    public ResponseEntity getAttacmetn2(@PathVariable String name, @PathVariable long id) throws SQLException, IOException {
        return attachmentService.getAttachment(id,name);
    }

    @DeleteMapping("/{name}/{id}")
    public ResponseEntity deletAttacment(@PathVariable String name, @PathVariable long id){
        return attachmentService.delete(name,id);
    }
}
