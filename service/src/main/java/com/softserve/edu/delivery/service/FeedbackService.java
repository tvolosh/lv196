package com.softserve.edu.delivery.service;

import com.softserve.edu.delivery.dao.FeedbackDao;
import com.softserve.edu.delivery.dao.OrderDao;
import com.softserve.edu.delivery.dao.UserDao;
import com.softserve.edu.delivery.domain.Car;
import com.softserve.edu.delivery.domain.Feedback;
import com.softserve.edu.delivery.domain.Offer;
import com.softserve.edu.delivery.domain.Order;
import com.softserve.edu.delivery.domain.User;
import com.softserve.edu.delivery.dto.FeedbackDTO;

import java.util.List;

/**
 * Created by Ivan Rudnytskyi on 15.09.2016.
 */
public interface FeedbackService {

    FeedbackDTO copyFeedbackToDTO(Feedback feedback);

    Feedback copyDTOToFeedback(FeedbackDTO feedbackDTO);

    List<FeedbackDTO> getAllFeedbacks();

    List<FeedbackDTO> getAllFeedbacksInRange(long from, long count);

    FeedbackDTO getFeedbackById(long id);

    void changeFeedbackStatus(long id, boolean status);

    void save(FeedbackDTO feedbackDTO);

    void update(FeedbackDTO feedbackDTO);

    void delete(Long id);

    FeedbackDTO findOne(Long id);

    String getApprovedDriverName(Long orderId);

    void setFeedbackDao(FeedbackDao feedbackDao);

    void setUserDao(UserDao userDao);

    void setOrderDao(OrderDao orderDao);

    Long getId(String query);

    User getUser(String email);

    Order getOrder(Long id);

    Car getCar(Long id);

    void saveUser(User user);

    void saveCar(Car car);

    void saveOrder(Order order);

    void saveOffer(Offer offer);

    List<User> getUsersByRole(String role);
}
