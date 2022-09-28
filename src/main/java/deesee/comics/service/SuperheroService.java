package deesee.comics.service;

import deesee.comics.dto.Superhero;
import deesee.comics.service.encryption.EncryptionService;
import deesee.comics.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.Validator;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class SuperheroService {
    private EncryptionService encryptionService;
    private Repository repository;
    @Autowired
    private Validator validator;

    public SuperheroService(Repository repository, EncryptionService encryptionService) {
        this.repository = repository;
        this.encryptionService = encryptionService;
    }

    public void save(Superhero superhero) {
        if(CollectionUtils.isEmpty(superhero.getSuperpowers())){
            throw new IllegalArgumentException("List of superpowers cannot be empty");
        }

//            throw new IllegalArgumentException(String.format("Unregistered superpower: %s", unregisteredSuperpower));
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
    public Collection<Superhero> findBySuperPower(String superpower) {
        return repository.findBySuperpower(superpower).stream()
                .filter(superhero -> superhero.getSuperpowers().contains(superpower))
                .collect(Collectors.toList());
    }

    @NonNull
    public Collection<Superhero> findBySuperpowerEncrypted(String superpower, Integer shiftCharTo) {
        return findBySuperPower(superpower).stream()
                .map(superhero -> getEncryptedSuperhero(superhero, shiftCharTo))
                .collect(Collectors.toList());
    }

    private Superhero getEncryptedSuperhero(Superhero superhero, Integer shiftCharTo){
        String encryptedFirstName = encryptionService.encrypt(superhero.getIdentity().getFirstName(), shiftCharTo);
        String encryptedLastName = encryptionService.encrypt(superhero.getIdentity().getLastName(), shiftCharTo);
        Superhero.Identity encryptedIdentity = new Superhero.Identity();
        encryptedIdentity.setFirstName(encryptedFirstName);
        encryptedIdentity.setLastName(encryptedLastName);

        Superhero updatedSuperHero = new Superhero();
        updatedSuperHero.setIdentity(encryptedIdentity);
        updatedSuperHero.setName(superhero.getName());
        updatedSuperHero.setBirthday(superhero.getBirthday());
        updatedSuperHero.setSuperpowers(new HashSet<>(superhero.getSuperpowers()));
        return updatedSuperHero;
    }
}