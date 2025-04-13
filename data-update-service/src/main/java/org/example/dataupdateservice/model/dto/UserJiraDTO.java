package org.example.dataupdateservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserJiraDTO {
    private String emailAddress;
    private String displayName;
}
