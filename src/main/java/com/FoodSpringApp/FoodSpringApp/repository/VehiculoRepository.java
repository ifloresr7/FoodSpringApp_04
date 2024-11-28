package com.FoodSpringApp.FoodSpringApp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FoodSpringApp.FoodSpringApp.model.Vehiculo;
@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    
}