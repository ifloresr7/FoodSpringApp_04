package com.FoodSpringApp.FoodSpringApp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.FoodSpringApp.FoodSpringApp.model.Alquiler;
import com.FoodSpringApp.FoodSpringApp.model.Usuario;
import com.FoodSpringApp.FoodSpringApp.model.Vehiculo;
import com.FoodSpringApp.FoodSpringApp.service.AlquilerService;
import com.FoodSpringApp.FoodSpringApp.service.UsuarioService;
import com.FoodSpringApp.FoodSpringApp.service.VehiculoService;

@Controller
public class AppController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private VehiculoService vehiculoService;
    @Autowired
    private AlquilerService alquilerService;

    private String version = "2024.11.24.21.35";

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("version", this.version);
        model.addAttribute("title", "Página de Inicio");
        model.addAttribute("description", "¡Bienvenido a FoodSpringApp!");
        model.addAttribute("currentPage", "home");
        model.addAttribute("role", obtenerRoleDeUsuario());
        return "home";
    }
    
    @GetMapping("/vehiculos")
    public String vehiculosPage(Model model) {
        model.addAttribute("version", this.version);
        model.addAttribute("vehiculos", vehiculoService.obtenerTodosVehiculos());
        model.addAttribute("title", "Nuestros mejores vehiculos");
        model.addAttribute("description", "Aquí puedes ver todos los vehículos disponibles para alquilar 🐈.");
        model.addAttribute("currentPage", "vehiculos");
        model.addAttribute("role", obtenerRoleDeUsuario());
        return "vehiculos";
    }

    @GetMapping("/mi-perfil")
    public String perfilPage(Model model) {
        String dni = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            dni = ((User) principal).getUsername();
        } else if (principal instanceof String) {
            dni = principal.toString();
        }
        Usuario usuario = usuarioService.obtenerUsuarioPorDni(dni);
        model.addAttribute("usuario", usuario);
        model.addAttribute("version", this.version);
        model.addAttribute("title", "Mi perfil");
        model.addAttribute("description", "Información de mi perfil.");
        model.addAttribute("currentPage", "mi-perfil");
        model.addAttribute("role", obtenerRoleDeUsuario());
        return "mi_perfil";
    }

    @GetMapping("/mis-alquileres")
    public String misAlquileresPage(Model model) {
        String dni = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Obtener el DNI del usuario autenticado
        if (principal instanceof User) {
            dni = ((User) principal).getUsername();
        } else if (principal instanceof String) {
            dni = principal.toString();
        }

        // Obtener el usuario autenticado por su DNI
        Usuario usuario = usuarioService.obtenerUsuarioPorDni(dni);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado con DNI: " + dni);
        }

        int usuarioID = usuario.getId(); // Asignar el ID del usuario

        // Obtener los alquileres del usuario
        List<Alquiler> alquileres = alquilerService.obtenerAlquileresPorCliente(usuarioID);

        // Obtener todos los vehículos para mapear información
        Map<Integer, Vehiculo> mapaVehiculos = vehiculoService.obtenerTodosVehiculos().stream()
            .collect(Collectors.toMap(Vehiculo::getId, vehiculo -> vehiculo));

        // Crear lista de alquileres con detalles del vehículo
        List<Map<String, Object>> alquileresConDetalles = alquileres.stream().map(alquiler -> {
            Map<String, Object> detalles = new HashMap<>();
            detalles.put("id", alquiler.getId());
            detalles.put("fechaInicio", alquiler.getFechaInicio());
            detalles.put("fechaFin", alquiler.getFechaFin());
            detalles.put("precio", alquiler.getPrecio());

            // Agregar información del vehículo
            Vehiculo vehiculo = mapaVehiculos.get(alquiler.getVehiculoId());
            if (vehiculo != null) {
                detalles.put("vehiculoInfo", vehiculo.getMarca() + " (" + vehiculo.getMatricula() + ")");
            } else {
                detalles.put("vehiculoInfo", "Vehículo no encontrado");
            }

            return detalles;
        }).collect(Collectors.toList());

        // Agregar atributos al modelo
        model.addAttribute("usuarioID", usuarioID);
        model.addAttribute("alquileres", alquileresConDetalles);
        model.addAttribute("version", this.version);
        model.addAttribute("title", "Mis alquileres");
        model.addAttribute("description", "Estos son todos mis alquileres.");
        model.addAttribute("currentPage", "mis-alquileres");
        model.addAttribute("role", obtenerRoleDeUsuario());

        return "mis_alquileres";
    }

    @GetMapping("/gestion-alquileres")
    public String alquileresPage(Model model) {
        // Obtener todos los alquileres, usuarios y vehículos
        List<Alquiler> alquileres = alquilerService.obtenerTodosAlquileres();
        List<Usuario> usuarios = usuarioService.obtenerTodosUsuarios();
        List<Vehiculo> vehiculos = vehiculoService.obtenerTodosVehiculos();

        // Mapear los usuarios y vehículos para acceso rápido por ID
        Map<Integer, Usuario> mapaUsuarios = usuarios.stream()
            .collect(Collectors.toMap(Usuario::getId, usuario -> usuario));
        Map<Integer, Vehiculo> mapaVehiculos = vehiculos.stream()
            .collect(Collectors.toMap(Vehiculo::getId, vehiculo -> vehiculo));

        // Agregar los detalles directamente al modelo (la vista accede a ellos)
        model.addAttribute("alquileres", alquileres);
        model.addAttribute("mapaUsuarios", mapaUsuarios);
        model.addAttribute("mapaVehiculos", mapaVehiculos);

        // Otros atributos del modelo
        model.addAttribute("version", this.version);
        model.addAttribute("vehiculos", vehiculos);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("title", "Gestión de alquileres");
        model.addAttribute("description", "Aquí puedes ver todos los alquileres.");
        model.addAttribute("currentPage", "gestion-alquileres");
        model.addAttribute("role", obtenerRoleDeUsuario());
        
        return "gestion_alquileres";
    }


    @GetMapping("/gestion-usuarios")
    public String usuariosPage(Model model) {
        model.addAttribute("version", this.version);
        model.addAttribute("usuarios", usuarioService.obtenerTodosUsuarios());
        model.addAttribute("title", "Gestión de usuarios");
        model.addAttribute("description", "Aquí puedes ver todos los usuarios.");
        model.addAttribute("currentPage", "gestion-usuarios");
        model.addAttribute("role", obtenerRoleDeUsuario());
        return "gestion_usuarios";
    }

    @GetMapping("/gestion-vehiculos")
    public String gestionVehiculosPage(Model model) {
        model.addAttribute("vehiculos", vehiculoService.obtenerTodosVehiculos());
        model.addAttribute("version", this.version);
        model.addAttribute("title", "Gestión de Vehículos");
        model.addAttribute("description", "Aquí puedes ver todos los vehículos.");
        model.addAttribute("currentPage", "gestion-vehiculos");
        model.addAttribute("role", obtenerRoleDeUsuario());
        return "gestion_vehiculos";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("version", this.version);
        model.addAttribute("title", "Login");
        model.addAttribute("description", "Inicia sesión.");
        model.addAttribute("currentPage", "login");
        model.addAttribute("role", obtenerRoleDeUsuario());
        return "login";
    }

    @GetMapping("/registro")
    public String registroPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("version", this.version);
        model.addAttribute("title", "Registro");
        model.addAttribute("description", "Registrate.");
        model.addAttribute("currentPage", "registro");
        model.addAttribute("role", obtenerRoleDeUsuario());
        return "registro";
    }

    private String obtenerRoleDeUsuario() {
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = "ROLE_ANONYMOUS";
        if (authentication != null && authentication.isAuthenticated()) {
            role = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("ROLE_ANONYMOUS");
        }
        return role;
    }
}