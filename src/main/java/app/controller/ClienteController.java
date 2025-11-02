package app.controller;

import app.dao.ClienteDAO;
import app.model.Cliente;
import app.model.ClienteConMultas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final ClienteDAO clienteDAO;

    @Autowired
    public ClienteController(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @PostMapping
    public ResponseEntity<Integer> insertar(@RequestBody Cliente c) {
        try {
            int id = clienteDAO.insertar(c);
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
    public ResponseEntity<List<Cliente>> listar() {
        try {
            List<Cliente> clientes = clienteDAO.listar();
            return ResponseEntity.ok(clientes);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable int id) {
        try {
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente != null) {
                return ResponseEntity.ok(cliente);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> actualizar(@PathVariable int id, @RequestBody Cliente c) {
        try {
            c.setId(id); // Asegurar que el ID coincida
            boolean actualizado = clienteDAO.actualizar(c);
            return ResponseEntity.ok(actualizado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/conmultaspendientes")
    public ResponseEntity<List<ClienteConMultas>> listarConMultasPendientes() {
        try {
            List<ClienteConMultas> clientes = clienteDAO.listarConMultasPendientes();
            return ResponseEntity.ok(clientes);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}