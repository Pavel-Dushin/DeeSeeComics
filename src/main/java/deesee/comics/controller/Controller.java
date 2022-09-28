package deesee.comics.controller;

import deesee.comics.dto.Superhero;
import deesee.comics.service.SuperheroService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/superhero")
@AllArgsConstructor
public class Controller {
    private SuperheroService superheroService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> save(@Valid @RequestBody Superhero superhero) {
        superheroService.save(superhero);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @ResponseBody
    public Collection<Superhero> findAll() {
        return superheroService.findAll();
    }

    @GetMapping("/encrypted")
    @ResponseBody
    public Collection<Superhero> findAllEncrypted(@RequestParam(required = false) Integer shiftCharTo) {
        return superheroService.findAllEncrypted(shiftCharTo);
    }

    @GetMapping("/by")
    @ResponseBody
    public Collection<Superhero> findBySuperPower(@RequestParam String superpower) {
        return superheroService.findBySuperPower(superpower);
    }

    @GetMapping("/encrypted/by")
    @ResponseBody
    public Collection<Superhero> findByEncrypted(
            @RequestParam(required = false) Integer shiftCharTo,
            @RequestParam String superpower
    ) {
        return superheroService.findBySuperpowerEncrypted(superpower, shiftCharTo);
    }
}
