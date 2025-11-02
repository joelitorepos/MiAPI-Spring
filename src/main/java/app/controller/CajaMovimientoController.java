package app.controller;

import app.dao.CajaMovimientoDAO;
import app.model.CajaMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cajamovimiento")
public class CajaMovimientoController {

    private final CajaMovimientoDAO cajaMovimientoDAO;

    @Autowired
    public CajaMovimientoController(CajaMovimientoDAO cajaMovimientoDAO) {
        this.cajaMovimientoDAO = cajaMovimientoDAO;
    }

    @PostMapping
    public ResponseEntity<Integer> insertar(@RequestBody CajaMovimiento m) {
        try {
            int id = cajaMovimientoDAO.insertar(m);
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

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> actualizar(@PathVariable int id, @RequestBody CajaMovimiento m) {
        try {
            m.setId(id); // Asegurar que el ID coincida
            boolean actualizado = cajaMovimientoDAO.actualizar(m);
            return ResponseEntity.ok(actualizado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CajaMovimiento>> listar() {
        try {
            List<CajaMovimiento> movimientos = cajaMovimientoDAO.listar();
            return ResponseEntity.ok(movimientos);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CajaMovimiento> buscarPorId(@PathVariable int id) {
        try {
            CajaMovimiento movimiento = cajaMovimientoDAO.buscarPorId(id);
            if (movimiento != null) {
                return ResponseEntity.ok(movimiento);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/porfecha")
    public ResponseEntity<List<CajaMovimiento>> listarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
        try {
            List<CajaMovimiento> movimientos = cajaMovimientoDAO.listarPorFecha(fecha);
            return ResponseEntity.ok(movimientos);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/saldodiario")
    public ResponseEntity<BigDecimal> calcularSaldoDiario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
        try {
            BigDecimal saldo = cajaMovimientoDAO.calcularSaldoDiario(fecha);
            return ResponseEntity.ok(saldo);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}