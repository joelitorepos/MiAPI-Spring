package app.controller;

import app.dao.AuditoriaDAO;
import app.model.Auditoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    private final AuditoriaDAO auditoriaDAO;

    @Autowired
    public AuditoriaController(AuditoriaDAO auditoriaDAO) {
        this.auditoriaDAO = auditoriaDAO;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auditoria> buscarPorId(@PathVariable int id) {
        try {
            Auditoria auditoria = auditoriaDAO.buscarPorId(id);
            if (auditoria != null) {
                return ResponseEntity.ok(auditoria);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Auditoria>> listarConFiltros(
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(required = false) String modulo,
            @RequestParam(required = false) String accion,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            List<Auditoria> auditorias = auditoriaDAO.listarConFiltros(idUsuario, modulo, accion, fechaInicio, fechaFin);
            return ResponseEntity.ok(auditorias);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}