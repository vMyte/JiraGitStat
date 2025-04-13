package org.example.dataupdateservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueDTO {
        private String key;
        private Fields fields;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Fields {
            private String summary;
            private Status status;
            private Assignee assignee;

            @JsonProperty("created")
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            private OffsetDateTime createdAt;

            @JsonProperty("updated")
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            private OffsetDateTime updatedAt;

            @JsonProperty("resolutiondate")
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            private OffsetDateTime closedAt;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Status {
                private String name;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Assignee {
                @JsonProperty("displayName")
                private String name;
            }
        }

    }

