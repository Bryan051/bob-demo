package org.acme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet {

    private int id;
    private String name;
    private LocalDate birthDate;
    private PetType type;
    private int ownerId;
    private List<Object> visits;

    public Pet() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("birthDate")
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    @JsonProperty("ownerId")
    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public List<Object> getVisits() {
        return visits;
    }

    public void setVisits(List<Object> visits) {
        this.visits = visits;
    }

    @Override
    public String toString() {
        return "Pet{id=" + id + ", name='" + name + "', birthDate=" + birthDate
                + ", type=" + type + ", ownerId=" + ownerId + "}";
    }
}
