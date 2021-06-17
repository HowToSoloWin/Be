package ru.com.alexsolo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.com.alexsolo.domain.Attachment;

import java.util.List;


@Repository
@Transactional
public class AttachmentRepository {

    private HistoryRepository historyRepository;
    private SessionFactory sessionFactory;

    @Autowired
    public AttachmentRepository(HistoryRepository historyRepository, SessionFactory sessionFactory) {
        this.historyRepository = historyRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void addAttacment(Attachment attachment){
        sessionFactory.getCurrentSession().save(attachment);
        historyRepository.addHistory(attachment.getTicket(),"File is attached","File is attached:" + attachment.getName());
    }

    @Transactional
    public Attachment getAttachment(long id,String name){
        Query query = sessionFactory.getCurrentSession().createQuery("from Attachment where ticket_id = :id and name =:name");
        query.setParameter("id",id);
        query.setParameter("name",name);
        Attachment attachment = (Attachment) query.getSingleResult();
        return attachment;
    }
    
    @Transactional
    public List<Attachment> getAttachmentList(long id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Attachment where ticket.id = :id");
        query.setParameter("id",id);
        return query.list();
    }

    @Transactional
    public void deletAttachment(Attachment attachment){
        sessionFactory.getCurrentSession().delete(attachment);
    }
}
