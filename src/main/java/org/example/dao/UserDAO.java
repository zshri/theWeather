package org.example.dao;

import org.example.config.HibernateConfig;
import org.example.exception.DaoException;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class UserDAO {


    public User findById(Long id) throws DaoException {

        Transaction transaction = null;
        User user = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException("db error");
        }
        return user;
    }

    //    todo return option
    public Optional<User> findUser(String login) throws DaoException {

        Transaction transaction = null;
        Optional<User> optionalUser = Optional.empty();
        try (Session hibernateSession = HibernateConfig.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
            User user = hibernateSession.createQuery("FROM User WHERE login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();
            optionalUser = Optional.ofNullable(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException("db error");
        }
        return optionalUser;
    }

    // get by id void get(T entity) throws DaoException;
    public void saveUser(User user) {
        Transaction transaction = null;
        try (Session hibernateSession = HibernateConfig.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
            hibernateSession.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            // todo Выбрасывать исключение? например СэйвЭксептион
            e.printStackTrace();
        }
    }



}