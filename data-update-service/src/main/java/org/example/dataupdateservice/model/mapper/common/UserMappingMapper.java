package org.example.dataupdateservice.model.mapper.common;

import lombok.RequiredArgsConstructor;
import org.example.dataupdateservice.config.GitHubConfig;
import org.example.dataupdateservice.model.dto.UserJiraDTO;
import org.example.dataupdateservice.model.entity.UserMapping;
import org.example.dataupdateservice.repository.RepositoryRepo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMappingMapper {

    public UserMapping toEntity(UserJiraDTO userJiraDTO, String gitHubUsername,Long id){
        UserMapping userMapping = new UserMapping();
        userMapping.setRepoId(id);
        userMapping.setEmail(userJiraDTO.getEmailAddress().toLowerCase());
        userMapping.setGithubUsername(gitHubUsername);
        userMapping.setJiraUsername(userJiraDTO.getDisplayName());
        return userMapping;
    }

}
