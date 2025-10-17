package unex.cume.mdai.SendaLite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import unex.cume.mdai.SendaLite.model.Comentario;
import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.model.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // ajusta si quieres H2 en memoria
public class ComentarioTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testPersistComentarioAndQuery() {
		Usuario user = new Usuario();
		user.setEmail("coment@example.com");
		user.setPassword("pwd");
		user.setNombre("Usuario Coment");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		entityManager.persist(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta Comentarios");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
		ruta.setAutor(user);
		ruta.setComentarios(new ArrayList<>());
		entityManager.persist(ruta);

		Comentario c = new Comentario();
		c.setTexto("Comentario de prueba");
		c.setFechaComentario(LocalDate.now());
		c.setUsuario(user);
		c.setRuta(ruta);
		// Añadir al listado de la ruta para que se persista por cascade al guardar la ruta
		ruta.getComentarios().add(c);

		// persistir vía ruta para mantener la misma estrategia usada en otros tests
		entityManager.persist(ruta);
		entityManager.flush();
		entityManager.clear();

		List<Comentario> resultados = entityManager.getEntityManager()
				.createQuery("SELECT c FROM Comentario c WHERE c.ruta.titulo = :t", Comentario.class)
				.setParameter("t", "Ruta Comentarios")
				.getResultList();
		assertThat(resultados).hasSize(1);
		assertThat(resultados.get(0).getTexto()).isEqualTo("Comentario de prueba");
	}

	@Test
	void testRemoveRutaRemovesComentarios() {
		Usuario user = new Usuario();
		user.setEmail("delcoment@example.com");
		user.setPassword("pwd");
		user.setNombre("Usuario Del");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		entityManager.persist(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta a borrar comentarios");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
		ruta.setAutor(user);
		ruta.setComentarios(new ArrayList<>());

		Comentario c1 = new Comentario();
		c1.setTexto("Comentario 1");
		c1.setFechaComentario(LocalDate.now());
		c1.setUsuario(user);
		c1.setRuta(ruta);
		ruta.getComentarios().add(c1);

		entityManager.persist(ruta);
		entityManager.flush();
		entityManager.clear();

		Ruta persisted = entityManager.getEntityManager()
				.createQuery("SELECT r FROM Ruta r WHERE r.titulo = :t", Ruta.class)
				.setParameter("t", "Ruta a borrar comentarios")
				.getSingleResult();
		Long rutaId = persisted.getIdRuta();

		entityManager.remove(persisted);
		entityManager.flush();
		entityManager.clear();

		List<Comentario> after = entityManager.getEntityManager()
				.createQuery("SELECT c FROM Comentario c WHERE c.ruta.idRuta = :rid", Comentario.class)
				.setParameter("rid", rutaId)
				.getResultList();
		assertThat(after).isEmpty();
	}
}
