package ptknow.exception.token;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String token) {
        super(String.format("Токен %s не найден", token));
    }
}

