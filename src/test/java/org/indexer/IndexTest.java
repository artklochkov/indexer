package org.indexer;

import org.indexer.core.index.Index;
import org.indexer.core.index.IndexBuilder;
import org.indexer.core.index.PostingList;
import org.indexer.core.index.PostingNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Tests for main Index class
 */
public class IndexTest extends Assert {

    private Index index;
    private String workingDirectory;

    private final static String TEST_RESOURCES_RELATIVE_PATH = "/src/test/resources/";
    private static final String TEST_FILE = "crimeAndPunishment.txt";

    @Before
    public void initIndex() {
        this.index = new Index();
        workingDirectory = System.getProperty("user.dir");
    }

    private int countWordsInIndex(Index index, String pattern) {
        int wordsCount = 0;
        Map<String, Collection<PostingNode>> postingListMap = index.getPostingNodes(pattern);
        for (Map.Entry<String, Collection<PostingNode>> entry : postingListMap.entrySet()) {
            Collection<PostingNode> postingNodes = entry.getValue();
            wordsCount += postingNodes.size();
        }
        return wordsCount;
    }

    @Test
    public void warCountTest() throws IOException {
        String pathToFile = workingDirectory + TEST_RESOURCES_RELATIVE_PATH + "/" + TEST_FILE;
        IndexBuilder indexBuilder = new IndexBuilder();
        indexBuilder.addFile(pathToFile);
        Index index = indexBuilder.build();
        int wordsCount = countWordsInIndex(index, "war");
        assertEquals(281, wordsCount);
    }

    @Test
    public void multipleThreadsTest() throws IOException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        String pathToFile = workingDirectory + TEST_RESOURCES_RELATIVE_PATH + "/" + TEST_FILE;
        final IndexBuilder indexBuilder = new IndexBuilder();
        for (int i = 0; i < 10; ++i) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        indexBuilder.addFile(pathToFile);
                    } catch (Throwable e) {
                        e.printStackTrace(System.err);
                    }
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        Index index = indexBuilder.build();
        int wordsCount = countWordsInIndex(index, "war");
        assertEquals(281, wordsCount);
    }
}
