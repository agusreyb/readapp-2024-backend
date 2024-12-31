package ar.edu.unsam.algo3.controller
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.dto.DatosAmigosDTO
import ar.edu.unsam.algo3.dto.UsuarioLoginDTO
import ar.edu.unsam.algo3.repositorio.UsuariosRepositorio
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.junit.jupiter.api.Assertions.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Dado un controller de Usuarios")
class UsuariosControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var usuariosRepositorio: UsuariosRepositorio


    @BeforeEach
    fun init() {
        usuariosRepositorio.apply {
            usuariosRepositorio.clear()

              val userPrueba1 =  Usuario(
                    "Valen",
                    "Perez",
                    "juancito",
                    "juanperez@gmail.com",
                    "password",
                    2002,
                    9,
                    12,
                    Lenguaje.Español,
                    200.0,
                    Poliglota(),
                    formaDeLeer = Promedio(),
                    "https://img.a.transfermarkt.technology/portrait/big/298613-1694431029.jpg?lm=1"
              )


            val userPrueba2 = Usuario(
                    "Maria",
                    "Gomez",
                    "mariagomez",
                    "mariagomez@gmail.com",
                    "password",
                    2006,
                    10,
                    7,
                    Lenguaje.Inglés,
                    250.0,
                    Precavido(),
                    Ansioso(),
                    "https://imagessl.casadellibro.com/img/autores/20083030.jpg"
            )

            create(userPrueba1)
            create(userPrueba2)

            create(Usuario(
                "User",
                "Admin",
                "admin",
                "admin@gmail.com",
                "password",
                2000,
                1,
                1,
                Lenguaje.Español,
                300.0,
                Poliglota(),
                formaDeLeer = Promedio(),
                "https://img.freepik.com/foto-gratis/cierre-dientes-hombre-moreno-sonriendo_1187-5800.jpg"
            ))
            create(Usuario(
                "Mariano",
                "Apellido Falso",
                "marianox34",
                "marianox34@gmail.com",
                "password",
                2000,
                1,
                1,
                Lenguaje.Español,
                150.0,
                Poliglota(),
                formaDeLeer = Promedio(),
                "https://img.freepik.com/foto-gratis/cierre-dientes-hombre-moreno-sonriendo_1187-5800.jpg"
            ))

        }
    }

    @Test
    fun `Se obtiene los datos del Usuario`() {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/usuarios/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Valen"))
            .andExpect(jsonPath("$.apellido").value("Perez"))
            .andExpect(jsonPath("$.username").value("juancito"))
            .andExpect(jsonPath("$.mail").value("juanperez@gmail.com"))
            .andExpect(jsonPath("$.fechaNacimiento").value("2002-09-12"))
            .andExpect(jsonPath("$.vpromedio").value(200))
            .andExpect(jsonPath("$.formaDeLeer.type").value("Promedio"))
            .andExpect(jsonPath("$.perfilLista").isArray)
            .andExpect(jsonPath("$.librosLeidos").isArray)
            .andExpect(jsonPath("$.librosPorLeer").isArray)
            .andExpect(jsonPath("$.autoresPreferidos").isArray)
            .andExpect(jsonPath("$.amigos").isArray)
            .andExpect(jsonPath("$.cantVecesLeido").isMap)
            .andExpect(jsonPath("$.imgperfil").value("https://img.a.transfermarkt.technology/portrait/big/298613-1694431029.jpg?lm=1"))
    }


    @Test
    fun `Login devuelve ID segun sus credenciales`() {

        val usuarioLogin = UsuarioLoginDTO(mail = "mariagomez@gmail.com", contrasenia = "password")
        val mapper = ObjectMapper()
        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(usuarioLogin))
            )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(2))

    }

    @Test
    fun `Login devuelve Mensaje Error si las credenciales son incorrectas`() {

        val usuarioLogin = UsuarioLoginDTO(mail = "maria@gmail.com", contrasenia = "contrasenia")
        val mapper = ObjectMapper()
        val errorMessage = mockMvc
            .perform(MockMvcRequestBuilders
                .post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(usuarioLogin))
            )
            .andExpect(status().isNotFound)
            .andReturn().resolvedException?.message
        assertEquals(errorMessage, "Usuario o contraseña incorrectos.")
    }

    @Test
    fun `Un amigo se agregar correctamente`() {

        val userId = 1

        val nuevosAmigos = listOf(
            DatosAmigosDTO(
                3,"User",
                "Admin",
                "admin",
                "https://img.freepik.com/foto-gratis/cierre-dientes-hombre-moreno-sonriendo_1187-5800.jpg"),
            DatosAmigosDTO(
                4,
                "Mariano",
                "Apellido Falso",
                "marianox34",
                "https://img.freepik.com/foto-gratis/cierre-dientes-hombre-moreno-sonriendo_1187-5800.jpg")
        )
        val mapper = ObjectMapper()
        val responseMessage = mockMvc
            .perform(MockMvcRequestBuilders
                .patch("/usuarios/${userId}/agregar-amigo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(nuevosAmigos))
            )
            .andExpect(status().isOk)
            //.andExpect(content().contentType("application/json"))
            .andReturn().response.contentAsString
        assertEquals("Amigos agregados exitosamente.", responseMessage)

        val usuarioActualizado = usuariosRepositorio.getByID(userId)
        val amigosAgregados = usuarioActualizado.amigos

        assertTrue(amigosAgregados.any { it.id == 3 },"El amigo con ID 3 no fue agregado a la lista.")
        assertTrue(amigosAgregados.any { it.id == 4 },"El amigo con ID 4 no fue agregado a la lista.")

    }


}















