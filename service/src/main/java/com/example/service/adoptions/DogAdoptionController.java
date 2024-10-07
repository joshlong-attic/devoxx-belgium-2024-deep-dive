package com.example.service.adoptions;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@Controller
@Transactional
@ResponseBody
class DogAdoptionController {

    private final DogRepository dogRepository;

    private final ApplicationEventPublisher publisher;

    DogAdoptionController(DogRepository dogRepository, ApplicationEventPublisher publisher) {
        this.dogRepository = dogRepository;
        this.publisher = publisher;
    }

    @GetMapping("/")
    Collection<Dog> getDogs() {
        return this.dogRepository.findAll();
    }

    @PostMapping("/dogs/{dogId}/adoptions")
    void adopt(@PathVariable int dogId,
               @RequestBody Map<String, String> owner) {
        this.dogRepository
                .findById(dogId)
                .ifPresent(dog -> {
                    var newDog = this.dogRepository.save(new Dog(
                            dog.id(), dog.name(),
                            dog.description(),
                            owner.get("name")
                    ));
                    this.publisher.publishEvent(new DogAdoptionEvent(newDog.id()));
                    System.out.println("adopted [" + newDog + "]");
                });
    }
}

interface DogRepository extends ListCrudRepository<Dog, Integer> {
}

record Dog(@Id int id, String name, String description, String owner) {
}