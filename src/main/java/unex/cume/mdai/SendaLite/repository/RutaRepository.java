package unex.cume.mdai.SendaLite.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import unex.cume.mdai.SendaLite.model.Ruta;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
}
