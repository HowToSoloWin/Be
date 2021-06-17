package ru.com.alexsolo.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.com.alexsolo.domain.Feedback;

import javax.persistence.Query;


@Repository
public class FeedbackRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void addFeedback(Feedback feedback){
        sessionFactory.getCurrentSession().persist(feedback);
    }

    @Transactional
    public Feedback getFeedback(long id){
        Query query = sessionFactory.getCurrentSession().createQuery("from Feedback where ticket.id = :id");
        query.setParameter("id",id);
        return (Feedback) query.getSingleResult();
    }

    @Transactional
    public void mergeFeedback(Feedback feedback){
        sessionFactory.getCurrentSession().merge(feedback);
    }

}
