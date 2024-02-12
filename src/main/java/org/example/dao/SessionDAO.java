package org.example.dao;

import org.example.config.HibernateConfig;
import org.example.model.Session;
import org.hibernate.Transaction;

public class SessionDAO {


    public Session findById(String sessionId) {

        Transaction transaction = null;
        Session sessionFromDb = null;
        try (org.hibernate.Session hibernateSession = HibernateConfig.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
            sessionFromDb = hibernateSession.get(Session.class, sessionId);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            // todo обработь тут а потом ещё пробросить?
            e.printStackTrace();
        }
        return sessionFromDb;
    }

    public Session findByUserId(Long userId) {

        Transaction transaction = null;
        Session sessionFromDb = null;
        try (org.hibernate.Session hibernateSession = HibernateConfig.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
//            transaction.setTimeout();
            sessionFromDb = hibernateSession.createQuery("FROM Session WHERE userId = :userId", Session.class)
                    .setParameter("userId", userId)
                    .uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            // todo обработь тут а потом ещё пробросить?
            e.printStackTrace();
        }
        return sessionFromDb;
    }



    public boolean saveSession(Session session){
        Transaction transaction = null;
        try (org.hibernate.Session hibernateSession = HibernateConfig.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
            hibernateSession.save(session);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    public boolean deleteSession(String id) {
        Transaction transaction = null;
        try (org.hibernate.Session hibernateSession = HibernateConfig.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
            hibernateSession.createQuery("DELETE FROM Session WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    public boolean deleteExpiredRecords() {
        Transaction transaction = null;
        try (org.hibernate.Session hibernateSession = HibernateConfig.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
            hibernateSession.createNativeQuery("DELETE FROM session WHERE expiresat < CURRENT_TIMESTAMP").executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }


}
