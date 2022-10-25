package com.example.sparesstrore.controller;

import com.example.sparesstrore.dto.LinkDto;
import com.example.sparesstrore.entity.Item;
import com.example.sparesstrore.entity.Purchase;
import com.example.sparesstrore.entity.Spare;
import com.example.sparesstrore.entity.User;
import com.example.sparesstrore.repository.ItemRepository;
import com.example.sparesstrore.repository.OrderRepository;
import com.example.sparesstrore.repository.SpareRepository;
import com.example.sparesstrore.repository.UserRepository;
import com.example.sparesstrore.security.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class OrderController {
    @Autowired
    private SpareRepository spareRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LinkService linkService;

    @PostMapping("/order/add-to-order/{productId}")
    public String addItemToOrder(@RequestParam int count, @PathVariable final long productId) {

        User user = userRepository.findByEmail(getCurrentUsername()).orElseThrow();

        if (user.getPurchases().stream()
                .filter(p -> p.getStatus().equals("Open"))
                .count() == 0) {
            orderRepository.save(new Purchase("Open", user));

            user = userRepository.findById(user.getUserId()).orElseThrow();
        }
        Purchase openPurshase = user.getPurchases().stream()
                .filter(p -> p.getStatus().equals("Open"))
                .findFirst()
                .orElseThrow();

        Spare spare = spareRepository.findById(productId).orElseThrow();
        itemRepository.save(new Item(count, spare, openPurshase));
        return "redirect:/cars/" + spare.getCar().getCarId() + "/spares";
    }

    @GetMapping("/order/{userId}")
    public String getUserOrder(@PathVariable long userId, Map<String, Object> model) {

        User user = userRepository.findByEmail(getCurrentUsername()).orElseThrow();

        if (user.getPurchases().stream()
                .filter(p -> p.getStatus().equals("Open"))
                .count() == 0) {
            orderRepository.save(new Purchase( "Open", user));

            user = userRepository.findById(user.getUserId())
                    .orElseThrow();
        }

        Iterable<Purchase> openPurchases = user.getPurchases().stream()
                .filter(p -> p.getStatus().equals("Open"))
                .collect(Collectors.toList());

        Iterable<Purchase> confirmedPurchases = user.getPurchases().stream()
                .filter(p -> p.getStatus().equals("Confirmed"))
                .collect(Collectors.toList());

        Iterable<Purchase> sentPurchases = user.getPurchases().stream()
                .filter(p -> p.getStatus().equals("Close"))
                .collect(Collectors.toList());

        model.put("openPurchases",openPurchases);
        model.put("confirmedPurchases",confirmedPurchases);
        model.put("sentPurchases",sentPurchases);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);

        return "order";
    }


    @PostMapping("/order/sent/{purchaseId}")
    public String sentOrder(@PathVariable final long purchaseId) {
        Purchase purchase = orderRepository.findById(purchaseId).orElseThrow();
        purchase.setStatus("Sent");
        orderRepository.save(purchase);
        return "redirect:/order/" + purchase.getUser().getUserId();
    }

    @PostMapping("/order/close/{purchaseId}")
    public String finishOrder(@PathVariable final long purchaseId) {
        Purchase purchase = orderRepository.findById(purchaseId).orElseThrow();
        purchase.setStatus("Close");
        orderRepository.save(purchase);
        return "redirect:/order/" + purchase.getUser().getUserId();
    }

    @PostMapping("/order/item/delete/{itemId}")
    public String deleteItem(@PathVariable final long itemId) {
        itemRepository.deleteById(itemId);
        return "redirect:/order/" + getCurrentUserId();
    }


    @GetMapping("/order/admin/find-all")
    public String getUserOrder(Map<String, Object> model) {

        List<Purchase> purchases = orderRepository.findAll();

        Iterable<Purchase> sentPurchases = purchases.stream().filter(p -> p.getStatus().equals("Sent"))
                .collect(Collectors.toList());


        Iterable<Purchase> confirmedPurchases = purchases.stream()
                .filter(p -> p.getStatus().equals("Confirmed"))
                .collect(Collectors.toList());

        Iterable<Purchase> closePurchases = purchases.stream()
                .filter(p -> p.getStatus().equals("Close"))
                .collect(Collectors.toList());

        model.put("closePurchases",closePurchases);
        model.put("confirmedPurchases",confirmedPurchases);
        model.put("sentPurchases",sentPurchases);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "order_admin";
    }

    @PostMapping("/order/admin/confirm/{purchaseId}")
    public String confirmOrder(@PathVariable final long purchaseId) {
        Purchase purchase = orderRepository.findById(purchaseId).orElseThrow();
        purchase.setStatus("Confirmed");
        orderRepository.save(purchase);
        return "redirect:/order/admin/find-all";
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public long getCurrentUserId() {
        return userRepository.findByEmail(getCurrentUsername())
                .orElseThrow()
                .getUserId();
    }
}
