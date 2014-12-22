package org.indexer.core.index;

import org.indexer.iops.FileStreamPositionedTokenizer;
import org.indexer.iops.PositionedToken;

import java.io.IOException;

/**
 * Created by artem on 12/15/14.
 */
public class IndexBuilder {

    private Index index = new Index();

    private volatile boolean stopped = false;

    private final FileIdGenerator fileIdGenerator = new FileIdGenerator();

    /**
     * add file to the index. This method is idempotent if and only if the method 'add' of
     * PostingList idempotent too.
     *
     * @param filePath - path to file to index
     * @return - next PositionedToken, to give an ability to add
     * remaining tokens to the other index.
     *
     * @throws java.io.IOException
     */
    public PositionedToken addFile(String filePath) throws IOException {
        int fileId = fileIdGenerator.getFileId(filePath);
        FileStreamPositionedTokenizer tokenizer = new FileStreamPositionedTokenizer(filePath);

        PositionedToken token;
        while ((token = tokenizer.nextToken()) != null && (!stopped)) {
            index.insert(token.getToken(), fileId, token.getPosition());
        }
        return token;
    }

    /**
     * implements the ability to stop index building.
     * Useful if index is building from different threads.
     */
    public void stopBuilding() {
        stopped = true;
    }

    public Index build() {
        return index;
    }
}
