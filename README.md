# GitHub Repositories API

This is a Spring Boot application developed for the Atipera recruitment task. It provides an API to list GitHub repositories for a given user, excluding forks, and includes branch details as per the acceptance criteria.

## Requirements
- Java 21
- Maven 3.8+
- Internet access to connect to the GitHub API (https://api.github.com)

## Setup
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd github-repos-api
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   The application will start on `http://localhost:8080`.

## API Usage
- **Endpoint**: `GET /api/users/{username}/repos`
- **Description**: Retrieves a list of non-fork repositories for the specified GitHub user, including repository name, owner login, and branch details (name and last commit SHA).
- **Example Request**:
  ```bash
  curl http://localhost:8080/api/users/octocat/repos
  ```
- **Example Response**:
  ```json
  [
    {
      "name": "Hello-World",
      "ownerLogin": "octocat",
      "branches": [
        {
          "name": "main",
          "lastCommitSha": "7fd1a60b01f91b314f59955a4e4d4e80d8edf11d"
        }
      ]
    }
  ]
  ```
- **Error Response (non-existing user)**:
  ```bash
  curl http://localhost:8080/api/users/nonexistentuser/repos
  ```
  ```json
  {
    "status": 404,
    "message": "User not found"
  }
  ```

## Running Tests
1. Run the integration test:
   ```bash
   mvn test
   ```
   The test verifies the happy path by querying repositories for the user "TFelbor" and checking the response structure.

## Project Structure
- `GithubController`: Handles the REST endpoint and exception handling.
- `GithubService`: Interacts with the GitHub API to fetch repositories and branches.
- `RepositoryResponse`, `BranchResponse`: DTOs for the API response.
- `GithubRepoResponse`, `GithubBranchResponse`: DTOs for GitHub API responses.
- `ErrorResponse`: DTO for error responses.
- `UserNotFoundException`: Custom exception for 404 errors.
- `GithubControllerIntegrationTest`: Integration test for the happy path.

## Notes
- The application uses `RestTemplate` for GitHub API calls.
- No mocks are used in the integration test to ensure real API interaction.
- The project adheres to the acceptance criteria without additional features like pagination or DDD.
