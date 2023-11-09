package kor.toxicity.selection.api.exception;

public abstract class SelectionAPIException extends RuntimeException {
    public SelectionAPIException(Throwable throwable) {
        super(throwable);
    }
    public SelectionAPIException(String message) {
        super(message);
    }
    public SelectionAPIException() {
        super();
    }
}
