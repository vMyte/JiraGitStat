package org.example.dataupdateservice.model.mapper.common;

import org.example.dataupdateservice.model.dto.UserJiraDTO;
import org.example.dataupdateservice.model.entity.UserMapping;
import org.springframework.stereotype.Component;

@Component
public class UserMappingMapper {

    public UserMapping toEntity(UserJiraDTO userJiraDTO, String gitHubUsername){
        UserMapping userMapping = new UserMapping();
        userMapping.setEmail(userJiraDTO.getEmailAddress().toLowerCase());
        userMapping.setGithubUsername(gitHubUsername);
        userMapping.setJiraUsername(userJiraDTO.getDisplayName());
        return userMapping;
    }

}
