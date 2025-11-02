package app.controller;

import app.dao.ReservaDAO;
import app.model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/reserva")
public class ReservaController {

    private final ReservaDAO reservaDAO;

    @Autowired
    public ReservaController(ReservaDAO reservaDAO) {
        this.reservaDAO = reservaDAO;
    }

    @PostMapping
    public ResponseEntity<Integer> insertar(@RequestBody Reserva r) {
        try {
            int id = reservaDAO.insertar(r);
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
    public ResponseEntity<List<Reserva>> listar() {
        try {
            List<Reserva> reservas = reservaDAO.listar();
            return ResponseEntity.ok(reservas);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarPorId(@PathVariable int id) {
        try {
            Reserva reserva = reservaDAO.buscarPorId(id);
            if (reserva != null) {
                return ResponseEntity.ok(reserva);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> actualizar(@PathVariable int id, @RequestBody Reserva r) {
        try {
            r.setId(id); // Asegurar que el ID coincida
            boolean actualizado = reservaDAO.actualizar(r);
            return ResponseEntity.ok(actualizado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}