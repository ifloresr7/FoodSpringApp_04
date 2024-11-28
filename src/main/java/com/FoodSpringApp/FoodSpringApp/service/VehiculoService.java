package com.FoodSpringApp.FoodSpringApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FoodSpringApp.FoodSpringApp.model.Vehiculo;
import com.FoodSpringApp.FoodSpringApp.repository.VehiculoRepository;

@Service
public class VehiculoService{ 

    @Autowired
    private VehiculoRepository  vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }
 
    public Vehiculo save(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }
    
    public List<Vehiculo> obtenerTodosVehiculos() {
        return vehiculoRepository.findAll();
    }
    
    public List<Vehiculo> findAll() {
        return vehiculoRepository.findAll();
    }



    public Vehiculo findById(int id) {
        return vehiculoRepository.findById(id).orElse(null);
    }

/**
 * 
 * @param id identificador unico del vehiculo
 * @param vehiculoData con los datos a actualizar
 * @return
 */
    public Vehiculo update(int id, Vehiculo vehiculoData) {
  
        try {
            Vehiculo vehiculoExistente = vehiculoRepository.findById(id).orElse(null);
            if (vehiculoExistente == null) {
                return null;
            }
 
                vehiculoExistente.setAutonomia_km(vehiculoData.getAutonomia_km());
                vehiculoExistente.setColor(vehiculoData.getColor());
                vehiculoExistente.setMarca(vehiculoData.getMarca());
                vehiculoExistente.setMatricula(vehiculoData.getMatricula());
                vehiculoExistente.setPotencia_cv(vehiculoData.getPotencia_cv());
                vehiculoExistente.setPuertas(vehiculoData.getPuertas());
                vehiculoExistente.setPrecio_dia(vehiculoData.getPrecio_dia());
                
                return vehiculoRepository.save(vehiculoExistente);
 
        } catch (Exception e) {
            String serror=     e.getMessage();
            System.err.println(serror);
          }
        return null; // O lanza una excepción si no existe
    }

    /**
     * por su id si existe se borra
     * @param id
     */
    public void deleteById(int id) {
        vehiculoRepository.deleteById(id);
    }

}
