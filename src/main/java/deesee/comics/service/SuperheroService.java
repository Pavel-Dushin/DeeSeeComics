package deesee.comics.service;

import deesee.comics.dto.Superhero;
import deesee.comics.service.encryption.EncryptionService;
import deesee.comics.repository.Repository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.Validator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SuperheroService {
    private final EncryptionService encryptionService;
    private final Repository repository;

    public void save(Superhero superhero) {
        repository.save(superhero);
    }

    @NonNull
    public Collection<Superhero> findAll() {
        return repository.findAll();
    }

    @NonNull
    public Collection<Superhero> findAllEncrypted(Integer shiftCharTo) {
        return findAll().stream()
                .map(superhero -> getEncryptedSuperhero(superhero, shiftCharTo))
                .collect(Collectors.toList());
    }

    @NonNull
    public Collection<Superhero> findBySuperPower(Set<String> superpower) {
        return repository.findBySuperpower(superpower);
    }

    @NonNull
    public Collection<Superhero> findBySuperpowerEncrypted(Set<String> superpowers, Integer shiftCharTo) {
        return findBySuperPower(superpowers).stream()
                .map(superhero -> getEncryptedSuperhero(superhero, shiftCharTo))
                .collect(Collectors.toList());
    }

    private Superhero getEncryptedSuperhero(Superhero superhero, Integer shiftCharTo){
        Superhero.Identity encryptedIdentity = null;
        if(superhero.getIdentity() != null){
            encryptedIdentity = new Superhero.Identity();
            String encryptedFirstName = encryptionService.encrypt(superhero.getIdentity().getFirstName(), shiftCharTo);
            String encryptedLastName = encryptionService.encrypt(superhero.getIdentity().getLastName(), shiftCharTo);
            encryptedIdentity.setFirstName(encryptedFirstName);
            encryptedIdentity.setLastName(encryptedLastName);
        }

        Superhero updatedSuperHero = new Superhero();
        updatedSuperHero.setIdentity(encryptedIdentity);
        updatedSuperHero.setName(superhero.getName());
        updatedSuperHero.setBirthday(superhero.getBirthday());
        updatedSuperHero.setSuperpowers(new HashSet<>(superhero.getSuperpowers()));
        return updatedSuperHero;
    }
}
