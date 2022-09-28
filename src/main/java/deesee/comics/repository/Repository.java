package deesee.comics.repository;

import deesee.comics.dto.Superhero;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class Repository {
    private Map<String, Superhero> superheroes = new ConcurrentHashMap<>();

    public void save(Superhero superhero) {
        superheroes.put(superhero.getName(), superhero);
    }

    @NonNull
    public Collection<Superhero> findAll() {
        return superheroes.values();
    }

    @NonNull
    public Collection<Superhero> findBySuperpower(String superpower) {
        return superheroes.values().stream()
                .filter(superhero -> superhero.getSuperpowers().contains(superpower))
                .collect(Collectors.toList());
    }
}
