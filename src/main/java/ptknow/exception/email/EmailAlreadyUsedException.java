package ptknow.exception.email;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super(String.format("Почта %s уже используется", email));
    }
}

