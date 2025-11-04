package unex.cume.mdai.SendaLite.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import unex.cume.mdai.SendaLite.model.Comentario;
import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.model.Usuario;
import unex.cume.mdai.SendaLite.repository.ComentarioRepository;
import unex.cume.mdai.SendaLite.repository.RutaRepository;
import unex.cume.mdai.SendaLite.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final RutaRepository rutaRepository;
    private final UsuarioRepository usuarioRepository;

    public ComentarioService(ComentarioRepository comentarioRepository, RutaRepository rutaRepository, UsuarioRepository usuarioRepository) {
        this.comentarioRepository = comentarioRepository;
        this.rutaRepository = rutaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Comentario anadirComentario(Comentario comentario) {
        if (comentario == null) throw new IllegalArgumentException("Comentario nulo");
        if (comentario.getFechaComentario() == null) comentario.setFechaComentario(LocalDate.now());
        return comentarioRepository.save(comentario);
    }

    @Transactional
    public Comentario modificarComentario(Comentario comentario) {
        if (comentario == null || comentario.getIdComentario() == null) throw new IllegalArgumentException("Comentario o id nulo");
        comentario.setFechaEdicion(LocalDate.now());
        // usar saveAndFlush para garantizar que los cambios se escriben inmediatamente en la BD
        return comentarioRepository.saveAndFlush(comentario);
    }

    @Transactional
    public void eliminarComentario(Comentario comentario) {
        comentarioRepository.delete(comentario);
    }

    @Transactional
    public List<Comentario> listarPorRutaId(Long idRuta) {
        return comentarioRepository.findByRutaIdRuta(idRuta);
    }

    @Transactional
    public List<Comentario> listarPorUsuarioId(Long idUsuario) {
        return comentarioRepository.findByUsuarioIdUsuario(idUsuario);
    }

    @Transactional
    public Optional<Comentario> buscarPorId(Long id) {
        return comentarioRepository.findById(id);
    }

    // Métodos usados por ComentarioController
    @Transactional
    public Comentario create(Long rutaId, Long usuarioId, String texto) {
        if (rutaId == null || usuarioId == null || texto == null) throw new IllegalArgumentException("Parámetros insuficientes");
        Ruta ruta = rutaRepository.findById(rutaId).orElseThrow(() -> new IllegalArgumentException("Ruta no encontrada: " + rutaId));
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));
        Comentario c = new Comentario();
        c.setRuta(ruta);
        c.setUsuario(usuario);
        c.setTexto(texto);
        c.setFechaComentario(LocalDate.now());
        return comentarioRepository.save(c);
    }

    @Transactional
    public List<Comentario> listByRuta(Long rutaId) {
        return listarPorRutaId(rutaId);
    }

    @Transactional
    public Comentario update(Long comentarioId, String texto) {
        Comentario c = comentarioRepository.findById(comentarioId).orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado: " + comentarioId));
        c.setTexto(texto);
        c.setFechaEdicion(LocalDate.now());
        return comentarioRepository.saveAndFlush(c);
    }

    @Transactional
    public void delete(Long comentarioId) {
        comentarioRepository.findById(comentarioId).ifPresent(comentarioRepository::delete);
    }
}
