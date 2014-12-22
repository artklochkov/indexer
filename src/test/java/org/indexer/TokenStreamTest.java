package org.indexer;

import org.indexer.iops.FileStreamPositionedTokenizer;
import org.indexer.iops.PositionedToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by artem on 12/22/14.
 */
public class TokenStreamTest extends Assert {

    private final static String TEST_RESOURCES_RELATIVE_PATH = "/src/test/resources/";
    private final static String TEST_FILE = "tokenizer.txt";
    private String workingDirectory;
    List<PositionedToken> expectedTokens;

    @Before
    public void initTokenStreamTest() {
        workingDirectory = System.getProperty("user.dir");
        expectedTokens = new ArrayList<>();
        expectedTokens.add(new PositionedToken("one", 0));
        expectedTokens.add(new PositionedToken("two", 4));
        expectedTokens.add(new PositionedToken("three", 8));
        expectedTokens.add(new PositionedToken("four", 14));
        expectedTokens.add(new PositionedToken("five", 19));
        expectedTokens.add(new PositionedToken("six", 24));
        expectedTokens.add(new PositionedToken("seven", 28));
        expectedTokens.add(new PositionedToken("eight", 34));
        expectedTokens.add(new PositionedToken("nine", 40));
        expectedTokens.add(new PositionedToken("ten", 45));
    }

    @Test
    public void testFileReading() throws IOException {
        String pathToFile = workingDirectory + TEST_RESOURCES_RELATIVE_PATH + "/" + TEST_FILE;
        FileStreamPositionedTokenizer tokenizer = new FileStreamPositionedTokenizer(pathToFile);
        PositionedToken token;
        Iterator<PositionedToken> tokensIterator = expectedTokens.iterator();
        while ((token = tokenizer.nextToken()) != null) {
            assertEquals(tokensIterator.next(), token);
        }
    }
}
