// src/main/java/unex/cume/mdai/SendaLite/service/RutaService.java
package unex.cume.mdai.SendaLite.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.repository.RutaRepository;
import unex.cume.mdai.SendaLite.repository.UsuarioRepository;
import unex.cume.mdai.SendaLite.model.Usuario;

@Service
@Transactional
public class RutaService {

    private final RutaRepository rutaRepo;
    private final UsuarioRepository usuarioRepo;

    public RutaService(RutaRepository rutaRepo, UsuarioRepository usuarioRepo) {
        this.rutaRepo = rutaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public Ruta create(Ruta ruta) {
        Usuario autor = ruta.getAutor();
        if (autor == null || autor.getIdUsuario() == null) {
            throw new IllegalArgumentException("Autor requerido");
        }
        Usuario found = usuarioRepo.findById(autor.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Autor no encontrado"));
        ruta.setAutor(found);
        return rutaRepo.save(ruta);
    }

    public List<Ruta> listAll() {
        return rutaRepo.findAll();
    }

    public Optional<Ruta> findById(Long id) {
        return rutaRepo.findById(id);
    }

    public void delete(Long id) {
        rutaRepo.deleteById(id);
    }

    public List<Ruta> searchByTitulo(String q) {
        return rutaRepo.findByTituloContainingIgnoreCase(q);
    }
}