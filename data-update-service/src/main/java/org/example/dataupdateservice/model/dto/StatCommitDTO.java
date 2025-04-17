package org.example.dataupdateservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatCommitDTO {
    private Stats stats;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Stats{
       private Long total;
       private Long additions;
       private Long deletions;
    }
}
