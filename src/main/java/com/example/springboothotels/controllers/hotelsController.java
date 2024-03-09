package com.example.springboothotels.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.springboothotels.models.Hotel;
import java.util.List;

@RestController
public class hotelsController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping(value = "/getHotels")
    public List<Hotel> getHotels() {
        TypedQuery<Hotel> query = entityManager.createQuery("SELECT h from Hotel h", Hotel.class);
        return query.getResultList();
    }

    @Transactional
    @PostMapping(value = "/addHotel", consumes = "application/json")
    public Hotel addHotel(@RequestBody Hotel hotel) {
        entityManager.persist(hotel);
        return hotel;
    }
}
