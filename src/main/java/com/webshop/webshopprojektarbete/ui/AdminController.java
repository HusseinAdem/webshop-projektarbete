package com.webshop.webshopprojektarbete.ui;

import com.webshop.webshopprojektarbete.entity.Order;
import com.webshop.webshopprojektarbete.entity.Products;
import com.webshop.webshopprojektarbete.repository.OrderLineRepo;
import com.webshop.webshopprojektarbete.repository.OrderRepo;
import com.webshop.webshopprojektarbete.service.OrderService;
import com.webshop.webshopprojektarbete.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    OrderLineRepo orderLineRepo;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;

    @GetMapping("/allorders")
    public String showAllOrders(Model model){
        List<Order> orderList = orderRepo.findAll();
        model.addAttribute("all_orders", orderList);
        return "allorderspage";
    }
    @PostMapping("/start_handling")
    public String startHandlingOrder(@RequestParam("order_id") int orderId, Model model){
        Order o = orderService.findOrderById(orderId);
        model.addAttribute("order_to_handle", o);
        return "order_management_page";
    }

    @PostMapping("/mark_order")
    public String markOrder(@RequestParam("order_id") int orderId, @RequestParam("action") String action, Model model) {
        String orderStatus = null;
        switch (action) {
            case "PENDING" -> orderStatus = "PENDING";
            case "SHIPPED" -> orderStatus = "SENT";
            case "RECEIVED" -> orderStatus = "RECEIVED";
            case "RETURN" -> {
                return "redirect:/allorders";
            }
        }
        orderService.markOrder(orderId, orderStatus);
        return startHandlingOrder(orderId, model);
    }
    @GetMapping("/admin_start_page")
    public String adminStart(){
        return "admin_start_page";
    }

    @GetMapping("/orders_to_handle")
    public String showOrdersToHandle(Model model){
        List<Order> receivedOrders = orderService.findOrdersByStatus("RECEIVED");
        model.addAttribute("received_orders", receivedOrders);
        return "orders_to_handle";
    }
    @GetMapping("/shipped_orders")
    public String showShippedOrders(Model model){
        List<Order> sentOrders = orderService.findOrdersByStatus("SENT");
        model.addAttribute("sent_orders", sentOrders);
        return "shipped_orders_page";
    }
    @GetMapping("/pending_orders")
    public String showPendingOrders(Model model){
        List<Order> pendingOrders = orderService.findOrdersByStatus("PENDING");
        model.addAttribute("pending_orders", pendingOrders);
        return "pending_orders_page";
    }
    @GetMapping("/add_product")
    public String addNewProductFirst(){
        System.out.println("hejdå");
        return "add_product_form";
    }

    @GetMapping("/dashboard")
    public String goToDashboard(){
        return "admin_start_page";
    }


    @PostMapping("/add_product")
    public String addNewProductSecond(@RequestParam String name,
                                      @RequestParam String color,
                                      @RequestParam int size,
                                      @RequestParam String brand,
                                      @RequestParam int price,
                                      @RequestParam("file") MultipartFile file,
                                      Model model){

        productService.handleFileUploadTARGET(file);
        //productService.handleFileUploadSRC(file);
        String filePathToSave = productService.convertImagePath(file);
        Products p = productService.addNewProduct(name,color,size,brand,price,filePathToSave);

        model.addAttribute("added_product",p);
        return "/added_product_confirmation_page";
    }
}
