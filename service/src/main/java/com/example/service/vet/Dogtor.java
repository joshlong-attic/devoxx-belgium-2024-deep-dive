package com.example.service.vet;

import com.example.service.adoptions.DogAdoptionEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class Dogtor {

    @EventListener
    void checkup(DogAdoptionEvent dogAdoptionEvent) {
        System.out.println("checking up on [" + dogAdoptionEvent + "]");
    }
}
