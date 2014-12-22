package org.indexer.core.index;

/**
 * PostingList loading exceptions wrapper.
 */
public class PostingListLoadException extends RuntimeException {

    public PostingListLoadException(Throwable cause) {
        super(cause);
    }

    public PostingListLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
