package deesee.comics.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import deesee.comics.validation.SuperpowerConstraint;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Schema(title = "Superhero", description = "Information about superhero")
public class Superhero {
    @NotBlank(message = "cannot be empty")
    @Schema(description = "pseudonym", required = true, minLength = 1, maxLength = 255, example = "deadpool")
    private String name;

    @JsonSerialize(using = IdentitySerializer.class)
    private Identity identity;

    @Schema(pattern = "yyyy-MM-dd", example = "1973-11-22")
    private LocalDate birthday;

    @SuperpowerConstraint
    @ArraySchema(maxItems = 5, schema = @Schema(description = "set of superpowers",
            required = true, minLength = 1, maxLength = 15, type = "string", example = "healing"))
    private Set<String> superpowers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Superhero superhero = (Superhero) o;
        return Objects.equals(name, superhero.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Getter
    @Setter
    @ToString
    @Schema(title = "Superhero identity", description = "Superhero identity information")
    public static class Identity {
        @Schema(minLength = 1, maxLength = 255, example = "wade")
        private String firstName;
        @Schema(minLength = 1, maxLength = 255, example = "wilson")
        private String lastName;
    }

    public static class IdentitySerializer extends StdSerializer<Identity> {
        public IdentitySerializer() {
            this(null);
        }

        public IdentitySerializer(Class<Superhero.Identity> t) {
            super(t);
        }

        @Override
        public void serialize(Superhero.Identity value, JsonGenerator gen, SerializerProvider arg2) throws IOException {
            gen.writeString(String.format("%s %s", value.getFirstName(), value.getLastName()));
        }
    }
}
