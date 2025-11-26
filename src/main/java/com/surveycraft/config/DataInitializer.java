package com.surveycraft.config;

import com.surveycraft.entity.*;
import com.surveycraft.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final VideojuegoRepository videojuegoRepository;
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final ResponseRepository responseRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, CategoryRepository categoryRepository,
                           VideojuegoRepository videojuegoRepository, UserRepository userRepository,
                           SurveyRepository surveyRepository, QuestionRepository questionRepository,
                           OptionRepository optionRepository, ResponseRepository responseRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.videojuegoRepository = videojuegoRepository;
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.responseRepository = responseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // --- 🧹 LIMPIEZA TOTAL (SOLO PARA PRUEBAS) ---
        // Esto borra la encuesta vieja para que se pueda crear la nueva con preguntas
        responseRepository.deleteAll();
        optionRepository.deleteAll();
        questionRepository.deleteAll();
        surveyRepository.deleteAll();
        // ---------------------------------------------

        // 1. Roles
        Role rolePlayer = createRoleIfNotFound("ROLE_PLAYER");
        Role roleCrafter = createRoleIfNotFound("ROLE_CRAFTER");

        // 2. Categoría y Juego
        Category cat = createCategoryIfNotFound("General");
        Videojuego game = createGameIfNotFound("Minecraft", "Juego de cubos", cat);

        // 3. Usuarios
        User admin = createUserIfNotFound("admin@surveycraft.com", "123456", "AdminCrafter", roleCrafter);
        User jugador = createUserIfNotFound("player@test.com", "123456", "GamerTest", rolePlayer);

        // 4. Encuesta COMPLETA
        createFullSurvey(admin, jugador, game);
    }

    private void createFullSurvey(User creador, User jugador, Videojuego juego) {
        // Ya no necesitamos el 'if count > 0' porque acabamos de limpiar arriba

        // A) Crear la Encuesta
        Survey survey = new Survey();
        survey.setTitulo("Encuesta de Satisfacción 2025");
        survey.setDescripcion("Queremos saber qué opinas de la nueva actualización");
        survey.setEstado("ACTIVE");
        survey.setJuegoId(juego.getId());
        survey.setTipoEncuesta("JUEGO");
        survey.setCrafter(creador);
        Survey savedSurvey = surveyRepository.save(survey);

        // B) Crear Pregunta 1
        Question q1 = new Question();
        q1.setPreguntaTexto("¿Qué te pareció el rendimiento?");
        q1.setTipoPregunta("SINGLE_CHOICE");
        q1.setSurvey(savedSurvey);
        Question savedQ1 = questionRepository.save(q1);

        Option opt1A = createOption("Excelente", savedQ1);
        Option opt1B = createOption("Regular", savedQ1);
        Option opt1C = createOption("Malo", savedQ1);

        // C) Crear Pregunta 2
        Question q2 = new Question();
        q2.setPreguntaTexto("¿Recomendarías este juego?");
        q2.setTipoPregunta("SINGLE_CHOICE");
        q2.setSurvey(savedSurvey);
        Question savedQ2 = questionRepository.save(q2);

        Option opt2A = createOption("Sí, totalmente", savedQ2);
        Option opt2B = createOption("No", savedQ2);

        // D) SIMULAR VOTOS
        createVote(jugador, savedQ1, opt1A); // Voto por Excelente
        createVote(jugador, savedQ2, opt2A); // Voto por Sí
        
        createVote(creador, savedQ1, opt1B); // Voto por Regular

        System.out.println(">>> ¡BASE DE DATOS REINICIADA Y ENCUESTA CREADA! <<<");
    }

    // --- MÉTODOS AUXILIARES (Sin cambios) ---
    private Option createOption(String texto, Question q) {
        Option o = new Option();
        o.setOpcionTexto(texto);
        o.setQuestion(q);
        return optionRepository.save(o);
    }

    private void createVote(User player, Question q, Option o) {
        Response r = new Response();
        r.setPlayer(player);
        r.setQuestion(q);
        r.setSelectedOption(o);
        r.setSubmittedAt(LocalDateTime.now());
        r.setSessionId("auto-gen");
        responseRepository.save(r);
    }

    private Role createRoleIfNotFound(String roleName) {
        return roleRepository.findByNombre(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setNombre(roleName);
            return roleRepository.save(role);
        });
    }

    private Category createCategoryIfNotFound(String nombre) {
        return categoryRepository.findByNombreCategoria(nombre).orElseGet(() -> {
            Category c = new Category();
            c.setNombreCategoria(nombre);
            return categoryRepository.save(c);
        });
    }

    private Videojuego createGameIfNotFound(String titulo, String desc, Category cat) {
        return videojuegoRepository.findByTitulo(titulo).orElseGet(() -> {
            Videojuego g = new Videojuego();
            g.setTitulo(titulo);
            g.setDescripcion(desc);
            g.setCategoria(cat);
            return videojuegoRepository.save(g);
        });
    }

    private User createUserIfNotFound(String email, String password, String username, Role role) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setUsername(username);
            user.setRole(role);
            return userRepository.save(user);
        });
    }
}