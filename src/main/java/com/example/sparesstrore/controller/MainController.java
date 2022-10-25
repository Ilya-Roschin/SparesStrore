package com.example.sparesstrore.controller;

import com.example.sparesstrore.dto.LinkDto;
import com.example.sparesstrore.entity.Car;
import com.example.sparesstrore.model.Role;
import com.example.sparesstrore.repository.CarRepository;
import com.example.sparesstrore.repository.CategoryRepository;
import com.example.sparesstrore.repository.UserRepository;
import com.example.sparesstrore.security.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LinkService linkService;

    @GetMapping("/main")
    public String getCars(Map<String, Object> model) {
        Iterable<Car> cars = carRepository.findAll();
        model.put("cars", cars);

        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);

        List<LinkDto> carsLinksDtos = new ArrayList<>();
        for (Car element : cars) {
            carsLinksDtos.add(new LinkDto(element.getBrand() + " " + element.getCarName(), "/cars/" + element.getCarId() + "/spares",element.getCarUrl()));
        }
        model.put("carLink", carsLinksDtos);

        return "main";
    }
}