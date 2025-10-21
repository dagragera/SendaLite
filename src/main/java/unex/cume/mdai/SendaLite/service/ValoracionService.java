package unex.cume.mdai.SendaLite.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.DoubleSummaryStatistics;
import unex.cume.mdai.SendaLite.model.Valoracion;
import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.model.Usuario;
import unex.cume.mdai.SendaLite.repository.ValoracionRepository;
import unex.cume.mdai.SendaLite.repository.RutaRepository;
import unex.cume.mdai.SendaLite.repository.UsuarioRepository;

@Service
@Transactional
public class ValoracionService {

    private final ValoracionRepository valorRepo;
    private final RutaRepository rutaRepo;
    private final UsuarioRepository usuarioRepo;

    public ValoracionService(ValoracionRepository valorRepo, RutaRepository rutaRepo, UsuarioRepository usuarioRepo) {
        this.valorRepo = valorRepo;
        this.rutaRepo = rutaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public Valoracion upsert(Long idRuta, Long idUsuario, int puntuacion) {
        if (puntuacion < 1 || puntuacion > 10) {
            throw new IllegalArgumentException("Puntuación debe estar entre 1 y 10");
        }
        Ruta ruta = rutaRepo.findById(idRuta).orElseThrow(() -> new IllegalArgumentException("Ruta no encontrada"));
        Usuario usuario = usuarioRepo.findById(idUsuario).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Optional<Valoracion> existing = valorRepo.findByUsuarioIdUsuarioAndRutaIdRuta(idUsuario, idRuta);
        Valoracion v;
        if (existing.isPresent()) {
            v = existing.get();
            v.setPuntuacion(puntuacion);
            v.setFechaValoracion(LocalDate.now());
        } else {
            v = new Valoracion();
            v.setRuta(ruta);
            v.setUsuario(usuario);
            v.setPuntuacion(puntuacion);
            v.setFechaValoracion(LocalDate.now());
        }
        return valorRepo.save(v);
    }

    public double averageForRuta(Long idRuta) {
        List<Valoracion> list = valorRepo.findByRutaIdRuta(idRuta);
        if (list.isEmpty()) return 0.0;
        DoubleSummaryStatistics stats = list.stream().mapToDouble(Valoracion::getPuntuacion).summaryStatistics();
        return stats.getAverage();
    }

    public List<Valoracion> listByRuta(Long idRuta) {
        return valorRepo.findByRutaIdRuta(idRuta);
    }

    public void delete(Long idValoracion) {
        valorRepo.deleteById(idValoracion);
    }
}

