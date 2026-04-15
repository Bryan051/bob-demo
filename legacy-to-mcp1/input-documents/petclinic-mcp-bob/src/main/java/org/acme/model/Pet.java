package org.acme.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Pet {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("birthDate")
    private String birthDate;

    @JsonProperty("type")
    private PetType type;

    @JsonProperty("ownerId")
    private Integer ownerId;

    @JsonProperty("visits")
    private List<Visit> visits;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    @Override
    public String toString() {
        return "Pet{id=" + id +
                ", name='" + name + "'" +
                ", birthDate='" + birthDate + "'" +
                ", type=" + type +
                ", ownerId=" + ownerId +
                ", visits=" + visits +
                "}";
    }
}
