package ptknow.exception.email;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super(String.format("Email %s is already in use", email));
    }
}
