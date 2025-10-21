package unex.cume.mdai.SendaLite.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import unex.cume.mdai.SendaLite.model.Comentario;
import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.model.Usuario;
import unex.cume.mdai.SendaLite.repository.ComentarioRepository;
import unex.cume.mdai.SendaLite.repository.RutaRepository;
import unex.cume.mdai.SendaLite.repository.UsuarioRepository;

@Service
@Transactional
public class ComentarioService {

    private final ComentarioRepository comentarioRepo;
    private final RutaRepository rutaRepo;
    private final UsuarioRepository usuarioRepo;

    public ComentarioService(ComentarioRepository comentarioRepo, RutaRepository rutaRepo, UsuarioRepository usuarioRepo) {
        this.comentarioRepo = comentarioRepo;
        this.rutaRepo = rutaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public Comentario create(Long idRuta, Long idUsuario, String texto) {
        Ruta ruta = rutaRepo.findById(idRuta).orElseThrow(() -> new IllegalArgumentException("Ruta no encontrada"));
        Usuario usuario = usuarioRepo.findById(idUsuario).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Comentario c = new Comentario();
        c.setRuta(ruta);
        c.setUsuario(usuario);
        c.setTexto(texto);
        c.setFechaComentario(LocalDate.now());
        return comentarioRepo.save(c);
    }

    public List<Comentario> listByRuta(Long idRuta) {
        return comentarioRepo.findByRutaIdRuta(idRuta);
    }

    public Optional<Comentario> findById(Long id) {
        return comentarioRepo.findById(id);
    }

    public Comentario update(Long idComentario, String nuevoTexto) {
        Comentario c = comentarioRepo.findById(idComentario).orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));
        c.setTexto(nuevoTexto);
        c.setFechaEdicion(LocalDate.now());
        return comentarioRepo.save(c);
    }

    public void delete(Long idComentario) {
        comentarioRepo.deleteById(idComentario);
    }
}

