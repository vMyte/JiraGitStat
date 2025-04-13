package org.example.dataupdateservice.model.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitDTO {
    private String sha;
    private CommitDetails commit;

    @Data
    public static class CommitDetails {
        private String message;
        private Committer committer;
    }

    @Data
    public static class Committer {
        private String name;
        private String email;
        private LocalDateTime date;
    }

}