package com.example.sparesstrore.controller;

import com.example.sparesstrore.dto.LinkDto;
import com.example.sparesstrore.entity.Car;
import com.example.sparesstrore.entity.Category;
import com.example.sparesstrore.entity.Spare;
import com.example.sparesstrore.repository.CarRepository;
import com.example.sparesstrore.repository.CategoryRepository;
import com.example.sparesstrore.repository.SpareRepository;
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
import java.util.stream.Collectors;

@Controller
public class SpareController {

    @Autowired
    private SpareRepository spareRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private LinkService linkService;

    @Autowired
    private FileService fileService;

    @GetMapping("/spares")
    public String getSpares(Map<String, Object> model) {
        Iterable<Spare> spares = spareRepository.findAll();
        Iterable<Category> categories = categoryRepository.findAll();
        Iterable<Car> cars = carRepository.findAll();
        model.put("spares", spares);
        model.put("categories", categories);
        model.put("cars", cars);
        List linkDtos = new ArrayList<>();
        for (Category element: categories) {
            linkDtos.add(new LinkDto(element.getCategoryName(),"/category/products/" + element.getCategoryId()));
        }
        model.put("categoryLink", linkDtos);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "spares_admin";
    }

    @PostMapping("/spares")
    public String addSpare(@RequestParam String name, @RequestParam int price, @RequestParam MultipartFile file, @RequestParam long categoryId, @RequestParam long carId, Map<String, Object> model) {
        Spare spare = new Spare(name, price);
        spare.setCategory(categoryRepository.findById(categoryId).orElseThrow());
        spare.setCar(carRepository.findById(carId).orElseThrow());
        spareRepository.save(spare);
        fileService.addImageToSpare(spare.getSpareId(),file);
        getSpares(model);
        return "redirect:/spares";
    }

    @PostMapping("/spares/delete/{id}")
    public String deleteById(@PathVariable final long id, Map<String, Object> model) {
        spareRepository.deleteById(id);
        getSpares(model);
        return "redirect:/spares";
    }

    @GetMapping("/spares/update/{id}")
    public String updateSpare(@PathVariable final long id, Map<String, Object> model) {
        Spare spare = spareRepository.findById(id).orElseThrow();
        model.put("spare",spare);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "updateProduct";
    }

    @PostMapping("/spares/update/{id}")
    public String updateById(@PathVariable final long id,@RequestParam String name,
                             @RequestParam int price,@RequestParam MultipartFile file,
                             Map<String, Object> model) {
        Spare spare = spareRepository.findById(id).orElseThrow();
        spare.setSpareName(name);
        spare.setPrice(price);
        spareRepository.save(spare);
        fileService.addImageToSpare(spare.getSpareId(),file);
        return "redirect:/spares";
    }


    @GetMapping("/cars/{id}/spares")
    public String getSparesByCar(@PathVariable final long id, Map<String, Object> model) {
        Iterable<Spare> spares = spareRepository.findAll()
                .stream()
                .filter(spare -> spare.getCar().getCarId().equals(id))
                .collect(Collectors.toList());
        Iterable<Category> categories = categoryRepository.findAll();
        Iterable<Car> cars = carRepository.findAll();
        model.put("spares", spares);
        model.put("categories", categories);
        model.put("cars", cars);
        List linkDtos = new ArrayList<>();
        model.put("categoryLink", linkDtos);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "spares_user";
    }

}
