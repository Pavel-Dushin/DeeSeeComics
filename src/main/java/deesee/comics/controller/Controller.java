package deesee.comics.controller;

import deesee.comics.dto.Superhero;
import deesee.comics.service.SuperheroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.util.Set;

@RestController
@RequestMapping("/superhero")
@AllArgsConstructor
@Tag(name = "Superheroes service")
public class Controller {
    private SuperheroService superheroService;

    @PostMapping
    @ResponseBody
    @Operation(summary = "Save superhero (create/update by name)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    description = "Request error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class))}
            ),
            @ApiResponse(responseCode = "204", description = "Saved successfully")
    })
    public ResponseEntity<Void> save(@Valid @RequestBody Superhero superhero) {
        superheroService.save(superhero);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "Search for superheroes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    description = "Request error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class))}
            ),
            @ApiResponse(responseCode = "200",
                    description = "Collection of superheroes",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Superhero.class)))}
            )
    })
    public Collection<Superhero> findAll() {
        return superheroService.findAll();
    }

    @GetMapping("/encrypted")
    @ResponseBody
    @Operation(summary = "Search for superheroes with encrypted identity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    description = "Request error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class))}
            ),
            @ApiResponse(responseCode = "200",
                    description = "Collection of superheroes",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Superhero.class)))}
            )
    })
    public Collection<Superhero> findAllEncrypted(@RequestParam(required = false) Integer shiftCharTo) {
        return superheroService.findAllEncrypted(shiftCharTo);
    }

    @GetMapping("/by")
    @ResponseBody
    @Operation(summary = "Search for superheroes by superpower(s)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    description = "Request error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class))}
            ),
            @ApiResponse(responseCode = "200",
                    description = "Collection of superheroes",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Superhero.class)))}
            )
    })
    public Collection<Superhero> findBySuperPower(@RequestParam(required = false) Set<String> superpowers) {
        return superheroService.findBySuperPower(superpowers);
    }

    @GetMapping("/encrypted/by")
    @ResponseBody
    @Operation(summary = "Search for superheroes by superpower(s) with encrypted identity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    description = "Request error",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorDto.class))}
            ),
            @ApiResponse(responseCode = "200",
                    description = "Collection of superheroes",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Superhero.class)))}
            )
    })
    public Collection<Superhero> findByEncrypted(
            @RequestParam(required = false) Integer shiftCharTo,
            @RequestParam(required = false) Set<String> superpowers
    ) {
        return superheroService.findBySuperpowerEncrypted(superpowers, shiftCharTo);
    }
}
