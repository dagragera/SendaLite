package unex.cume.mdai.SendaLite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import unex.cume.mdai.SendaLite.model.Valoracion;
import unex.cume.mdai.SendaLite.service.ValoracionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/rutas/{rutaId}/valoraciones")
public class ValoracionController {

    private final ValoracionService valoracionService;
    private final Logger logger = LoggerFactory.getLogger(ValoracionController.class);

    @Autowired
    public ValoracionController(ValoracionService valoracionService) {
        this.valoracionService = valoracionService;
        logger.info("ValoracionController inicializado");
    }

    @PostMapping
    public ResponseEntity<Valoracion> upsert(@PathVariable Long rutaId, @RequestBody Map<String, Object> body) {
        Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
        int puntuacion = Integer.parseInt(body.get("puntuacion").toString());
        Valoracion v = valoracionService.upsert(rutaId, usuarioId, puntuacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(v);
    }

    @GetMapping
    public List<Valoracion> list(@PathVariable Long rutaId) {
        return valoracionService.listByRuta(rutaId);
    }

    @GetMapping("/avg")
    public ResponseEntity<Double> avg(@PathVariable Long rutaId) {
        double avg = valoracionService.averageForRuta(rutaId);
        return ResponseEntity.ok(avg);
    }

    @DeleteMapping("/{valoracionId}")
    public ResponseEntity<Void> delete(@PathVariable Long rutaId, @PathVariable Long valoracionId) {
        valoracionService.delete(valoracionId);
        return ResponseEntity.noContent().build();
    }
}
