package com.atipera.service;

import com.atipera.dto.BranchResponse;
import com.atipera.dto.GithubBranchResponse;
import com.atipera.dto.GithubRepoResponse;
import com.atipera.dto.RepositoryResponse;
import com.atipera.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GithubService {
    private final RestTemplate restTemplate;
    private static final String GITHUB_API_URL = "https://api.github.com";

    public GithubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RepositoryResponse> getUserRepositories(String username) {
        try {
            // Fetch user repositories
            String reposUrl = GITHUB_API_URL + "/users/" + username + "/repos";
            GithubRepoResponse[] repos = restTemplate.getForObject(reposUrl, GithubRepoResponse[].class);

            if (repos == null) {
                throw new UserNotFoundException("User not found");
            }

            // Filter non-fork repositories and map to response
            return Arrays.stream(repos)
                    .filter(repo -> !repo.isFork())
                    .map(repo -> {
                        // Fetch branches for each repository
                        String branchesUrl = GITHUB_API_URL + "/repos/" + username + "/" + repo.getName() + "/branches";
                        GithubBranchResponse[] branches = restTemplate.getForObject(branchesUrl, GithubBranchResponse[].class);
                        List<BranchResponse> branchResponses = branches != null
                                ? Arrays.stream(branches)
                                .map(branch -> new BranchResponse(branch.getName(), branch.getCommit().getSha()))
                                .collect(Collectors.toList())
                                : List.of();
                        return new RepositoryResponse(repo.getName(), repo.getOwner().getLogin(), branchResponses);
                    })
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException("User not found");
            }
            throw e;
        }
    }
}
