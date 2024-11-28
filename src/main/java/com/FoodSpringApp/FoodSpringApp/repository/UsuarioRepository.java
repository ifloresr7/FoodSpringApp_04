package com.FoodSpringApp.FoodSpringApp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.FoodSpringApp.FoodSpringApp.model.Usuario;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u WHERE u.dni = :dni")
    Usuario findByDni(@Param("dni") String dni);
}