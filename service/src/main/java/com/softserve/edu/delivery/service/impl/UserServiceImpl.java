package com.softserve.edu.delivery.service.impl;

import com.softserve.edu.delivery.dao.UserDao;
import com.softserve.edu.delivery.domain.User;
import com.softserve.edu.delivery.dto.UserAuthDTO;
import com.softserve.edu.delivery.dto.UserProfileDto;
import com.softserve.edu.delivery.dto.UserProfileFilterDto;
import com.softserve.edu.delivery.service.UserService;
import com.softserve.edu.delivery.utils.Jpa;
import com.softserve.edu.delivery.utils.TransactionManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean exists(String email) {
        return TransactionManager.withTransaction(() -> userDao.exists(email));
    }

    @Override
    public void register(User user) {
        TransactionManager.withTransaction(() -> {
            if (!userDao.exists(user.getEmail())) {
                userDao.save(user);
            } else {
                throw new IllegalArgumentException("User with given email already exists.");
            }
        });
    }


    public void register(User user, boolean withoutLambda) {
        if (!withoutLambda) {
            register(user);
        } else {
            EntityTransaction tx = null;
            try {
                EntityManager entityManager = Jpa.getEntityManager();
                tx = entityManager.getTransaction();
                tx.begin();
                if (userDao.exists(user.getEmail())) {
                    throw new IllegalArgumentException("User with given email already exists.");
                } else {
                    userDao.save(user);
                    tx.commit();
                }
            }catch (Exception ex) {
                if (tx != null) tx.rollback();
                throw ex;
            }
        }
    }


    /*** This method verifies user credentials(login page)
     * author Petro Shtenovych
     * @param user with credentials email and password
     * @throws IllegalArgumentException if param ref null
     * @throws RuntimeException if database errors occur
     * @return true if user verification was success
     */
    @Override
    public boolean verificationLogin(UserAuthDTO user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        EntityTransaction tx = null;
        try {
            EntityManager entityManager = Jpa.getEntityManager();
            tx = entityManager.getTransaction();
            tx.begin();
            if ( ! this.userDao.exists(user.getEmail())) {
                tx.commit();
                return false;
            } else {
                User dbUser = this.userDao.findOne(user.getEmail()).get();
                tx.commit();
                if ( ! checkPassword(user, dbUser)) {
                    return false;
                }
            }
        }catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
                throw ex;
            }
        }
        return true;
    }

    //<-----------------------Petro Shtenovych---------------------------->

    private static boolean checkPassword(UserAuthDTO user, User dbUser) {
        if( ! user.getPassword().equals(dbUser.getPassport())) {
            return false;
        }
        return true;
    }
    
    
    
    
	@Override
	public List<UserProfileDto> getAllUsers(int from, int count, UserProfileFilterDto filter) {
		return TransactionManager.withTransaction(() ->
				userDao
						.findAll().stream()
						.skip(from)
						.filter(filter::test)
						.map(UserProfileDto::create)
						.limit(count)
						.collect(Collectors.toList())
				);
	}

	@Override
	public UserProfileDto changeUserStatus(String mail, boolean blocked) {
		return TransactionManager.withTransaction(() ->
				userDao
						.findOne(mail)
						.map(user -> {	
						user.setBlocked(blocked);
						return userDao.updateWithReturn(user);
						})
						.map(UserProfileDto::create)
						.orElseThrow(() -> new IllegalStateException("User: " + mail + " not found!"))
				);
	}
	
	@Override
	public List<UserProfileDto> changeUsersStatus(Map<String, Boolean> map) {
		return TransactionManager.withTransaction(() ->
				map
					.keySet().stream()
					.map(mail -> changeUserStatus(mail, map.get(mail)))
					.collect(Collectors.toList())
				);			
	}
}
