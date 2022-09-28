package deesee.comics.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import deesee.comics.validation.SuperpowerConstraint;
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
public class Superhero {
    @NotBlank(message = "cannot be empty")
    private String name;
    @JsonSerialize(using = IdentitySerializer.class)
    private Identity identity;
    private LocalDate birthday;
    @SuperpowerConstraint
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
    public static class Identity {
        private String firstName;
        private String LastName;
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
