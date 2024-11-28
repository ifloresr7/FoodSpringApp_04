package com.FoodSpringApp.FoodSpringApp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.FoodSpringApp.FoodSpringApp.model.Vehiculo;
import com.FoodSpringApp.FoodSpringApp.service.VehiculoService;

@Controller
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

   @PostMapping("/save-vehiculo")
    public ResponseEntity<Map<String, String>> guardarVehiculo(@RequestBody Vehiculo vehiculo) {
        Map<String, String> response = new HashMap<>();
        try {
            Vehiculo nuevoVehiculo = vehiculoService.save(vehiculo);
            response.put("message", "vehiculo registrado con éxito.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Hubo un error al registrar el vehiculo: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }



    @PutMapping("/update-vehiculo")
    public ResponseEntity<Vehiculo> actualizarVehiculo(@RequestBody Vehiculo vehiculoData) {
        Vehiculo vehiculoActualizado = vehiculoService.update(vehiculoData.getId(), vehiculoData);
        System.out.println(vehiculoActualizado);
        if (vehiculoActualizado != null) {
            return ResponseEntity.ok(vehiculoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable int id) {
        if (vehiculoService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        vehiculoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
