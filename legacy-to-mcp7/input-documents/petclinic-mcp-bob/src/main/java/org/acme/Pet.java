package org.acme;

import java.util.List;

public class Pet {
    public Integer id;
    public String name;
    public String birthDate;
    public PetType type;
    public Integer ownerId;
    public List<Visit> visits;

    public static class PetType {
        public Integer id;
        public String name;
    }

    public static class Visit {
        public Integer id;
        public String date;
        public String description;
    }
}
