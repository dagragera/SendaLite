package unex.cume.mdai.SendaLite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import unex.cume.mdai.SendaLite.service.RutaService;
import unex.cume.mdai.SendaLite.service.ComentarioService;
import unex.cume.mdai.SendaLite.service.ValoracionService;
import unex.cume.mdai.SendaLite.service.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@Controller
public class ViewController implements CommandLineRunner {

    private final RutaService rutaService;
    private final ComentarioService comentarioService;
    private final ValoracionService valoracionService;
    private final UsuarioService usuarioService;

    private final Logger logger = LoggerFactory.getLogger(ViewController.class);

    @Autowired
    public ViewController(RutaService rutaService, ComentarioService comentarioService, ValoracionService valoracionService, UsuarioService usuarioService) {
        this.rutaService = rutaService;
        this.comentarioService = comentarioService;
        this.valoracionService = valoracionService;
        this.usuarioService = usuarioService;
        logger.info("ViewController inicializado");
    }

    @Override
    public void run(String... args) {
        try {
            int count = rutaService.listAll() != null ? rutaService.listAll().size() : 0;
            logger.info("Startup: hay {} rutas (consulta realizada en CommandLineRunner)", count);
        } catch (Exception e) {
            logger.warn("No fue posible obtener el conteo de rutas en startup: {}", e.toString());
        }
    }

    @GetMapping({"/", ""})
    public String index(Model model) {
        model.addAttribute("rutas", rutaService.listAll());
        return "index";
    }

    // Redirección para enlaces antiguos que apuntaban a /static
    @GetMapping("/static")
    public String staticRedirect() {
        return "redirect:/";
    }

    @GetMapping("/rutas/{id}")
    public String rutaDetalle(@PathVariable Long id, Model model) {
        try {
            // Intentar cargar la ruta con todos sus detalles (autor, comentarios, valoraciones)
            var opt = rutaService.buscarConDetalles(id);
            if (opt.isPresent()) {
                var ruta = opt.get();
                model.addAttribute("ruta", ruta);
                model.addAttribute("comentarios", ruta.getComentarios());
                model.addAttribute("valoraciones", ruta.getValoraciones());
            } else {
                model.addAttribute("ruta", null);
                model.addAttribute("comentarios", comentarioService.listByRuta(id));
                model.addAttribute("valoraciones", valoracionService.listByRuta(id));
            }
            model.addAttribute("usuarios", usuarioService.listAll());
            model.addAttribute("media", valoracionService.averageForRuta(id));
            return "ruta";
        } catch (Exception ex) {
            logger.error("Error al obtener detalle de ruta {}: {}", id, ex.toString(), ex);
            model.addAttribute("status", 500);
            model.addAttribute("error", "Internal Server Error");
            model.addAttribute("message", ex.getMessage());
            return "error";
        }
    }

    // Lista de usuarios (vista)
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listAll());
        return "usuarios";
    }

    // Detalle de usuario (vista)
    @GetMapping("/usuarios/{id}")
    public String usuarioDetalle(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.findById(id).orElse(null));
        // opcional: añadir rutas/valoraciones/comentarios del usuario si se requieren
        return "usuario";
    }

    // Formulario para crear una nueva ruta
    @GetMapping("/rutas/nueva")
    public String nuevaRuta(Model model) {
        model.addAttribute("ruta", new unex.cume.mdai.SendaLite.model.Ruta());
        model.addAttribute("usuarios", usuarioService.listAll());
        return "ruta_form";
    }

    // Formulario para editar una ruta existente
    @GetMapping("/rutas/{id}/editar")
    public String editarRuta(@PathVariable Long id, Model model) {
        model.addAttribute("ruta", rutaService.findById(id).orElse(null));
        model.addAttribute("usuarios", usuarioService.listAll());
        return "ruta_form";
    }

    // Página de diagnóstico rápida usando Thymeleaf (comprueba que el motor de plantillas renderiza)
    @GetMapping("/debug")
    public String debugIndex(Model model) {
        model.addAttribute("mensajeDebug", "Thymeleaf render OK");
        return "debug-index";
    }

    // Endpoint de diagnóstico rápido
    @GetMapping("/__health")
    @ResponseBody
    public String health() {
        return "OK";
    }
}
