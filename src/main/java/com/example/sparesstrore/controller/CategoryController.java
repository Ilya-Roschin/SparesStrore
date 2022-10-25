package com.example.sparesstrore.controller;

import com.example.sparesstrore.dto.LinkDto;
import com.example.sparesstrore.entity.Category;
import com.example.sparesstrore.entity.Spare;
import com.example.sparesstrore.repository.CategoryRepository;
import com.example.sparesstrore.repository.SpareRepository;
import com.example.sparesstrore.security.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SpareRepository spareRepository;

    @Autowired
    private LinkService linkService;

    @GetMapping("/category/find-all")
    public String getCategoriesPage(Map<String, Object> model) {
        Iterable<Category> categories =categoryRepository.findAll();
        model.put("categories", categories);
        Iterable<LinkDto> links = linkService.getLinkList();
        model.put("links",links);
        return "categories";
    }


    @PostMapping("/category/create")
    public String addCategory(@RequestParam String categoryName) {
        categoryRepository.save(new Category(categoryName));
        return "redirect:/category/find-all";
    }

    @PostMapping("/category/delete/{id}")
    public String deleteById(@PathVariable final long id) {
        categoryRepository.deleteById(id);
        return "redirect:/category/find-all";
    }

}
