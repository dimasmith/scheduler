package scheduler.app.exceptions;

public class EntityNotFoundException extends BackendException {

    public EntityNotFoundException(final String message) {
        super(message);
    }
}
