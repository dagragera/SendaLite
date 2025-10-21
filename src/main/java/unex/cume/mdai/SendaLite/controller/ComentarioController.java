package unex.cume.mdai.SendaLite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import unex.cume.mdai.SendaLite.model.Comentario;
import unex.cume.mdai.SendaLite.service.ComentarioService;

@RestController
@RequestMapping("/api/rutas/{rutaId}/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping
    public ResponseEntity<Comentario> create(@PathVariable Long rutaId, @RequestBody Map<String, Object> body) {
        Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
        String texto = body.get("texto").toString();
        Comentario created = comentarioService.create(rutaId, usuarioId, texto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Comentario> list(@PathVariable Long rutaId) {
        return comentarioService.listByRuta(rutaId);
    }

    @PutMapping("/{comentarioId}")
    public ResponseEntity<Comentario> update(@PathVariable Long rutaId, @PathVariable Long comentarioId, @RequestBody Map<String, Object> body) {
        try {
            String texto = body.get("texto").toString();
            Comentario updated = comentarioService.update(comentarioId, texto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{comentarioId}")
    public ResponseEntity<Void> delete(@PathVariable Long rutaId, @PathVariable Long comentarioId) {
        comentarioService.delete(comentarioId);
        return ResponseEntity.noContent().build();
    }
}

