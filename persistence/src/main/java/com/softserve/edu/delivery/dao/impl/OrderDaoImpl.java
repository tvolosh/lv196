package com.softserve.edu.delivery.dao.impl;

import com.softserve.edu.delivery.dao.OrderDao;
import com.softserve.edu.delivery.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class OrderDaoImpl extends BaseDaoImpl<Order, Long> implements OrderDao {

    public OrderDaoImpl() {
        super(Order.class);
    }

    public List<Order> findAllOrdersByStatus(String email, int page, int size, OrderStatus orderStatus) {
        return getEntityManager()
                .createQuery("select o from Order o where o.user.email = :email " +
                        "and o.orderStatus = :orderStatus " +
                        "order by o.registrationDate", Order.class)
                .setParameter("email", email)
                .setParameter("orderStatus", orderStatus)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }


    public List<Order> changeStatus(String order_id, Boolean offerStatus) {
        Order order=(Order)getEntityManager()                                   //get order from DB by order_id
                .createQuery("select o from Order o where o.id = :id ")
                .setParameter("id", Long.parseLong(order_id))
                .getSingleResult();

        getEntityManager()                                                      //update offer's with offerStatus where is our order
                .createQuery("update Offer set isApproved=:offerStatus where order = :order")
                .setParameter("order", order)
                .setParameter("offerStatus", offerStatus);
        return findAll();
    }


    // next 4 methods author Ivan Synyshyn
    @Override
    public List<Order> getOrderByCityFrom(Long id) {
        EntityManager em = super.getEntityManager();
        Query query = em.createQuery("select o from Order o where o.cityFrom.cityId = :cityId");
        query.setParameter("cityId", id);
        return query.getResultList();
    }

    @Override
    public List<Order> getOrderByCityTo(Long id) {
        EntityManager em = super.getEntityManager();
        Query query = em.createQuery("select o from Order o where o.cityTo.cityId = :cityId");
        query.setParameter("cityId", id);
        return query.getResultList();
    }

    @Override
    public List<Order> getOrderByWeight(BigDecimal weight) {
        EntityManager em = super.getEntityManager();
        Query query = em.createQuery("select o from Order o where o.weight <= :weight");
        query.setParameter("weight", weight);
        return query.getResultList();
    }

    @Override
    public List<Order> getOrderByArrivalDate(Timestamp arrivalDate) {
        EntityManager em = super.getEntityManager();
        Query query = em.createQuery("select o from Order o where o.arrivalDate <= :arrivalDate");
        query.setParameter("arrivalDate", arrivalDate);
        return query.getResultList();
    }
}
