// src/main/java/unex/cume/mdai/SendaLite/service/RutaService.java
package unex.cume.mdai.SendaLite.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.repository.RutaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @Transactional
    public Ruta anadirRuta(Ruta ruta) {
        if (ruta == null) throw new IllegalArgumentException("Ruta nula");
        if (ruta.getFechaCreacion() == null) ruta.setFechaCreacion(LocalDate.now());
        return rutaRepository.save(ruta);
    }

    // Wrappers para el controller
    @Transactional
    public Ruta create(Ruta ruta) {
        return anadirRuta(ruta);
    }

    @Transactional
    public List<Ruta> listAll() {
        return rutaRepository.findAll();
    }

    @Transactional
    public Optional<Ruta> findById(Long id) {
        return rutaRepository.findById(id);
    }

    @Transactional
    public List<Ruta> searchByTitulo(String q) {
        return rutaRepository.findByTituloContainingIgnoreCase(q);
    }

    @Transactional
    public void delete(Long id) {
        eliminarRutaPorId(id);
    }

    @Transactional
    public Ruta modificarRuta(Ruta ruta) {
        if (ruta == null || ruta.getIdRuta() == null) throw new IllegalArgumentException("Ruta o id nulo");
        ruta.setFechaActualizacion(LocalDate.now());
        // usar saveAndFlush para asegurar persistencia inmediata en tests
        return rutaRepository.saveAndFlush(ruta);
    }

    @Transactional
    public void eliminarRuta(Ruta ruta) {
        rutaRepository.delete(ruta);
    }

    @Transactional
    public boolean eliminarRutaPorId(Long idRuta) {
        if (idRuta == null) return false;
        Optional<Ruta> r = rutaRepository.findById(idRuta);
        if (r.isEmpty()) return false;
        rutaRepository.delete(r.get());
        return true;
    }

    @Transactional
    public List<Ruta> listarRutas() {
        return rutaRepository.findAll();
    }

    @Transactional
    public Optional<Ruta> buscarPorId(Long id) {
        return rutaRepository.findById(id);
    }
}