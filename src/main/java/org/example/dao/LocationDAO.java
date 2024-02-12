package org.example.dao;

import org.example.config.HibernateConfig;
import org.example.exception.DaoException;
import org.example.model.Location;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class LocationDAO {

    public List<Location> findListLocationByUserId(Long userId) throws DaoException {
        Transaction transaction = null;
        List<Location> locationList = null;
        try (Session hibernateSession = HibernateConfig.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
//            locationList = hibernateSession.createCriteria(Location.class).list();
            Query<Location> query = hibernateSession.createQuery("from Location where userId = :id", Location.class)
                    .setParameter("id", userId);
            locationList = query.list();
            transaction.commit();
            return locationList;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException("db error");
        }
    }

    public Location getLocation(Long id) throws DaoException {
        Transaction transaction = null;
        Location locationFromDb = null;
        try (Session hibernateSession = HibernateConfig.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
            locationFromDb = hibernateSession.get(Location.class, id);
            transaction.commit();
            return locationFromDb;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException("db error");
        }
    }

    public boolean save(Location location) throws DaoException {
        Transaction transaction = null;
        try (Session hibernateSession = HibernateConfig.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
            hibernateSession.save(location);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException("save error");
        }
    }

    public boolean delete(Long id) throws DaoException {
        Transaction transaction = null;
        try (Session hibernateSession = HibernateConfig.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
            hibernateSession.createQuery("DELETE FROM Location WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException("delete error");
        }
    }

}
