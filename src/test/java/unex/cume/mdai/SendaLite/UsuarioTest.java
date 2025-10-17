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
@AutoConfigureTestDatabase(replace = Replace.NONE) // usa configuración real si tienes Testcontainers o DB en ejecución; elimina si quieres DB en memoria
public class UsuarioTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testCascadePersistFromRutaToComentario() {
		// Crear y persistir usuario (autor)
		Usuario user = new Usuario();
		// ...ajusta setters según tu entidad Usuario...
		user.setEmail("autor@example.com");
		user.setPassword("pwd");
		user.setNombre("Autor Prueba");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		entityManager.persist(user);

		// Crear ruta y asignar autor
		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta de prueba");
		ruta.setDescripcion("Descripción");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
		ruta.setAutor(user);
		ruta.setComentarios(new ArrayList<>());

		// Crear comentario asociado a la ruta y al usuario
		Comentario c = new Comentario();
		c.setTexto("Buen camino");
		c.setFechaComentario(LocalDate.now());
		c.setUsuario(user);
		c.setRuta(ruta);
		ruta.getComentarios().add(c);

		// Persistir la ruta; los comentarios deben persistirse por cascade
		entityManager.persist(ruta);
		entityManager.flush();
		entityManager.clear();

		// Comprobar que el comentario se guardó
		List<Comentario> comentarios = entityManager.getEntityManager()
				.createQuery("SELECT c FROM Comentario c", Comentario.class)
				.getResultList();
		assertThat(comentarios).hasSize(1);
		assertThat(comentarios.get(0).getTexto()).isEqualTo("Buen camino");
	}

	@Test
	void testCascadeRemoveFromRutaToComentario() {
		// Crear y persistir usuario (autor)
		Usuario user = new Usuario();
		user.setEmail("autor2@example.com");
		user.setPassword("pwd2");
		user.setNombre("Autor Prueba 2");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		entityManager.persist(user);

		// Crear ruta con comentario y persistir
		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta a eliminar");
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

		// Recuperar ruta, eliminarla y comprobar que los comentarios también se eliminan
		Ruta persistedRuta = entityManager.getEntityManager()
				.createQuery("SELECT r FROM Ruta r WHERE r.titulo = :t", Ruta.class)
				.setParameter("t", "Ruta a eliminar")
				.getSingleResult();
		Long rutaId = persistedRuta.getIdRuta();

		// eliminar
		entityManager.remove(persistedRuta);
		entityManager.flush();
		entityManager.clear();

		// No debe quedar ningún comentario para esa ruta
		List<Comentario> comentariosAfter = entityManager.getEntityManager()
				.createQuery("SELECT c FROM Comentario c WHERE c.ruta.idRuta = :rid", Comentario.class)
				.setParameter("rid", rutaId)
				.getResultList();
		assertThat(comentariosAfter).isEmpty();
	}
}
