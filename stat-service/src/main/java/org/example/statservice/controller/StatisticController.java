package org.example.statservice.controller;
import lombok.RequiredArgsConstructor;
import org.example.statservice.repository.StatisticRepo;
import org.example.statservice.repository.UserMappingRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import org.example.statservice.repository.IssueRepo;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stat")
//@CrossOrigin(origins = "http://127.0.0.1:5500")
public class StatisticController {
      private final StatisticRepo statisticRepo;
     private final UserMappingRepo userMappingRepo;
     private final IssueRepo issueRepo;
    
     // Общий метод для поиска user_mapping_id по email
     private Long getUserIdByEmail(String email) {
          return userMappingRepo.findByEmail(email)
                  .orElseThrow(() -> new ResponseStatusException(
                          HttpStatus.NOT_FOUND, "User not found with email: " + email))
                  .getId();
     }

     // Метод для получения метрики по userId
     private Long getMetricValue(Long userId, String metricName) {
          return statisticRepo.findByUserMappingIdAndMetricName(userId, metricName)
                  .map(stat -> stat.getMetricValue().longValue())
                  .orElse(0L);
     }

     // Метод для получения суммарной метрики
     private Long getTotalMetricValue(String metricName) {
          return statisticRepo.sumMetricValueByMetricName(metricName)
                  .orElse(0L);
     }

     // commits
     @GetMapping("/commits")
     public Long countCommits() {
         //System.out.println("hohoho");
          return getTotalMetricValue("COUNT_COMMITS");
     }

     @GetMapping("/commits/{email}")
     public Long countCommitForEmail(@PathVariable String email) {
          return getMetricValue(getUserIdByEmail(email), "COUNT_COMMITS");
     }

     // addedLines
     @GetMapping("/addedLines")
     public Long addedLines() {
          return getTotalMetricValue("LINES_ADDED");
     }

     @GetMapping("/addedLines/{email}")
     public Long addedLinesForEmail(@PathVariable String email) {
          return getMetricValue(getUserIdByEmail(email), "LINES_ADDED");
     }

     // deletedLines
     @GetMapping("/deletedLines")
     public Long deletedLines() {
          return getTotalMetricValue("LINES_DELETED");
     }

     @GetMapping("/deletedLines/{email}")
     public Long deletedLinesForEmail(@PathVariable String email) {
          return getMetricValue(getUserIdByEmail(email), "LINES_DELETED");
     }

     // totalLines
     @GetMapping("/totalAvgLines")
     public Long totalAvgLines() {
          return getTotalMetricValue("LINES_AVG_TOTAL");
     }

     @GetMapping("/totalAvgLines/{email}")
     public Long totalAvgLinesForEmail(@PathVariable String email) {
          return getMetricValue(getUserIdByEmail(email), "LINES_AVG_TOTAL");
     }

     // doneIssues
     @GetMapping("/doneIssues")
     public Long doneIssues() {
          return getTotalMetricValue("DONE_ISSUES");
     }

     @GetMapping("/doneIssues/{email}")
     public Long doneIssuesForEmail(@PathVariable String email) {
          return getMetricValue(getUserIdByEmail(email), "DONE_ISSUES");
     }

     // avgTimeIssues
     @GetMapping("/avgTimeIssues/{email}")
     public BigDecimal avgTimeIssuesForEmail(@PathVariable String email) {
          return statisticRepo.findByUserMappingIdAndMetricName(
                          getUserIdByEmail(email), "AVG_ISSUE_TIME").orElseThrow().getMetricValue();

     }
     
     @GetMapping("/users")
     public List<String> getAllUsers(){
          return userMappingRepo.findAllEmails();
     }
     
     @GetMapping("/doneIssueOnWeek")
     public List<Object[]> getDoneIssueOnWeek(){
          return issueRepo.getDoneIssuesOnWeekRaw();
     }
}
