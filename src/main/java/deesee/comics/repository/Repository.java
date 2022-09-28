package deesee.comics.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import deesee.comics.dto.Superhero;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class Repository {
    private Map<String, Superhero> superheroes;

    public Repository() throws JsonProcessingException {
        superheroes = new ConcurrentHashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Superhero> initSuperheroes = objectMapper.readValue(Constants.INIT_DATA, new TypeReference<>() {
        });
        initSuperheroes.forEach(superhero -> superheroes.put(superhero.getName(), superhero));
    }

    public void save(Superhero superhero) {
        superheroes.put(superhero.getName(), superhero);
    }

    @NonNull
    public Collection<Superhero> findAll() {
        return superheroes.values();
    }

    @NonNull
    public Collection<Superhero> findBySuperpower(Set<String> superpowers) {
        return superheroes.values().stream()
                .filter(superhero ->
                        CollectionUtils.isEmpty(superpowers) && CollectionUtils.isEmpty(superhero.getSuperpowers())
                                || !CollectionUtils.isEmpty(superpowers) && superhero.getSuperpowers().containsAll(superpowers)
                )
                .collect(Collectors.toList());
    }

    private static final class Constants {
        private static final String INIT_DATA = "[\n" +
                "    {\n" +
                "        \"name\": \"superman\",\n" +
                "        \"identity\": {\n" +
                "            \"firstName\": \"clark\",\n" +
                "            \"lastName\": \"kent\"\n" +
                "        },\n" +
                "        \"birthday\": \"1977-04-18\",\n" +
                "        \"superpowers\": [\n" +
                "            \"flight\",\n" +
                "            \"strength\",\n" +
                "            \"invulnerability\"\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"deadpool\",\n" +
                "        \"identity\": {\n" +
                "            \"firstName\": \"wade\",\n" +
                "            \"lastName\": \"wilson\"\n" +
                "        },\n" +
                "        \"birthday\": \"1973-11-22\",\n" +
                "        \"superpowers\": [\n" +
                "            \"healing\"\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"batman\",\n" +
                "        \"identity\": {\n" +
                "            \"firstName\": \"bruce\",\n" +
                "            \"lastName\": \"wayne\"\n" +
                "        },\n" +
                "        \"birthday\": \"1915-04-17\",\n" +
                "        \"superpowers\": []\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"aquaman\",\n" +
                "        \"identity\": {\n" +
                "            \"firstName\": \"arthur\",\n" +
                "            \"lastName\": \"curry\"\n" +
                "        },\n" +
                "        \"birthday\": \"1986-01-29\",\n" +
                "        \"superpowers\": [\n" +
                "            \"flight\",\n" +
                "            \"healing\",\n" +
                "            \"strength\"\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\": \"flash\",\n" +
                "        \"identity\": {\n" +
                "            \"firstName\": \"barry\",\n" +
                "            \"lastName\": \"allen\"\n" +
                "        },\n" +
                "        \"birthday\": \"1992-09-30\",\n" +
                "        \"superpowers\": [\n" +
                "            \"speed\",\n" +
                "            \"healing\"\n" +
                "        ]\n" +
                "    }\n" +
                "]";
    }
}
