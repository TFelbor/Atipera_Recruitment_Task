package com.atipera.dto;

public record ErrorResponse(int status, String message) {
}
```

```java
// src/main/java/com/atipera/exception/UserNotFoundException.java
package com.atipera.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
