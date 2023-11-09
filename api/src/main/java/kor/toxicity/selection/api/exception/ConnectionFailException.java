package kor.toxicity.selection.api.exception;

public class ConnectionFailException extends SelectionAPIException {
    public ConnectionFailException(Exception e) {
        super(e);
    }
}
