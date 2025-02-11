package org.example.impl;

import org.example.entity.Account;
import org.example.entity.User;
import org.example.factory.MySessionFactory;
import org.example.service.UserService;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserServiceImpl implements UserService {

    private SessionFactory sessionFactory;
    public UserServiceImpl() {
        this.sessionFactory = MySessionFactory.getSessionFactory();
    }
    @Override
    public void register(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            // start transaction
            transaction = session.beginTransaction();

            // business logic
            session.persist(user);

            // end transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("transaction failed: " + e.getMessage());
        }
    }

    @Override
    public User authenticate(String email, String password) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            // start transaction
            transaction = session.beginTransaction();

            // business logic
            String hql = "SELECT u From User u WHERE u.email = :email and u.password = :password";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            List<User> users = query.getResultList();

            // end transaction
            transaction.commit();
            return users.isEmpty()? null : users.get(0);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("transaction failed: " + e.getMessage());
        }
        return null;
    }

}
