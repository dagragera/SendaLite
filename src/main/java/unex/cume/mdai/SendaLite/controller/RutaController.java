package unex.cume.mdai.SendaLite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.service.RutaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/rutas")
public class RutaController {

    private final RutaService rutaService;
    private final Logger logger = LoggerFactory.getLogger(RutaController.class);

    @Autowired
    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
        logger.info("RutaController inicializado");
    }

    @PostMapping
    public ResponseEntity<Ruta> create(@RequestBody Ruta ruta) {
        Ruta saved = rutaService.create(ruta);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Ruta> list(@RequestParam(value = "q", required = false) String q) {
        if (q != null && !q.isBlank()) {
            return rutaService.searchByTitulo(q);
        }
        return rutaService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ruta> get(@PathVariable Long id) {
        // Usar buscarConDetalles para traer autor, comentarios y valoraciones ya inicializados
        return rutaService.buscarConDetalles(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ruta> update(@PathVariable Long id, @RequestBody Ruta ruta) {
        try {
            Ruta updated = rutaService.modificarRuta(ruta);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rutaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
