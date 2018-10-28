package no.cantara.docsite.domain.external;

import no.cantara.docsite.util.JsonbFactory;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

abstract public class ExternalURL<T> implements Serializable {

    protected final @JsonbTransient T internal;

    public ExternalURL(T internal) {
        this.internal = internal;
    }

    public static ExternalURL<String> VOID() {
        return new ExternalURL<>("") {
            @Override
            public String getKey() {
                return "void";
            }

            @Override
            public String getExternalURL() {
                return "";
            }
        };
    }

    abstract public @JsonbProperty("key") String getKey();

    abstract public @JsonbProperty("value") String getExternalURL();

    public @JsonbTransient boolean isGenericOf(Class<?> clazz) {
        return Arrays.asList(internal.getClass().getTypeName()).stream().anyMatch(type -> type.equals(clazz.getTypeName()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExternalURL)) return false;
        ExternalURL<?> that = (ExternalURL<?>) o;
        return Objects.equals(internal, that.internal) &&
                Objects.equals(getKey(), that.getKey()) &&
                Objects.equals(getExternalURL(), that.getExternalURL());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getExternalURL());
    }

    @Override
    public String toString() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(getKey(), getExternalURL());
        return JsonbFactory.asString(builder.build());
    }
}