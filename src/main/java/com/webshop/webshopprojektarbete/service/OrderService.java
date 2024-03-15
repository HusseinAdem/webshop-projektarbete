package com.webshop.webshopprojektarbete.service;

import com.webshop.webshopprojektarbete.entity.Order;
import com.webshop.webshopprojektarbete.entity.Orderline;
import com.webshop.webshopprojektarbete.entity.Products;
import com.webshop.webshopprojektarbete.entity.Users;
import com.webshop.webshopprojektarbete.repository.OrderLineRepo;
import com.webshop.webshopprojektarbete.repository.OrderRepo;
import com.webshop.webshopprojektarbete.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Hashtable;

@Service
public class OrderService {
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    OrderLineRepo orderLineRepo;

    public void placeNewOrder(String userId, Hashtable<Products, Integer> orderedItems){

        Users a = userRepo.findByEmailIgnoreCase(userId);
        Order o = new Order(a.getEmail(),"RECIEVED", LocalDateTime.now(),a);
        orderRepo.save(o);
        newOrderLine(o, orderedItems);
        System.out.println("order: " + o);

    }
    public void newOrderLine(Order o, Hashtable<Products, Integer> orderedItems){

        for (Products product : orderedItems.keySet()){
            int quantity = orderedItems.get(product);
            Orderline ol = new Orderline(o.getId(), product.getId(), quantity, o, product);
            orderLineRepo.save(ol);
            System.out.println("orderline: " + ol);
        }


    }
}
