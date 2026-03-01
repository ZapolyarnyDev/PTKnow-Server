package ptknow.exception.token;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super(String.format("Токен %s не валиден", token));
    }
}

