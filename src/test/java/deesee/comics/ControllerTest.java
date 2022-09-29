package deesee.comics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import deesee.comics.dto.Superhero;
import deesee.comics.repository.Repository;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class ControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Repository repository;
    private Map<String, SuperheroResponse> superheroes;

    @BeforeEach
    void init() throws JsonProcessingException {
        superheroes = new ConcurrentHashMap<>();
        List<SuperheroResponse> initSuperheroes = objectMapper.readValue(Constants.ALL_SUPERHEROES_RESPONSE, new TypeReference<>() {
        });
        initSuperheroes.forEach(superhero -> superheroes.put(superhero.getName(), superhero));
    }

    @Test
    void findAllSuperheroes() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/superhero"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        List<SuperheroResponse> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(superheroes.size(), result.size());
        for (SuperheroResponse superheroResponse : result) {
            SuperheroResponse sample = superheroes.get(superheroResponse.getName());
            assertNotNull(sample);
            assertEquals(sample.getName(), superheroResponse.getName());
            assertEquals(sample.getBirthday(), superheroResponse.getBirthday());
            assertEquals(sample.getIdentity(), superheroResponse.getIdentity());
            if (CollectionUtils.isEmpty(sample.getSuperpowers())) {
                assertTrue(CollectionUtils.isEmpty(superheroResponse.getSuperpowers()));
            } else {
                assertEquals(sample.getSuperpowers().size(), superheroResponse.getSuperpowers().size());
                assertTrue(sample.getSuperpowers().containsAll(superheroResponse.getSuperpowers()));
            }
        }
    }

    @Test
    void findSuperheroesBySuperpower() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/superhero/by")
                                .param("superpowers", "flight"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        List<SuperheroResponse> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        List<SuperheroResponse> samples = List.of(superheroes.get("aquaman"), superheroes.get("superman"));

        assertEquals(samples.size(), result.size());
        for (SuperheroResponse superheroResponse : result) {
            SuperheroResponse sample = superheroes.get(superheroResponse.getName());
            assertNotNull(sample);
            if (!superheroResponse.getName().equals("aquaman") && !superheroResponse.getName().equals("superman")) {
                Assertions.fail(String.format("Found unexpected superhero '%s'", superheroResponse.getName()));
            }
            assertEquals(sample.getName(), superheroResponse.getName());
            assertEquals(sample.getBirthday(), superheroResponse.getBirthday());
            assertEquals(sample.getIdentity(), superheroResponse.getIdentity());
            assertEquals(sample.getSuperpowers().size(), superheroResponse.getSuperpowers().size());
            assertTrue(sample.getSuperpowers().containsAll(superheroResponse.getSuperpowers()));
        }
    }

    @Test
    void findSuperheroesBySuperpowers() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/superhero/by")
                                .param("superpowers", "flight", "healing"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        List<SuperheroResponse> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        List<SuperheroResponse> samples = List.of(superheroes.get("aquaman"));

        assertEquals(samples.size(), result.size());
        SuperheroResponse superheroResponse = result.get(0);
        SuperheroResponse sample = superheroes.get(superheroResponse.getName());
        assertNotNull(sample);
        assertEquals("aquaman", superheroResponse.getName());
        assertEquals(sample.getName(), superheroResponse.getName());
        assertEquals(sample.getBirthday(), superheroResponse.getBirthday());
        assertEquals(sample.getIdentity(), superheroResponse.getIdentity());
        assertEquals(sample.getSuperpowers().size(), superheroResponse.getSuperpowers().size());
        assertTrue(sample.getSuperpowers().containsAll(superheroResponse.getSuperpowers()));
    }

    @Test
    void findSuperheroesWithEmptySuperpowers() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/superhero/by"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        List<SuperheroResponse> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        List<SuperheroResponse> samples = List.of(superheroes.get("batman"));

        assertEquals(samples.size(), result.size());
        SuperheroResponse superheroResponse = result.get(0);
        SuperheroResponse sample = superheroes.get(superheroResponse.getName());
        assertNotNull(sample);
        assertEquals("batman", superheroResponse.getName());
        assertEquals(sample.getName(), superheroResponse.getName());
        assertEquals(sample.getBirthday(), superheroResponse.getBirthday());
        assertEquals(sample.getIdentity(), superheroResponse.getIdentity());
        assertTrue(CollectionUtils.isEmpty(superheroResponse.getSuperpowers()));
    }

    @Test
    void findSuperheroesWithUnknownSuperpower() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/superhero/by")
                                .param("superpowers", "unknown_superpower"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        List<SuperheroResponse> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        assertTrue(CollectionUtils.isEmpty(result));
    }

    @Test
    void findAllSuperheroesEncrypted() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/superhero/encrypted")
                                .param("shiftCharTo", "3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        List<SuperheroResponse> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(superheroes.size(), result.size());
        for (SuperheroResponse superheroResponse : result) {
            SuperheroResponse sample = superheroes.get(superheroResponse.getName());
            assertNotNull(sample);
            assertEquals(sample.getName(), superheroResponse.getName());
            assertEquals(sample.getBirthday(), superheroResponse.getBirthday());
            assertNotEquals(sample.getIdentity(), superheroResponse.getIdentity(), "Identity must be encrypted");
            if (CollectionUtils.isEmpty(sample.getSuperpowers())) {
                assertTrue(CollectionUtils.isEmpty(superheroResponse.getSuperpowers()));
            } else {
                assertEquals(sample.getSuperpowers().size(), superheroResponse.getSuperpowers().size());
                assertTrue(sample.getSuperpowers().containsAll(superheroResponse.getSuperpowers()));
            }
        }
    }

    @Test
    void findSuperheroesBySuperpowerEncrypted() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/superhero/encrypted/by")
                                .param("superpowers", "flight"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        List<SuperheroResponse> result = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {
        });

        List<SuperheroResponse> samples = List.of(superheroes.get("aquaman"), superheroes.get("superman"));

        assertEquals(samples.size(), result.size());
        for (SuperheroResponse superheroResponse : result) {
            SuperheroResponse sample = superheroes.get(superheroResponse.getName());
            assertNotNull(sample);
            if (!superheroResponse.getName().equals("aquaman") && !superheroResponse.getName().equals("superman")) {
                Assertions.fail(String.format("Found unexpected superhero '%s'", superheroResponse.getName()));
            }
            assertEquals(sample.getName(), superheroResponse.getName());
            assertEquals(sample.getBirthday(), superheroResponse.getBirthday());
            assertNotEquals(sample.getIdentity(), superheroResponse.getIdentity(), "Identity must be encrypted");
            assertEquals(sample.getSuperpowers().size(), superheroResponse.getSuperpowers().size());
            assertTrue(sample.getSuperpowers().containsAll(superheroResponse.getSuperpowers()));
        }
    }

    @Test
    void deleteSuperheroTest() throws Exception {
        Superhero createdSuperhero = createIronmanSuperhero();

        mockMvc.perform(MockMvcRequestBuilders.delete("/superhero/{superheroName}", createdSuperhero.getName()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Optional<Superhero> createdSuperheroOpt = repository.findAll().stream()
                .filter(hero -> hero.getName().equals(createdSuperhero.getName()))
                .findAny();
        createdSuperheroOpt.ifPresent(
                superhero -> Assertions.fail(String.format("Superhero '%s' deletion failed", superhero.getName()))
        );
    }

    @Test
    void createSuperheroTest() throws Exception {
        Superhero sample = constructIronmanSuperhero();
        Superhero createdSuperhero = createIronmanSuperhero();

        assertEquals(sample.getName(), createdSuperhero.getName());
        assertEquals(sample.getBirthday(), createdSuperhero.getBirthday());
        assertEquals(sample.getIdentity().getFirstName(), createdSuperhero.getIdentity().getFirstName());
        assertEquals(sample.getIdentity().getLastName(), createdSuperhero.getIdentity().getLastName());
        assertEquals(sample.getSuperpowers().size(), createdSuperhero.getSuperpowers().size());
        assertTrue(sample.getSuperpowers().containsAll(createdSuperhero.getSuperpowers()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/superhero/{superheroName}", sample.getName()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void createSuperheroWithUnknownSuperpowerTest() throws Exception {
        String ironman = "{\n" +
                "        \"name\": \"Iron Man\",\n" +
                "        \"superpowers\": [\n" +
                "            \"unknown_superpower\"\n" +
                "        ]\n" +
                "    }\n";

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/superhero")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(ironman))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createSuperheroWithEmptyName() throws Exception {
        String ironman = "{\n" +
                "        \"superpowers\": [\n" +
                "            \"flight\"\n" +
                "        ]\n" +
                "    }\n";

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/superhero")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(ironman))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateSuperheroTest() throws Exception {
        Superhero superhero = createIronmanSuperhero();
        String ironman = "{\n" +
                "        \"name\": \"Iron Man\",\n" +
                "        \"identity\": {\n" +
                "            \"firstName\": \"Anthony Edward firstName\",\n" +
                "            \"lastName\": \"Stark lastname\"\n" +
                "        },\n" +
                "        \"birthday\": \"1964-12-02\"\n" +
                "    }\n";
        Superhero sample = objectMapper.readValue(ironman, Superhero.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/superhero")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ironman))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Optional<Superhero> updatedSuperheroOpt = repository.findAll().stream()
                .filter(hero -> hero.getName().equals(superhero.getName()))
                .findAny();
        if (updatedSuperheroOpt.isEmpty()) {
            Assertions.fail(String.format("Couldn't find created superhero: %s", superhero.getName()));
        }
        Superhero updatedSuperhero = updatedSuperheroOpt.get();

        assertEquals(sample.getName(), updatedSuperhero.getName());
        assertEquals(sample.getBirthday(), updatedSuperhero.getBirthday());
        assertEquals(sample.getIdentity().getFirstName(), updatedSuperhero.getIdentity().getFirstName());
        assertEquals(sample.getIdentity().getLastName(), updatedSuperhero.getIdentity().getLastName());
        assertTrue(CollectionUtils.isEmpty(updatedSuperhero.getSuperpowers()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/superhero/{superheroName}", sample.getName()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private Superhero constructIronmanSuperhero() {
        Superhero superhero = new Superhero();
        superhero.setName("Iron Man");
        Superhero.Identity identity = new Superhero.Identity();
        identity.setFirstName("Anthony Edward");
        identity.setLastName("Stark");
        superhero.setIdentity(identity);
        superhero.setBirthday(LocalDate.of(1963, 3, 1));
        superhero.setSuperpowers(Set.of("flight"));

        return superhero;
    }

    private Superhero createIronmanSuperhero() throws Exception {
        String name = "Iron Man";
        String ironman = "{\n" +
                "        \"name\": \"Iron Man\",\n" +
                "        \"identity\": {\n" +
                "            \"firstName\": \"Anthony Edward\",\n" +
                "            \"lastName\": \"Stark\"\n" +
                "        },\n" +
                "        \"birthday\": \"1963-03-01\",\n" +
                "        \"superpowers\": [\n" +
                "            \"flight\"\n" +
                "        ]\n" +
                "    }\n";

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/superhero")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(ironman))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Optional<Superhero> createdSuperheroOpt = repository.findAll().stream()
                .filter(hero -> hero.getName().equals(name))
                .findAny();
        if (createdSuperheroOpt.isEmpty()) {
            Assertions.fail(String.format("Couldn't find created superhero: %s", name));
        }

        return createdSuperheroOpt.get();
    }


    @Getter
    @Setter
    private static class SuperheroResponse {
        private String name;
        private String identity;
        private LocalDate birthday;
        private Set<String> superpowers;
    }

    private static final class Constants {
        private static final String ALL_SUPERHEROES_RESPONSE = "[\n" +
                "    {\n" +
                "        \"name\": \"aquaman\",\n" +
                "        \"identity\": \"arthur curry\",\n" +
                "        \"birthday\": \"1986-01-29\",\n" +
                "        \"superpowers\": [\n" +
                "            \"flight\",\n" +
                "            \"healing\",\n" +
                "            \"strength\"\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"deadpool\",\n" +
                "        \"identity\": \"wade wilson\",\n" +
                "        \"birthday\": \"1973-11-22\",\n" +
                "        \"superpowers\": [\n" +
                "            \"healing\"\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"superman\",\n" +
                "        \"identity\": \"clark kent\",\n" +
                "        \"birthday\": \"1977-04-18\",\n" +
                "        \"superpowers\": [\n" +
                "            \"flight\",\n" +
                "            \"strength\",\n" +
                "            \"invulnerability\"\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"batman\",\n" +
                "        \"identity\": \"bruce wayne\",\n" +
                "        \"birthday\": \"1915-04-17\",\n" +
                "        \"superpowers\": []\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"flash\",\n" +
                "        \"identity\": \"barry allen\",\n" +
                "        \"birthday\": \"1992-09-30\",\n" +
                "        \"superpowers\": [\n" +
                "            \"healing\",\n" +
                "            \"speed\"\n" +
                "        ]\n" +
                "    }\n" +
                "]";
    }
}