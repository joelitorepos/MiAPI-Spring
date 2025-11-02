package app.controller;

import app.dao.PrestamoDAO;
import app.model.ComboItem;
import app.model.Prestamo;
import app.model.PrestamoConDetalles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/prestamo")
public class PrestamoController {

    private final PrestamoDAO prestamoDAO;

    @Autowired
    public PrestamoController(PrestamoDAO prestamoDAO) {
        this.prestamoDAO = prestamoDAO;
    }

    @PostMapping
    public ResponseEntity<Integer> insertar(@RequestBody Prestamo p) {
        try {
            int id = prestamoDAO.insertar(p);
            if (id != -1) {
                return ResponseEntity.status(HttpStatus.CREATED).body(id);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Prestamo>> listar() {
        try {
            List<Prestamo> prestamos = prestamoDAO.listar();
            return ResponseEntity.ok(prestamos);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> buscarPorId(@PathVariable int id) {
        try {
            Prestamo prestamo = prestamoDAO.buscarPorId(id);
            if (prestamo != null) {
                return ResponseEntity.ok(prestamo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> actualizar(@PathVariable int id, @RequestBody Prestamo p) {
        try {
            p.setId(id); // Asegurar que el ID coincida
            boolean actualizado = prestamoDAO.actualizar(p);
            return ResponseEntity.ok(actualizado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/clientesactivos")
    public ResponseEntity<List<ComboItem>> listarClientesActivos() {
        try {
            List<ComboItem> clientes = prestamoDAO.listarClientesActivos();
            return ResponseEntity.ok(clientes);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/copiasdisponibles")
    public ResponseEntity<List<ComboItem>> listarCopiasDisponibles() {
        try {
            List<ComboItem> copias = prestamoDAO.listarCopiasDisponibles();
            return ResponseEntity.ok(copias);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/usuariosactivos")
    public ResponseEntity<List<ComboItem>> listarUsuariosActivos() {
        try {
            List<ComboItem> usuarios = prestamoDAO.listarUsuariosActivos();
            return ResponseEntity.ok(usuarios);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/vencidos")
    public ResponseEntity<List<Prestamo>> listarVencidos() {
        try {
            List<Prestamo> prestamos = prestamoDAO.listarVencidos();
            return ResponseEntity.ok(prestamos);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/porperiodo")
    public ResponseEntity<List<PrestamoConDetalles>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fechaFin) {
        try {
            List<PrestamoConDetalles> prestamos = prestamoDAO.listarPorPeriodo(fechaInicio, fechaFin);
            return ResponseEntity.ok(prestamos);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}