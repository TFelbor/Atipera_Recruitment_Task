package com.atipera.controller;

import com.atipera.dto.BranchResponse;
import com.atipera.dto.RepositoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GithubControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetUserRepositories_HappyPath() {
        // Given: My GitHub username
        String username = "TFelbor"; 

        // When: Call the API endpoint
        ResponseEntity<RepositoryResponse[]> response = restTemplate.getForEntity("/api/users/{username}/repos", RepositoryResponse[].class, username);

        // Then: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RepositoryResponse[] repos = response.getBody();
        assertNotNull(repos);
        assertTrue(repos.length > 0, "Expected at least one repository");

        // Verify that repositories are not forks and contain required fields
        for (RepositoryResponse repo : repos) {
            assertNotNull(repo.name(), "Repository name should not be null");
            assertEquals(username, repo.ownerLogin(), "Owner login should match the requested username");
            List<BranchResponse> branches = repo.branches();
            assertNotNull(branches, "Branches should not be null");
            for (BranchResponse branch : branches) {
                assertNotNull(branch.name(), "Branch name should not be null");
                assertNotNull(branch.lastCommitSha(), "Last commit SHA should not be null");
            }
        }
    }
}
