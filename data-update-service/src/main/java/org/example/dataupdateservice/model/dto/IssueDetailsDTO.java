package org.example.dataupdateservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueDetailsDTO {
    private Integer total;
    private List<Issue> issues;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Issue {
        private Long id;
    }
}
