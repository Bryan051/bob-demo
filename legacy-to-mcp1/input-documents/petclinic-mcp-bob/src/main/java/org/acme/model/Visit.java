package org.acme.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Visit {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("petId")
    private Integer petId;

    @JsonProperty("date")
    private String date;

    @JsonProperty("description")
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Visit{id=" + id + ", petId=" + petId + ", date='" + date + "', description='" + description + "'}";
    }
}
