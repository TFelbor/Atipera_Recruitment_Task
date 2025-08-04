package com.atipera.controller;

import com.atipera.dto.ErrorResponse;
import com.atipera.dto.RepositoryResponse;
import com.atipera.exception.UserNotFoundException;
import com.atipera.service.GithubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class GithubController {
    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{username}/repos")
    public List<RepositoryResponse> getUserRepositories(@PathVariable String username) {
        return githubService.getUserRepositories(username);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
