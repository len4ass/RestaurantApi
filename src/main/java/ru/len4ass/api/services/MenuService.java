package ru.len4ass.api.services;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import ru.len4ass.api.models.menu.Menu;
import ru.len4ass.api.repositories.DishRepository;

@Service
public class MenuService {
    @Resource
    private final DishRepository dishRepository;

    public MenuService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public Menu getMenu() {
        var allAvailableDishes = dishRepository.findDishesByQuantityAfter(0);
        return new Menu(allAvailableDishes);
    }
}
