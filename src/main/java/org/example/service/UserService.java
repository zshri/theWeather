package org.example.service;

import org.example.dao.SessionDAO;
import org.example.dao.UserDAO;
import org.example.exception.DaoException;
import org.example.model.Session;
import org.example.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserService {

    UserDAO userDAO = new UserDAO();
    SessionDAO sessionDAO = new SessionDAO();

    public boolean checkLoginExists(String login) throws DaoException {
        Optional<User> userFromDb = userDAO.findUser(login);
        return userFromDb.isPresent();
    }

    public boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return Pattern.matches(regex, password);
    }

    public void createUser(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        userDAO.saveUser(user);
    }

    public User login(String login, String password) throws DaoException {
        Optional<User> userFromDb = userDAO.findUser(login);
        if (userFromDb.isPresent() && BCrypt.checkpw(password, userFromDb.get().getPassword())) {
            return userFromDb.get();
        } else {
            return null;
//            throw new Exception("login error");
        }
    }

    public User getUserBySession(String sessionId) throws DaoException {
        Session sessionFromDb = sessionDAO.findById(sessionId);

        User userFromDb = null;
        if (sessionFromDb != null) {
            userFromDb = userDAO.findById(sessionFromDb.getUserId());
        }
        return userFromDb;
    }

    public void saveSession(String sessionId, User user) {
        Session sessionById = sessionDAO.findById(sessionId);

        if (sessionById == null) {
            Timestamp timestamp =  new Timestamp(System.currentTimeMillis() + 3600000L * 24 * 30);
            Session sessionToDb = new Session(sessionId, user.getId(), timestamp);
            sessionDAO.saveSession(sessionToDb);
        }

    }

    public void deleteSession(String id) {
        sessionDAO.deleteSession(id);
    }
}
