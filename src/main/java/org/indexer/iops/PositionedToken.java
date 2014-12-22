package org.indexer.iops;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents String token with it's position in file.
 */
public class PositionedToken {

    private final String token;
    private final long position;

    public PositionedToken(String token, long position) {
        this.token = token;
        this.position = position;
    }

    public String getToken() {
        return token;
    }

    public long getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PositionedToken)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return new EqualsBuilder().
                append(token, ((PositionedToken) obj).token).
                append(position, ((PositionedToken) obj).position).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(token).
                append(position).
                toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("token", token).
                append("position", position).
                toString();
    }
}
