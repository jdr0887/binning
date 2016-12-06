package org.renci.binning;

public class BinningException extends Exception {

    private static final long serialVersionUID = -1043050876795590454L;

    public BinningException() {
        super();
    }

    public BinningException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BinningException(String message, Throwable cause) {
        super(message, cause);
    }

    public BinningException(String message) {
        super(message);
    }

    public BinningException(Throwable cause) {
        super(cause);
    }

}
