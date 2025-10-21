package unex.cume.mdai.SendaLite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.service.RutaService;

@RestController
@RequestMapping("/api/rutas")
public class RutaController {

    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
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
        return rutaService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rutaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

