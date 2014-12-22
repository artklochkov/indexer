package org.indexer.core.index;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Represents occurence of the word in collection of files.
 */
public class PostingNode implements Comparable<PostingNode>, Serializable {

    /**
     * Id of the document, where the word was found.
     */
    private final int documentId;

    /**
     * Position of the word in document (binary position)
     */
    private final long documentPosition;

    public PostingNode(int documentId, long documentPosition) {
        this.documentId = documentId;
        this.documentPosition = documentPosition;
    }


    public int getDocumentId() {
        return documentId;
    }

    public long getDocumentPosition() {
        return documentPosition;
    }

    @Override
    public int compareTo(PostingNode postingNode) {
        if (this.documentId != postingNode.documentId) {
            return Integer.signum(this.documentId - postingNode.documentId);
        } else {
            return Long.signum(this.documentPosition - postingNode.documentPosition);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PostingNode)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return new EqualsBuilder().
                append(documentId, ((PostingNode) obj).documentId).
                append(documentPosition, ((PostingNode) obj).documentPosition).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(documentId).
                append(documentPosition).
                toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("documentId", documentId).
                append("documentPosition", documentPosition).
                toString();
    }
}
