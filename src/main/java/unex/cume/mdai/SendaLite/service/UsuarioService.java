package unex.cume.mdai.SendaLite.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import unex.cume.mdai.SendaLite.model.Usuario;
import unex.cume.mdai.SendaLite.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;

    public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public Usuario create(Usuario u) {
        if (u.getEmail() == null || u.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email requerido");
        }
        if (usuarioRepo.findByEmail(u.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        if (u.getFechaRegistro() == null) {
            u.setFechaRegistro(LocalDate.now());
        }
        return usuarioRepo.save(u);
    }

    public List<Usuario> listAll() {
        return usuarioRepo.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepo.findById(id);
    }

    public Usuario update(Long id, Usuario updated) {
        return usuarioRepo.findById(id).map(existing -> {
            existing.setNombre(updated.getNombre());
            existing.setAvatar(updated.getAvatar());
            existing.setActivo(updated.isActivo());
            // no actualizamos email/password aquí por simplicidad
            return usuarioRepo.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    public void delete(Long id) {
        usuarioRepo.deleteById(id);
    }
}

