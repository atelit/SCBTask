package org.example.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "timestamp",
        "status",
        "error",
        "path"
})
@Data
@Builder
@Getter
@Setter
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    @JsonProperty("timestamp")
    public String timestamp;
    @JsonProperty("status")
    public Integer status;
    @JsonProperty("error")
    public String error;
    @JsonProperty("path")
    public String path;
}
