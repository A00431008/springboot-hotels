package com.example.springboothotels.controllers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    private boolean hotelExists(Hotel hotel) {
        TypedQuery<Hotel> query = entityManager.createQuery("SELECT h from Hotel h ", Hotel.class);
        List<Hotel> existingHotel = query.getResultList();
        for (Hotel hotelFromList : existingHotel) {
            if (hotel.getName().equals(hotelFromList.getName())) {
                return true;
            }
        }

        return false;
    }

    @Transactional
    @PostMapping(value = "/addHotel", consumes = "application/json")
    public ResponseEntity<String> addHotel(@RequestBody Hotel hotel) {
        try {
            if (hotelExists(hotel)) {
                String response = "Hotel with name \"" + hotel.getName() 
                    + "\" Already Exists !! Cannot add same hotel twice !!!";
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {
                entityManager.persist(hotel);
                String response = "Hotel Added Successfully !!!";
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Added Delete method to test
    @Transactional
    @DeleteMapping(value = "/deleteHotel/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        Hotel hotelToDelete = entityManager.find(Hotel.class, id);

        if (hotelToDelete == null) {
            return new ResponseEntity<>("Hotel not found with ID: " + id, HttpStatus.NOT_FOUND);
        }

        entityManager.remove(hotelToDelete);

        return new ResponseEntity<>("Hotel deleted successfully", HttpStatus.OK);
    }

}
