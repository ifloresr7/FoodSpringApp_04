package com.FoodSpringApp.FoodSpringApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FoodSpringApp.FoodSpringApp.model.Alquiler;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Integer> {
    List<Alquiler> findByClienteId(int clienteId);
}
