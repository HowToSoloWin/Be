package ru.com.alexsolo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.com.alexsolo.Enum.Role;
import ru.com.alexsolo.domain.User;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Transactional
public class UserRepository {

    @Autowired
    private SessionFactory sessionFactory;


    private List<User> userList = Stream.of(
            new User("Mcree","Old", Role.Employee,"user1_mogilev@yopmail.com","P@ssword1"),
            new User("Mercy","brut",Role.Employee,"user2_mogilev@yopmail.com","P@ssword1"),
            new User("Genji","hasashi",Role.Manager,"manager1_mogilev@yopmail.com","P@ssword1"),
            new User("Hanzo","hasashi", Role.Manager,"manager2_mogilev@yopmail.com","P@ssword1"),
            new User("Anna","Over",Role.Engineer,"engineer1_mogilev@yopmail.com","P@ssword1"),
            new User("Batist","dell",Role.Engineer,"engineer2_mogilev@yopmail.com","P@ssword1"),
            new User("Ashe Manager","Oww",Role.Manager,"123","123"),
            new User("Ashe Engineer" , "oww", Role.Engineer,"111","111"),
            new User("Ashe Employee","oww",Role.Employee,"222","222")
    ).collect(Collectors.toList());

    @PostConstructor
    @Transactional
    public void init(){
        Session session = sessionFactory.openSession();
        this.userList.forEach(user -> session.save(user));
    }

    @Cacheable("addresses")
    public User getUser(String email){
        Query query = sessionFactory.getCurrentSession().createQuery("From User where email = :email");
        query.setParameter("email",email);
        return (User) query.getSingleResult();
    }

    public List<User> getListEngineer(){
        Query query = sessionFactory.getCurrentSession().createQuery("From User where role = :role");
        query.setParameter("role",Role.Engineer);
        return query.list();
    }

    public List<User> getListManager(){
        Query query = sessionFactory.getCurrentSession().createQuery("From User where role = :role");
        query.setParameter("role",Role.Manager);
        return query.list();
    }
}
