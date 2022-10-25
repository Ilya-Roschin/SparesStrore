package com.example.sparesstrore.controller;

import com.example.sparesstrore.dto.LinkDto;
import com.example.sparesstrore.entity.Car;
import com.example.sparesstrore.entity.Category;
import com.example.sparesstrore.repository.CarRepository;
import com.example.sparesstrore.repository.CategoryRepository;
import com.example.sparesstrore.security.FileService;
import com.example.sparesstrore.security.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CarController {


    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LinkService linkService;

    @Autowired
    private FileService fileService;

    @GetMapping("/cars")
    public String getCars(Map<String, Object> model) {
        Iterable<Car> cars = carRepository.findAll();
        Iterable<Category> categories = categoryRepository.findAll();
        model.put("cars", cars);
        List linkDtos = new ArrayList<>();
        for (Category element: categories) {
            linkDtos.add(new LinkDto(element.getCategoryName(),"/category/products/" + element.getCategoryId()));
        }
        model.put("categoryLink", linkDtos);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "cars";
    }

    @PostMapping("/cars")
    public String addCar(@RequestParam String name, @RequestParam String brand, @RequestParam MultipartFile file, Map<String, Object> model) {
        Car car = new Car(name, brand);
        carRepository.save(car);
        fileService.addImageToCar(car.getCarId(),file);
        getCars(model);
        return "redirect:cars";
    }

    @PostMapping("/cars/delete/{id}")
    public String deleteCarById(@PathVariable final long id, Map<String, Object> model) {
        carRepository.deleteById(id);
        getCars(model);
        return "redirect:/cars";
    }

    @GetMapping("/cars/update/{id}")
    public String updateCar(@PathVariable final long id, Map<String, Object> model) {
        Car car = carRepository.findById(id).orElseThrow();
        model.put("car",car);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "updateCar";
    }

    @PostMapping("/cars/update/{id}")
    public String updateById(@PathVariable final long id,@RequestParam MultipartFile file,
                             @RequestParam String name, @RequestParam String brand) {
        Car car = carRepository.findById(id).orElseThrow();
        car.setCarName(name);
        car.setBrand(brand);
        carRepository.save(car);
        fileService.addImageToCar(car.getCarId(),file);
        return "redirect:/cars";
    }
}
