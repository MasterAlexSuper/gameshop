package model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Game {
    private int id;
    private String name;
    private BigDecimal rating;
    private BigDecimal cost;
    private String description;
    private String type;
    private java.sql.Date releaseDate;
    private java.sql.Date creationDate;


    public String toString() {
        return "Title=" + this.getName() + ", rating=" + this.getRating() + ", cost=" + this.getCost() + ", description=" + this.getDescription() + ", type=" + this.getType() + ", releaseDate=" + this.getReleaseDate() + ", creationDate=" + this.getCreationDate();
    }
}