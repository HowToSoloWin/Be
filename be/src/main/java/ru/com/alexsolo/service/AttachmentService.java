package ru.com.alexsolo.service;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.com.alexsolo.domain.Attachment;
import ru.com.alexsolo.exceptions.IllegalFormatException;
import ru.com.alexsolo.repository.AttachmentRepository;
import ru.com.alexsolo.repository.TicketRepository;

import javax.persistence.NoResultException;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Service
public class AttachmentService {

    private TicketRepository ticketRepository;
    private AttachmentRepository attachmentRepository;
    private SessionFactory sessionFactory;

    @Autowired
    public AttachmentService(TicketRepository ticketRepository, AttachmentRepository attachmentRepository, SessionFactory sessionFactory) {
        this.ticketRepository = ticketRepository;
        this.attachmentRepository = attachmentRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public ResponseEntity addattacment(List<MultipartFile> file, long id){
        Session session = sessionFactory.getCurrentSession();
        file.forEach(multipartFile -> {
            if (wrongType(multipartFile.getContentType())) {
                throw new IllegalFormatException("");
            }
        });
        file.forEach(multipartFile -> {
            try {
                Blob blob = session.getLobHelper().createBlob(multipartFile.getInputStream(),5000000);
                attachmentRepository.addAttacment(new Attachment(blob,ticketRepository.getTicket(id),multipartFile.getOriginalFilename()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getAttachment(long id,String name) throws IOException, SQLException {
        Attachment attachment;
        try {
             attachment = attachmentRepository.getAttachment(id,name);
        }catch (NoResultException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", attachment.getName()));
        byte[] bytes = IOUtils.toByteArray(attachment.getBlob().getBinaryStream());
        String contetnType = "";

        if (getFileExtension(attachment.getName()).equals("pdf")){
            contetnType =  "application/pdf";
        }else if(getFileExtension(attachment.getName()).equals("png")){
            contetnType = "image/png";
        }else if (getFileExtension(attachment.getName()).equals("jpeg") || getFileExtension(attachment.getName()).equals("jpg")){
            contetnType = "image/jpg";
        }else if (getFileExtension(attachment.getName()).equals("docx")){
            contetnType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }else if (getFileExtension(attachment.getName()).equals("doc")){
            contetnType = "application/msword";
        }

        return ResponseEntity.ok().headers(headers).contentLength(
                bytes.length).contentType(MediaType.parseMediaType(contetnType)).
                body(new InputStreamResource(attachment.getBlob().getBinaryStream()));
    }

    private  String getFileExtension(String mystr) {
        int index = mystr.lastIndexOf('.');
        return index == -1? null : mystr.substring(index).replace(".","");
    }

    public ResponseEntity delete(String name, long id){
        try {
            attachmentRepository.deletAttachment(attachmentRepository.getAttachment(id,name));
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
    }

    private boolean wrongType(String type){
        if (type != null && !type.equals("image/jpeg") && !type.equals("application/pdf") &&
                !type.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") &&
                !type.equals("application/msword") && !type.equals("image/png")){
            return true;
        }
        return false;
    }

}
