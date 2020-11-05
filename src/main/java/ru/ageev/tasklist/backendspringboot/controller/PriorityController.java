package ru.ageev.tasklist.backendspringboot.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ageev.tasklist.backendspringboot.entity.Priority;
import ru.ageev.tasklist.backendspringboot.repo.PriorityRepository;

import java.util.List;


@RequestMapping("/priority")
@RestController
public class PriorityController {

    private PriorityRepository priorityRepository;


    public PriorityController(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    @GetMapping("/test")
    public List<Priority> test() {
        List<Priority> priorityList = priorityRepository.findAll();
        System.out.println("list = " + priorityList);
        return priorityList;
    }

}
