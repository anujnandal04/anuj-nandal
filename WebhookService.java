package com.bajaj.finserv.service;

import com.bajaj.finserv.model.SolutionRequest;
import com.bajaj.finserv.model.WebhookRequest;
import com.bajaj.finserv.model.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {
    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String SUBMIT_SOLUTION_URL = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
    
    @Autowired
    private RestTemplate restTemplate;
    
    public void processWebhookFlow() {
        try {
            // Step 1: Generate webhook
            WebhookResponse webhookResponse = generateWebhook();
            logger.info("Webhook generated: {}", webhookResponse);
            
            if (webhookResponse != null && webhookResponse.isSuccess()) {
                // Step 2: Determine and solve SQL problem
                String sqlSolution = solveSQL("REG12347"); // Replace with your actual regNo
                
                // Step 3: Submit solution
                submitSolution(webhookResponse.getWebhook(), webhookResponse.getAccessToken(), sqlSolution);
            } else {
                logger.error("Failed to generate webhook");
            }
        } catch (Exception e) {
            logger.error("Error in webhook flow: {}", e.getMessage(), e);
        }
    }
    
    private WebhookResponse generateWebhook() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        WebhookRequest request = new WebhookRequest(
                "Anuj Nandal", // Your name
                "REG12347",    // Your registration number
                "anujnandal04@gmail.com" // Your email
        );
        
        HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(
                GENERATE_WEBHOOK_URL,
                entity,
                WebhookResponse.class
        );
        
        return response.getBody();
    }
    
    private String solveSQL(String regNo) {
        // Extract last two digits from regNo
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastDigits = Integer.parseInt(lastTwoDigits);
        
        // Choose SQL problem based on odd/even
        if (lastDigits % 2 == 1) {
            // Odd - Question 1
            logger.info("Solving Question 1 (Odd regNo)");
            return solveQuestion1();
        } else {
            // Even - Question 2
            logger.info("Solving Question 2 (Even regNo)");
            return solveQuestion2();
        }
    }
    
    private String solveQuestion1() {
        // Solution for Question 1 (Odd regNo)
        return "SELECT d.department_name, COUNT(e.employee_id) AS employee_count " +
               "FROM departments d " +
               "LEFT JOIN employees e ON d.department_id = e.department_id " +
               "GROUP BY d.department_name " +
               "ORDER BY employee_count DESC, d.department_name ASC";
    }
    
    private String solveQuestion2() {
        // Solution for Question 2 (Even regNo)
        return "SELECT p.product_name, SUM(o.quantity * o.unit_price) AS total_revenue " +
               "FROM products p " +
               "JOIN order_details o ON p.product_id = o.product_id " +
               "GROUP BY p.product_name " +
               "ORDER BY total_revenue DESC " +
               "LIMIT 5";
    }
    
    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);
        
        SolutionRequest solutionRequest = new SolutionRequest(sqlQuery);
        HttpEntity<SolutionRequest> entity = new HttpEntity<>(solutionRequest, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(
                SUBMIT_SOLUTION_URL,
                entity,
                String.class
        );
        
        logger.info("Solution submitted, response: {}", response.getBody());
    }
}