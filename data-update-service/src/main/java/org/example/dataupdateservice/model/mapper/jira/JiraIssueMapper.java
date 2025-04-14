package org.example.dataupdateservice.model.mapper.jira;

import org.example.dataupdateservice.model.dto.IssueDTO;
import org.example.dataupdateservice.model.dto.IssueDetailsDTO;
import org.example.dataupdateservice.model.entity.IssueEntity;
import org.example.dataupdateservice.service.IssueLoader;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JiraIssueMapper {
    public IssueEntity toEntity(IssueDTO issueDTO){
        IssueEntity issue = new IssueEntity();
        issue.setKey(issueDTO.getKey());
        issue.setSummary(issueDTO.getFields().getSummary());
        issue.setStatus(issueDTO.getFields().getStatus().getName());
        issue.setAssignee(issueDTO.getFields().getAssignee().getName());
        issue.setCreatedAt(issueDTO.getFields().getCreatedAt().toString());
        issue.setUpdatedAt(
                Optional.ofNullable(issueDTO.getFields().getClosedAt())
                        .map(Object::toString)
                        .orElse(null)
        );
        return issue;
    }
}

