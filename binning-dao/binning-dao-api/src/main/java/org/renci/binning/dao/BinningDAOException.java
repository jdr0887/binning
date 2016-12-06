package org.renci.binning.dao;

public class BinningDAOException extends Exception {

    private static final long serialVersionUID = 5033504425787801829L;

    public BinningDAOException() {
        super();
    }

    public BinningDAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BinningDAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public BinningDAOException(String message) {
        super(message);
    }

    public BinningDAOException(Throwable cause) {
        super(cause);
    }

}
