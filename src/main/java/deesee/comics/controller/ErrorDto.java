package deesee.comics.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(title = "Error", description = "Error information occurred during the request")
public class ErrorDto {
    @Schema(description = "Error details", required = true, minLength = 1, maxLength = 255,
            example = "name cannot be empty")
    private String error;
}
