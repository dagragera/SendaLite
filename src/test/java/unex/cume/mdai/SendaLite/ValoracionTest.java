package unex.cume.mdai.SendaLite;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import unex.cume.mdai.SendaLite.model.Ruta;
import unex.cume.mdai.SendaLite.model.Usuario;
import unex.cume.mdai.SendaLite.model.Valoracion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // ajusta si quieres H2 en memoria
public class ValoracionTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void testUniqueUserRutaConstraint() {
		Usuario user = new Usuario();
		user.setEmail("unique@example.com");
		user.setPassword("pwd");
		user.setNombre("Unique User");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		entityManager.persist(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta Unique");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
		ruta.setAutor(user);
		ruta.setValoraciones(new ArrayList<>());
		entityManager.persist(ruta);

		Valoracion v1 = new Valoracion();
		v1.setPuntuacion(9);
		v1.setFechaValoracion(LocalDate.now());
		v1.setUsuario(user);
		v1.setRuta(ruta);
		entityManager.persist(v1);
		entityManager.flush();

		Valoracion v2 = new Valoracion();
		v2.setPuntuacion(8);
		v2.setFechaValoracion(LocalDate.now());
		v2.setUsuario(user);
		v2.setRuta(ruta);
		entityManager.persist(v2);

		Assertions.assertThrows(PersistenceException.class, () -> {
			entityManager.flush();
		});
	}

	@Test
	void testPersistValoracionAndQuery() {
		Usuario user = new Usuario();
		user.setEmail("persist@example.com");
		user.setPassword("pwd");
		user.setNombre("Persist User");
		user.setFechaRegistro(LocalDate.now());
		user.setActivo(true);
		entityManager.persist(user);

		Ruta ruta = new Ruta();
		ruta.setTitulo("Ruta Persist");
		ruta.setDescripcion("Desc");
		ruta.setFechaCreacion(LocalDate.now());
		ruta.setActiva(true);
		ruta.setAutor(user);
		ruta.setValoraciones(new ArrayList<>());
		entityManager.persist(ruta);

		Valoracion v = new Valoracion();
		v.setPuntuacion(7);
		v.setFechaValoracion(LocalDate.now());
		v.setUsuario(user);
		v.setRuta(ruta);
		entityManager.persist(v);

		entityManager.flush();
		entityManager.clear();

		List<Valoracion> list = entityManager.getEntityManager()
				.createQuery("SELECT v FROM Valoracion v WHERE v.usuario.email = :email", Valoracion.class)
				.setParameter("email", "persist@example.com")
				.getResultList();
		assertThat(list).hasSize(1);
		assertThat(list.get(0).getPuntuacion()).isEqualTo(7);
	}
}
