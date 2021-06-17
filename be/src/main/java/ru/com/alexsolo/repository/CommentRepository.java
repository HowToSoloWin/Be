package ru.com.alexsolo.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.com.alexsolo.domain.Comment;

import org.hibernate.query.Query;

import java.util.List;

@Repository
@Transactional
public class CommentRepository {

    private UserRepository userRepository;
    private TicketRepository ticketRepository;
    private SessionFactory sessionFactory;

    @Autowired
    public CommentRepository(UserRepository userRepository, TicketRepository ticketRepository, SessionFactory sessionFactory) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void addComment(Comment comment){
        sessionFactory.getCurrentSession().persist(comment);
    }

    public List<Comment>  getComment(long id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Comment c where c.ticket.id = :id order by c.date desc").setMaxResults(5);
        query.setParameter("id",id);
        return query.list();
    }

    public List<Comment> getAllComment(long id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Comment c where c.ticket.id = :id order by c.date desc");
        query.setParameter("id",id);
        return query.list();
    }

    public long getAllCountComments(long id){
       Query query = sessionFactory.getCurrentSession().createQuery("SELECT COUNT(1) FROM Comment c where c.ticket.id = :id");
       query.setParameter("id",id);
       return (long) query.getSingleResult();
    }
}
