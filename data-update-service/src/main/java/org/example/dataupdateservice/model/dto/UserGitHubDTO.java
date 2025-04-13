package org.example.dataupdateservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGitHubDTO {
    private CommitDetails commit;

    @Data
    public static class CommitDetails {
        private Committer committer;
    }

    @Data
    public static class Committer {
        private String name;
        private String email;
    }
}
