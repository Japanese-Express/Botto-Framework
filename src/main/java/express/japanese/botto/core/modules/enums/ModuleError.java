package express.japanese.botto.core.modules.enums;

public enum ModuleError {
    UNKNOWN("Unknown"),
    CONSTRUCTOR("Constructor had an error"),
    INVALID_LANGUAGE("The language chosen was invalid for this context"),
    INVALID_VARIABLE("Some variable was invalid and caused an error"),
    INVALID_CHANNEL("Invalid channel"),
    MESSAGE_SEND("Failed to send messages"),
    INVALID_MESSAGE_ID("Failed to gather messageId"),
    FAILED_TO_READY("Module failed onReady method"),
    FAILED_TO_INSTANCE("Failed to create instance of module"),
    NO_ANNOTATION("Module is missing an annotation");

    private final String errorMsg;
    ModuleError(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
