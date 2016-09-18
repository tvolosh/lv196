package com.softserve.edu.delivery.service;

import com.softserve.edu.delivery.dto.FeedbackDTO;
import com.softserve.edu.delivery.dto.OrderForAddDto;
import com.softserve.edu.delivery.dto.OrderForListDto;
import java.util.List;
/**
 * Created by Marina Chepurna
 */
public interface OrderService {
    List<OrderForListDto> findAllActiveOrders(String email, int page, int size);
    void addOrder(OrderForAddDto dto, String email);

/**Method for user story - "As customer I want to write transporter feedback."
* Author - Taras Kurdiukov
*/
    String addFeedback (FeedbackDTO dto, String email);
}
