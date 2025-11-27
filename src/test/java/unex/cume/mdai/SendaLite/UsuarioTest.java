package unex.cume.mdai.SendaLite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import unex.cume.mdai.SendaLite.model.Usuario;
import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.model.Comentario;
import unex.cume.mdai.SendaLite.model.Dificultad;
import unex.cume.mdai.SendaLite.model.TipoActividad;
import unex.cume.mdai.SendaLite.service.UsuarioService;
import unex.cume.mdai.SendaLite.service.RutaService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY) // usar BD embebida (H2) para tests aislados
@Import({UsuarioService.class, RutaService.class, TestConfig.class})
public class UsuarioTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private RutaService rutaService;

	@Test
	void testPersistenciaEnCascadaDeRutaAComentario() {
		// Crear y persistir usuario (autor)
		Usuario user = new Usuario();
		// ...ajusta setters según tu entidad Usuario...
		user.setEmail("autor@example.com");
		user.setPassword("pwd");
		user.setNombre("Autor Prueba");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		usuarioService.anadirUsuario(user);

		// Crear ruta y asignar autor
		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta de prueba");
		ruta.setDescripcion("Descripción");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		List<Comentario> comentarios = new ArrayList<>();
		ruta.setComentarios(comentarios);

		// Crear comentario asociado a la ruta y al usuario
		Comentario c = new Comentario();
		c.setTexto("Buen camino");
		c.setFechaComentario(LocalDate.now());
		c.setUsuario(user);
		c.setRuta(ruta);
		ruta.getComentarios().add(c);

		// Persistir la ruta; los comentarios deben persistirse por cascade
		rutaService.anadirRuta(ruta);
		entityManager.getEntityManager().flush();
		entityManager.clear();

		// Comprobar que el comentario se guardó
		java.util.List<Comentario> comentariosPersist = entityManager.getEntityManager()
                .createQuery("SELECT c FROM Comentario c WHERE c.ruta.titulo = :t", Comentario.class)
                .setParameter("t", "Ruta de prueba")
                .getResultList();
        assertThat(comentariosPersist).hasSize(1);
        assertThat(comentariosPersist.get(0).getTexto()).isEqualTo("Buen camino");
	}

	@Test
	void testEliminacionEnCascadaDeRutaAComentario() {
		// Crear y persistir usuario (autor)
		Usuario user = new Usuario();
		user.setEmail("autor2@example.com");
		user.setPassword("pwd2");
		user.setNombre("Autor Prueba 2");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		usuarioService.anadirUsuario(user);

		// Crear ruta con comentario y persistir
		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta a eliminar");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		List<Comentario> comentarios2 = new ArrayList<>();
		ruta.setComentarios(comentarios2);

		Comentario c1 = new Comentario();
		c1.setTexto("Comentario 1");
		c1.setFechaComentario(LocalDate.now());
		c1.setUsuario(user);
		c1.setRuta(ruta);
		ruta.getComentarios().add(c1);

		rutaService.anadirRuta(ruta);
		entityManager.getEntityManager().flush();
		entityManager.clear();

		// Recuperar ruta, eliminarla y comprobar que los comentarios también se eliminan
		Ruta persistedRuta = entityManager.getEntityManager()
				.createQuery("SELECT r FROM Ruta r WHERE r.titulo = :t", Ruta.class)
				.setParameter("t", "Ruta a eliminar")
				.getSingleResult();
		Long rutaId = persistedRuta.getIdRuta();

		// eliminar
		rutaService.eliminarRuta(persistedRuta);
		entityManager.getEntityManager().flush();
		entityManager.clear();

		// No debe quedar ningún comentario para esa ruta
		java.util.List<Comentario> comentariosAfter = entityManager.getEntityManager()
				.createQuery("SELECT c FROM Comentario c WHERE c.ruta.idRuta = :rid", Comentario.class)
				.setParameter("rid", rutaId)
				.getResultList();
		assertThat(comentariosAfter).isEmpty();
	}

	@Test
	void testCrearUsuarioBasico() {
		Usuario u = new Usuario();
		u.setEmail("basicuser@example.com");
		u.setPassword("pwd");
		u.setNombre("Basic User");
		u.setFechaRegistro(LocalDate.now());
		u.setActivo(true);
		usuarioService.anadirUsuario(u);
		entityManager.getEntityManager().flush();
		entityManager.clear();

		java.util.List<Usuario> found = entityManager.getEntityManager()
				.createQuery("SELECT u FROM Usuario u WHERE u.email = :e", Usuario.class)
				.setParameter("e", "basicuser@example.com")
				.getResultList();
		assertThat(found).hasSize(1);
		assertThat(found.get(0).getNombre()).isEqualTo("Basic User");
	}

	@Test
	void testEliminarUsuarioBasico() {
		Usuario u = new Usuario();
		u.setEmail("deluser@example.com");
		u.setPassword("pwd");
		u.setNombre("Del User");
		u.setFechaRegistro(LocalDate.now());
		u.setActivo(true);
		usuarioService.anadirUsuario(u);
		entityManager.getEntityManager().flush();
		entityManager.clear();

		Usuario persisted = entityManager.getEntityManager()
				.createQuery("SELECT u FROM Usuario u WHERE u.email = :e", Usuario.class)
				.setParameter("e", "deluser@example.com")
				.getSingleResult();
		usuarioService.eliminarUsuario(persisted);
		entityManager.getEntityManager().flush();
		entityManager.clear();

		java.util.List<Usuario> after = entityManager.getEntityManager()
				.createQuery("SELECT u FROM Usuario u WHERE u.email = :e", Usuario.class)
				.setParameter("e", "deluser@example.com")
				.getResultList();
		assertThat(after).isEmpty();
	}

	@Test
	void testModificarUsuarioBasico() {
		Usuario u = new Usuario();
		u.setEmail("moduser@example.com");
		u.setPassword("pwd");
		u.setNombre("Original Name");
		u.setFechaRegistro(LocalDate.now());
		u.setActivo(true);
		usuarioService.anadirUsuario(u);
		entityManager.getEntityManager().flush();

		// modificar mediante servicio
		u.setNombre("Nombre Modificado");
		usuarioService.modificarUsuario(u);
		entityManager.getEntityManager().flush();
		entityManager.clear();

		Usuario found = entityManager.getEntityManager()
				.createQuery("SELECT u FROM Usuario u WHERE u.email = :e", Usuario.class)
				.setParameter("e", "moduser@example.com")
				.getSingleResult();
		assertThat(found.getNombre()).isEqualTo("Nombre Modificado");
	}
}
