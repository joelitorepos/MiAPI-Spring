package app.controller;

import app.dao.MultaDAO;
import app.model.Multa;
import app.model.MultaConDetalles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/multa")
public class MultaController {

    private final MultaDAO multaDAO;

    @Autowired
    public MultaController(MultaDAO multaDAO) {
        this.multaDAO = multaDAO;
    }

    @PostMapping
    public ResponseEntity<Integer> insertar(@RequestBody Multa m) {
        try {
            int id = multaDAO.insertar(m);
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
    public ResponseEntity<List<Multa>> listar() {
        try {
            List<Multa> multas = multaDAO.listar();
            return ResponseEntity.ok(multas);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Multa> buscarPorId(@PathVariable int id) {
        try {
            Multa multa = multaDAO.buscarPorId(id);
            if (multa != null) {
                return ResponseEntity.ok(multa);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> actualizar(@PathVariable int id, @RequestBody Multa m) {
        try {
            m.setId(id); // Asegurar que el ID coincida
            boolean actualizado = multaDAO.actualizar(m);
            return ResponseEntity.ok(actualizado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/totalpendienteporcliente")
    public ResponseEntity<BigDecimal> getTotalPendientePorCliente(@RequestParam int idCliente) {
        try {
            BigDecimal total = multaDAO.getTotalPendientePorCliente(idCliente);
            return ResponseEntity.ok(total);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/totalrecaudado")
    public ResponseEntity<BigDecimal> calcularTotalRecaudado(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fechaFin) {
        try {
            BigDecimal total = multaDAO.calcularTotalRecaudado(fechaInicio, fechaFin);
            return ResponseEntity.ok(total);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pagadasporperiodo")
    public ResponseEntity<List<MultaConDetalles>> listarPagadasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date fechaFin) {
        try {
            List<MultaConDetalles> multas = multaDAO.listarPagadasPorPeriodo(fechaInicio, fechaFin);
            return ResponseEntity.ok(multas);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}