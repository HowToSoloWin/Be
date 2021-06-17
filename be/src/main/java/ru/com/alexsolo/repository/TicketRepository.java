package ru.com.alexsolo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.com.alexsolo.Enum.Role;
import ru.com.alexsolo.Enum.State;
import ru.com.alexsolo.Enum.Urgency;
import ru.com.alexsolo.domain.Category;
import ru.com.alexsolo.domain.Ticket;
import ru.com.alexsolo.domain.User;


import javax.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Transactional
public class TicketRepository {

    private UserRepository userRepository;
    private HistoryRepository historyRepository;
    private CategoryRepository categoryRepository;
    private SessionFactory sessionFactory;

    @Autowired
    public TicketRepository(UserRepository userRepository, HistoryRepository historyRepository, CategoryRepository categoryRepository, SessionFactory sessionFactory) {
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.categoryRepository = categoryRepository;
        this.sessionFactory = sessionFactory;
    }

    @PostConstruct
    public void init(){
        User user1 = userRepository.getUser("user1_mogilev@yopmail.com");
        User user2 = userRepository.getUser("user2_mogilev@yopmail.com");
        User user3 = userRepository.getUser("222");
        User manager1 = userRepository.getUser("123");
        User enginer3 = userRepository.getUser("111");
        User manager2 = userRepository.getUser("manager2_mogilev@yopmail.com");
        User Engineer1 = userRepository.getUser("engineer1_mogilev@yopmail.com");
        User Engineer2 = userRepository.getUser("engineer2_mogilev@yopmail.com");
        Category category = categoryRepository.getCategory("People Management");
        Date date = new Date();

        List<Ticket> ticketList = Stream.of(
                new Ticket("First","Welcom",new Timestamp(date.getTime() +34000),new Timestamp(date.getTime() + 321033231) ,Engineer1,user1, State.Draft,category, Urgency.Critical,manager1),
                new Ticket("Second","To",new Timestamp(date.getTime() +1),new Timestamp(date.getTime() + 444444) ,Engineer2,user2, State.New,category, Urgency.Low,manager2),
                new Ticket("Third","Painkiller",new Timestamp(date.getTime()+40000),new Timestamp(date.getTime() + 2000) ,Engineer1,user1, State.Draft,category, Urgency.Average,manager1),
                new Ticket("Bird","Test",new Timestamp(date.getTime()+80000),new Timestamp(date.getTime() +34000) ,Engineer2,user2, State.New,category, Urgency.Hight,manager2),
                new Ticket("White","Wild",new Timestamp(date.getTime()-100006),new Timestamp(date.getTime() + 40000111) ,Engineer1,user1, State.Approved,category, Urgency.Average,manager1),
                new Ticket("Jojo","Joji",new Timestamp(date.getTime()),new Timestamp(date.getTime() + 300000534) ,Engineer2,user2, State.Draft,category, Urgency.Average,manager2),
                new Ticket("ITS 2 manager","NoTime",new Timestamp(date.getTime()),new Timestamp(date.getTime()),manager2,State.New,category,Urgency.Average),
                new Ticket("123","123",new Timestamp(date.getTime()),new Timestamp(date.getTime()),enginer3,user3,State.Done,category,Urgency.Average,manager1)
        ).collect(Collectors.toList());

        Session session = sessionFactory.openSession();
        ticketList.forEach(ticket -> {
            session.save(ticket);
            historyRepository.addHistory(ticket,"Ticket is create","Ticket is create");
        });
    }

    public List<Ticket> getTicketsForRoleEmployee(User user){
        Query query =sessionFactory.getCurrentSession().createQuery("From Ticket ti  where ti.owner = :owner ");
        query.setParameter("owner", user);
        return query.list();
    }

    public List<Ticket> getTicketForRoleMangager(User user){
        Query query = sessionFactory.getCurrentSession().createQuery("From Ticket ti where  ti.state = :state or ti.approved = :approved or ti.owner = :owner");
        query.setParameter("state",State.New);
        query.setParameter("approved",user);
        query.setParameter("owner",user);
        return query.list();
    }

    public List<Ticket> getTicketForRoleEngineer(User user){
        Query query = sessionFactory.getCurrentSession().createQuery("From Ticket ti where ti.assignee = :user or ti.state = :state ");
        query.setParameter("user",user);
        query.setParameter("state",State.Approved);
        return query.list();
    }

    public Ticket getTicket(Long id){
        Query query = sessionFactory.getCurrentSession().createQuery("From Ticket  where id = :id");
        query.setParameter("id",id);
        return (Ticket) query.getSingleResult();
    }

    @Transactional
    public long addTicket(Ticket ticket){
        long id = (long) sessionFactory.getCurrentSession().save(ticket);
        historyRepository.addHistory(ticket,"Ticket is create","Ticket is create");
        return id;
    }

    public List<Ticket> getMyTicket(User user){
        Query query = sessionFactory.getCurrentSession().createQuery("From Ticket Ti where owner = :user");
        query.setParameter("user",user);
        return query.list();
    }

    @Transactional
    public void editTicket(Ticket ticket)  {
        sessionFactory.getCurrentSession().merge(ticket);
        historyRepository.addHistory(ticket,"Ticket is edit","Ticket is edit");
    }

    @Transactional
    public void updateState(Ticket ticket){
        Session session = sessionFactory.getCurrentSession();
        session.merge(ticket);
    }

    private List<Ticket> addNullble(List<Ticket> ticketList){
        User userNull = new User("","",Role.Nullble,"","");
        ticketList.forEach(ticket -> {
            if(ticket.getApproved() == null){
                ticket.setApproved(userNull);
            }if(ticket.getAssignee() == null){
                ticket.setAssignee(userNull);
            }
        });

        return ticketList;
    }
}
