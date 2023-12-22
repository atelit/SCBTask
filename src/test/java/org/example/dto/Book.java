package org.example.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "author",
        "publication",
        "category",
        "pages",
        "price"
})
@Data
@Builder
@Getter
@Setter
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class Book {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("author")
    public String author;
    @JsonProperty("publication")
    public String publication;
    @JsonProperty("category")
    public String category;
    @JsonProperty("pages")
    public Integer pages;
    @JsonProperty("price")
    public Double price;

}
