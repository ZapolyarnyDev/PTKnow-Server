package ptknow.exception.file;

import ptknow.model.file.attachment.resource.ResourceType;

public class InvalidResourceIdException extends RuntimeException {
    public InvalidResourceIdException(ResourceType resourceType, String resourceId, String expectedType) {
        super("Invalid resource id '" + resourceId + "' for " + resourceType + ". Expected " + expectedType);
    }
}
