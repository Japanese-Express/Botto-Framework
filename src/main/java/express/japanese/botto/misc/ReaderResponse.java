package express.japanese.botto.misc;

public class ReaderResponse<T> {
    private final String message;
    private final int responseCode;
    private T returned;
    ReaderResponse(String message, int responseCode) {
        this.message = message;
        this.responseCode = responseCode;
    }
    ReaderResponse(T returned, String message, int responseCode) {
        this(message, responseCode);
        this.returned = returned;
    }

    public T getReturned() {
        return returned;
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
