package ru.com.alexsolo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.com.alexsolo.domain.Category;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Transactional
public class CategoryRepository {

    @Autowired
    private SessionFactory sessionFactory;

    private List<Category> categoryList = Stream.of(
            new Category("Application & Services"),
            new Category("Benefits & Paper Work"),
            new Category("Hardware & Software"),
            new Category("People Management"),
            new Category("Security & Access"),
            new Category("Workplaces & Facilities")
    ).collect(Collectors.toList());

    @PostConstruct
    public void init(){
       Session session = sessionFactory.openSession();
       categoryList.forEach(category -> session.save(category));

    }

    public Category getCategory(String name){
        Query query = sessionFactory.getCurrentSession().createQuery("From Category where name = :name");
        query.setParameter("name",name);
        return (Category) query.getSingleResult();
    }
}
