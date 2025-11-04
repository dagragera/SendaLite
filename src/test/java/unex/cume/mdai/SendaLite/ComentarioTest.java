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
import org.springframework.context.annotation.Import;

import unex.cume.mdai.SendaLite.model.Comentario;
import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.model.Usuario;
import unex.cume.mdai.SendaLite.model.Dificultad;
import unex.cume.mdai.SendaLite.model.TipoActividad;
import unex.cume.mdai.SendaLite.service.ComentarioService;
import unex.cume.mdai.SendaLite.service.RutaService;
import unex.cume.mdai.SendaLite.service.UsuarioService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // ajusta si quieres H2 en memoria
@Import({ComentarioService.class, RutaService.class, UsuarioService.class})
public class ComentarioTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ComentarioService comentarioService;

	@Autowired
	private RutaService rutaService;

	@Autowired
	private UsuarioService usuarioService;

	@Test
	void testPersistirComentarioYConsulta() {
		Usuario user = new Usuario();
		user.setEmail("coment@example.com");
		user.setPassword("pwd");
		user.setNombre("Usuario Coment");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		usuarioService.anadirUsuario(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta Comentarios");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		ruta.setComentarios(new ArrayList<>());
		rutaService.anadirRuta(ruta);

		Comentario c = new Comentario();
		c.setTexto("Comentario de prueba");
		c.setFechaComentario(LocalDate.now());
		c.setUsuario(user);
		c.setRuta(ruta);
		// persistir usando el servicio
		comentarioService.anadirComentario(c);
		entityManager.getEntityManager().flush();
		entityManager.clear();

		List<Comentario> resultados = entityManager.getEntityManager()
				.createQuery("SELECT c FROM Comentario c WHERE c.ruta.titulo = :t", Comentario.class)
				.setParameter("t", "Ruta Comentarios")
				.getResultList();
		assertThat(resultados).hasSize(1);
		assertThat(resultados.get(0).getTexto()).isEqualTo("Comentario de prueba");
	}

	@Test
	void testEliminarRutaEliminaComentarios() {
		Usuario user = new Usuario();
		user.setEmail("delcoment@example.com");
		user.setPassword("pwd");
		user.setNombre("Usuario Del");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		usuarioService.anadirUsuario(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta a borrar comentarios");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		ruta.setComentarios(new ArrayList<>());
		rutaService.anadirRuta(ruta);

		Comentario c1 = new Comentario();
		c1.setTexto("Comentario 1");
		c1.setFechaComentario(LocalDate.now());
		c1.setUsuario(user);
		c1.setRuta(ruta);
		comentarioService.anadirComentario(c1);
		entityManager.getEntityManager().flush();
		entityManager.clear();

		Ruta persisted = entityManager.getEntityManager()
				.createQuery("SELECT r FROM Ruta r WHERE r.titulo = :t", Ruta.class)
				.setParameter("t", "Ruta a borrar comentarios")
				.getSingleResult();
		Long rutaId = persisted.getIdRuta();

		rutaService.eliminarRuta(persisted);
		entityManager.getEntityManager().flush();
		entityManager.clear();

		List<Comentario> after = entityManager.getEntityManager()
				.createQuery("SELECT c FROM Comentario c WHERE c.ruta.idRuta = :rid", Comentario.class)
				.setParameter("rid", rutaId)
				.getResultList();
		assertThat(after).isEmpty();
	}

	@Test
	void testCrearComentarioBasico() {
		Usuario user = new Usuario();
		user.setEmail("basiccomment@example.com");
		user.setPassword("pwd");
		user.setNombre("Basic Commenter");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		usuarioService.anadirUsuario(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta Basic Comment");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		ruta.setComentarios(new ArrayList<>());
		rutaService.anadirRuta(ruta);

		Comentario c = new Comentario();
		c.setTexto("Comentario básico");
		c.setFechaComentario(LocalDate.now());
		c.setUsuario(user);
		c.setRuta(ruta);
		comentarioService.anadirComentario(c);
		entityManager.getEntityManager().flush();
		entityManager.clear();

		List<Comentario> results = entityManager.getEntityManager()
				.createQuery("SELECT c FROM Comentario c WHERE c.texto = :txt", Comentario.class)
				.setParameter("txt", "Comentario básico")
				.getResultList();
		assertThat(results).hasSize(1);
		assertThat(results.get(0).getTexto()).isEqualTo("Comentario básico");
	}

	@Test
	void testEliminarComentarioBasico() {
		Usuario user = new Usuario();
		user.setEmail("delcommentuser@example.com");
		user.setPassword("pwd");
		user.setNombre("Del Commenter");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		usuarioService.anadirUsuario(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta Del Comment");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		ruta.setComentarios(new ArrayList<>());
		rutaService.anadirRuta(ruta);

		Comentario c = new Comentario();
		c.setTexto("A eliminar");
		c.setFechaComentario(LocalDate.now());
		c.setUsuario(user);
		c.setRuta(ruta);
		comentarioService.anadirComentario(c);
		entityManager.getEntityManager().flush();
		Long id = c.getIdComentario();

		entityManager.clear();

		Comentario toRemove = entityManager.getEntityManager().find(Comentario.class, id);
		comentarioService.eliminarComentario(toRemove);
		entityManager.getEntityManager().flush();

		Comentario shouldBeNull = entityManager.getEntityManager().find(Comentario.class, id);
		assertThat(shouldBeNull).isNull();
	}

	@Test
	void testModificarComentarioBasico() {
		Usuario user = new Usuario();
		user.setEmail("modcommentuser@example.com");
		user.setPassword("pwd");
		user.setNombre("Mod Commenter");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		usuarioService.anadirUsuario(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta Mod Comment");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
        ruta.setDificultad(Dificultad.MEDIA);
        ruta.setTipoActividad(TipoActividad.SENDERISMO);
		ruta.setAutor(user);
		ruta.setComentarios(new ArrayList<>());
		rutaService.anadirRuta(ruta);

		Comentario c = new Comentario();
		c.setTexto("Original");
		c.setFechaComentario(LocalDate.now());
		c.setUsuario(user);
		c.setRuta(ruta);
		comentarioService.anadirComentario(c);
		entityManager.getEntityManager().flush();

		// modificar mediante servicio
		c.setTexto("Modificado");
		comentarioService.modificarComentario(c);
		Long id = c.getIdComentario();
		entityManager.clear();

		Comentario found = entityManager.getEntityManager().find(Comentario.class, id);
		assertThat(found.getTexto()).isEqualTo("Modificado");
	}
}
