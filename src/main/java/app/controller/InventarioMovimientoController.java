package app.controller;

import app.dao.InventarioMovimientoDAO;
import app.model.InventarioMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/inventariomovimiento")
public class InventarioMovimientoController {

    private final InventarioMovimientoDAO inventarioMovimientoDAO;

    @Autowired
    public InventarioMovimientoController(InventarioMovimientoDAO inventarioMovimientoDAO) {
        this.inventarioMovimientoDAO = inventarioMovimientoDAO;
    }

    @PostMapping
    public ResponseEntity<Integer> insertar(@RequestBody InventarioMovimiento m) {
        try {
            int id = inventarioMovimientoDAO.insertar(m);
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
    public ResponseEntity<List<InventarioMovimiento>> listar() {
        try {
            List<InventarioMovimiento> movimientos = inventarioMovimientoDAO.listar();
            return ResponseEntity.ok(movimientos);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioMovimiento> buscarPorId(@PathVariable int id) {
        try {
            InventarioMovimiento movimiento = inventarioMovimientoDAO.buscarPorId(id);
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
}