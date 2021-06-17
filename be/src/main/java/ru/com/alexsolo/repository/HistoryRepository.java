package ru.com.alexsolo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.com.alexsolo.domain.History;
import ru.com.alexsolo.domain.Ticket;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public class HistoryRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void addHistory(Ticket ticket, String action, String description){
        sessionFactory.getCurrentSession().persist(new History(ticket,new Timestamp(System.currentTimeMillis()),action,ticket.getOwner(),description));
    }

    public List<History> getLastFiveHistory(long id){
        Query query = sessionFactory.getCurrentSession().createQuery("From History h where ticket.id = :id order by h.date desc").setMaxResults(5);
        query.setParameter("id",id);
        return query.list();
    }

    public List<History> getAllHistory(long id){
        Query query = sessionFactory.getCurrentSession().createQuery("From History h where ticket.id = :id order by h.date desc");
        query.setParameter("id",id);
        return query.list();
    }

    public long getAllCountHistory(long id){
        Query query = sessionFactory.getCurrentSession().createQuery("SELECT COUNT(1) FROM History h where h.ticket.id = :id");
        query.setParameter("id",id);
        return (long) query.getSingleResult();
    }
}
