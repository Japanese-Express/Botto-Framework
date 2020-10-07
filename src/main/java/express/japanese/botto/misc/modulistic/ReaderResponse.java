package express.japanese.botto.misc.modulistic;

/**
 * Response for JsonFile reading, weather or not
 * it has had success or not.
 * @param <T> Response
 */
public class ReaderResponse<T> {
    private final String message;
    private final int responseCode;
    private T response;
    ReaderResponse(String message, int responseCode) {
        this.message = message;
        this.responseCode = responseCode;
    }
    ReaderResponse(T response, String message, int responseCode) {
        this(message, responseCode);
        this.response = response;
    }

    public T getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public boolean responseCodeIsError() {
        return responseCode != 200 && responseCode != 1;
    }
}
