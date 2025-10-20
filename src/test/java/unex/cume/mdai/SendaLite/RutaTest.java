package unex.cume.mdai.SendaLite;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.model.Usuario;
import unex.cume.mdai.SendaLite.model.Valoracion;
import unex.cume.mdai.SendaLite.model.Dificultad;
import unex.cume.mdai.SendaLite.model.TipoActividad;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY) // usar BD embebida (H2) para tests aislados
public class RutaTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testPersistirRutaConValoraciones() {
		Usuario user = new Usuario();
		user.setEmail("autor.valoracion@example.com");
		user.setPassword("pwd");
		user.setNombre("Autor Valoracion");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		entityManager.persist(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta con valoraciones");
		ruta.setDescripcion("Descripción");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		ruta.setValoraciones(new ArrayList<>());

		Valoracion v = new Valoracion();
		v.setPuntuacion(8);
		v.setFechaValoracion(LocalDate.now());
		v.setUsuario(user);
		v.setRuta(ruta);
		ruta.getValoraciones().add(v);

		entityManager.persist(ruta);
		entityManager.flush();
		entityManager.clear();

		List<Valoracion> valoraciones = entityManager.getEntityManager()
				.createQuery("SELECT v FROM Valoracion v", Valoracion.class)
				.getResultList();
		assertThat(valoraciones).hasSize(1);
		assertThat(valoraciones.get(0).getPuntuacion()).isEqualTo(8);
	}

	@Test
	void testEliminarRutaEliminaValoraciones() {
		Usuario user = new Usuario();
		user.setEmail("autor.del@example.com");
		user.setPassword("pwd");
		user.setNombre("Autor Del");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		entityManager.persist(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta a eliminar");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		ruta.setValoraciones(new ArrayList<>());

		Valoracion v1 = new Valoracion();
		v1.setPuntuacion(7);
		v1.setFechaValoracion(LocalDate.now());
		v1.setUsuario(user);
		v1.setRuta(ruta);
		ruta.getValoraciones().add(v1);

		entityManager.persist(ruta);
		entityManager.flush();
		entityManager.clear();

		Ruta persisted = entityManager.getEntityManager()
				.createQuery("SELECT r FROM Ruta r WHERE r.titulo = :t", Ruta.class)
				.setParameter("t", "Ruta a eliminar")
				.getSingleResult();
		Long rutaId = persisted.getIdRuta();

		entityManager.remove(persisted);
		entityManager.flush();
		entityManager.clear();

		List<Valoracion> after = entityManager.getEntityManager()
				.createQuery("SELECT v FROM Valoracion v WHERE v.ruta.idRuta = :rid", Valoracion.class)
				.setParameter("rid", rutaId)
				.getResultList();
		assertThat(after).isEmpty();
	}

	@Test
	void testCrearRutaBasica() {
		Usuario user = new Usuario();
		user.setEmail("basicruta@example.com");
		user.setPassword("pwd");
		user.setNombre("Basic Ruta");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		entityManager.persist(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta Basica");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		entityManager.persist(ruta);
		entityManager.flush();
		entityManager.clear();

		List<Ruta> found = entityManager.getEntityManager()
				.createQuery("SELECT r FROM Ruta r WHERE r.titulo = :t", Ruta.class)
				.setParameter("t", "Ruta Basica")
				.getResultList();
		assertThat(found).hasSize(1);
		assertThat(found.get(0).getTitulo()).isEqualTo("Ruta Basica");
	}

	@Test
	void testEliminarRutaBasica() {
		Usuario user = new Usuario();
		user.setEmail("delruta@example.com");
		user.setPassword("pwd");
		user.setNombre("Del Ruta");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		entityManager.persist(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta a borrar Base");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		entityManager.persist(ruta);
		entityManager.flush();
		Long id = ruta.getIdRuta();

		entityManager.clear();

		Ruta toRemove = entityManager.getEntityManager().find(Ruta.class, id);
		entityManager.remove(toRemove);
		entityManager.flush();

		Ruta shouldBeNull = entityManager.getEntityManager().find(Ruta.class, id);
		assertThat(shouldBeNull).isNull();
	}

	@Test
	void testModificarRutaBasica() {
		Usuario user = new Usuario();
		user.setEmail("modruta@example.com");
		user.setPassword("pwd");
		user.setNombre("Mod Ruta");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		entityManager.persist(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta Original");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		entityManager.persist(ruta);
		entityManager.flush();

		// modificar mientras está gestionada
		ruta.setTitulo("Ruta Modificada");
		entityManager.flush();
		Long id = ruta.getIdRuta();
		entityManager.clear();

		Ruta found = entityManager.getEntityManager().find(Ruta.class, id);
		assertThat(found.getTitulo()).isEqualTo("Ruta Modificada");
	}
}
