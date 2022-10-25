package com.example.sparesstrore.security;

import com.example.sparesstrore.dto.LinkDto;
import com.example.sparesstrore.model.Role;
import com.example.sparesstrore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LinkService {

    @Autowired
    private UserRepository userRepository;
    public List<LinkDto> getLinkList() {

        List<LinkDto> linkDtos = new ArrayList<>();
        linkDtos.add(new LinkDto("главная страница","/main"));
        linkDtos.add(new LinkDto("вход","/auth/login"));
        if (isAdmin()) {
            linkDtos.add(new LinkDto("управление деталями","/spares"));
            linkDtos.add(new LinkDto("управление машинами","/cars"));
            linkDtos.add(new LinkDto("управление категориями","/category/find-all"));
            linkDtos.add(new LinkDto("управление заказами","/order/admin/find-all"));
        } else {
            linkDtos.add(new LinkDto("корзина","/order/" + getCurrentUserId()));
        }
       return linkDtos;
    }

    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public long getCurrentUserId() {
        return userRepository.findByEmail(getCurrentUsername())
                .orElseThrow()
                .getUserId();
    }

    private boolean isAdmin() {
        return userRepository.findByEmail(getCurrentUsername()).orElseThrow().getRole().equals(Role.ADMIN);
    }
}
