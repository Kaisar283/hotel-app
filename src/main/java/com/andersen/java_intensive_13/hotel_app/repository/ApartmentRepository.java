package com.andersen.java_intensive_13.hotel_app.repository;


import com.andersen.java_intensive_13.hotel_app.model.Apartment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {

    @Query("SELECT a FROM Apartment a WHERE a.isReserved = false")
    public List<Apartment> findAllAvailableApartments(PageRequest pageRequest);
}
