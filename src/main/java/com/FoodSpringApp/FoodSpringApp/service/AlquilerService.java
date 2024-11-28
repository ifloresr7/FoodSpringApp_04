package com.FoodSpringApp.FoodSpringApp.service;

import com.FoodSpringApp.FoodSpringApp.model.Alquiler;
import com.FoodSpringApp.FoodSpringApp.model.Vehiculo;
import com.FoodSpringApp.FoodSpringApp.repository.AlquilerRepository;
import com.FoodSpringApp.FoodSpringApp.repository.VehiculoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AlquilerService{

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    
    public List<Alquiler> obtenerTodosAlquileres() {
        return alquilerRepository.findAll();
    }

    public List<Alquiler> obtenerAlquileresPorCliente(int clienteId) {
        return alquilerRepository.findByClienteId(clienteId);
    }

    public Alquiler create(Alquiler alquilerData) {
        // Validar que los datos necesarios están presentes
        if (alquilerData.getClienteId() == 0 || alquilerData.getVehiculoId() == 0 ||
            alquilerData.getFechaInicio() == null || alquilerData.getFechaFin() == null) {
            throw new IllegalArgumentException("Datos incompletos para crear el alquiler.");
        }
    
        // Calcular la diferencia de días entre las fechas
        long diferenciaDias = ChronoUnit.DAYS.between(
            alquilerData.getFechaInicio().toInstant(),
            alquilerData.getFechaFin().toInstant()
        ) + 1; // Sumar 1 para incluir el día de inicio
    
        if (diferenciaDias <= 0) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha de fin.");
        }
    
        // Obtener el precio por día del vehículo
        Vehiculo vehiculo = vehiculoRepository.findById(alquilerData.getVehiculoId())
            .orElseThrow(() -> new RuntimeException("Vehículo no encontrado."));
    
        double precioTotal = diferenciaDias * vehiculo.getPrecio_dia();
    
        // Asignar el precio calculado al alquiler
        alquilerData.setPrecio(precioTotal);
    
        // Guardar el alquiler en la base de datos
        return alquilerRepository.save(alquilerData);
    } 
    
    public Alquiler update(Alquiler alquilerData) {
        // Buscar el alquiler existente utilizando el ID (int)
        Alquiler alquilerExistente = alquilerRepository.findById(alquilerData.getId())
                .orElseThrow(() -> new RuntimeException("Alquiler no encontrado."));
    
        // Validar las fechas
        if (alquilerData.getFechaInicio() == null || alquilerData.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias.");
        }
        long diferenciaDias = ChronoUnit.DAYS.between(
                alquilerData.getFechaInicio().toInstant(),
                alquilerData.getFechaFin().toInstant()
        ) + 1;
    
        if (diferenciaDias <= 0) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha de fin.");
        }
    
        // Obtener el precio por día del vehículo
        Vehiculo vehiculo = vehiculoRepository.findById(alquilerData.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado."));
    
        double precioTotal = diferenciaDias * vehiculo.getPrecio_dia();
    
        // Actualizar los datos del alquiler existente
        alquilerExistente.setClienteId(alquilerData.getClienteId());
        alquilerExistente.setVehiculoId(alquilerData.getVehiculoId());
        alquilerExistente.setFechaInicio(alquilerData.getFechaInicio());
        alquilerExistente.setFechaFin(alquilerData.getFechaFin());
        alquilerExistente.setPrecio(precioTotal);
    
        // Guardar el alquiler actualizado
        return alquilerRepository.save(alquilerExistente);
    }
    
    public void eliminar(int id) {
        Alquiler alquiler = alquilerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Alquiler no encontrado."));
        alquilerRepository.delete(alquiler);
    }
    
}
