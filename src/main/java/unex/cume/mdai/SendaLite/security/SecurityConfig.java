package unex.cume.mdai.SendaLite.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // Usar NoOp durante desarrollo para aceptar contraseñas en texto plano (seed existente)
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Permitir acceso público a páginas de login/registro y recursos estáticos
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/register", "/login", "/css/**", "/js/**", "/img/**", "/webjars/**", "/h2-console/**", "/").permitAll()
                .anyRequest().authenticated()
        )
        // Form login configurado para usar nuestra página /login
        .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", true)
        )
        .logout(logout -> logout.permitAll());

        // Habilitar frames para H2 console (solo local/de desarrollo)
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }
}
