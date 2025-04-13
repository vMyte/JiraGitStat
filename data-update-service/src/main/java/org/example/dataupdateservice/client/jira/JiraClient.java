package org.example.dataupdateservice.client.jira;

import lombok.RequiredArgsConstructor;
import org.springframework.aot.hint.annotation.RegisterReflection;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class JiraClient {
    private final WebClient jiraWebClient;

}
