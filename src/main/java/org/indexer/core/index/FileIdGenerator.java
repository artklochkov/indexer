package org.indexer.core.index;

import net.jcip.annotations.ThreadSafe;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generates file identifiers to simplify work with documents in collection.
 */
@ThreadSafe
public class FileIdGenerator implements Serializable {

    ConcurrentMap<String, Integer> filesToIdMapping = new ConcurrentHashMap<>();
    Integer currentFileId = 0;

    /**
     * @param filepath - file to add to indexed collection
     * @return existing, or just generated file id.
     */
    public int getFileId(String filepath) {
        Integer fileId = filesToIdMapping.get(filepath);
        if (fileId == null) {
            synchronized (this) {
                fileId = filesToIdMapping.get(filepath);
                if (fileId == null) {
                    fileId = ++currentFileId;
                    filesToIdMapping.put(filepath, fileId);
                }
            }
        }
        return fileId;
    }
}
