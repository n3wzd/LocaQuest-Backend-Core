package com.example.reviewsplash.repogitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.reviewsplash.model.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, String> {
    
}
