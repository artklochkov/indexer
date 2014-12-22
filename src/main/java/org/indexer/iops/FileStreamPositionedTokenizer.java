package org.indexer.iops;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Tokenizer, which can be used to work with large files, without lines delimiters.
 * Gives the ability to get not only token, but it's position in file too.
 */
public class FileStreamPositionedTokenizer {

    private static final int BUFFER_SIZE = 16384;

    private ReadableByteChannel source;
    private ByteBuffer byteBuffer;
    private CharBuffer charBuffer;
    private CharsetDecoder decoder;
    private StringBuilder tokenBuilder = new StringBuilder();
    private Queue<PositionedToken> tokensBuffer = new ArrayDeque<>();
    private long position = 0;

    public FileStreamPositionedTokenizer(String filePath) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        source =  fileInputStream.getChannel();
        Charset charset = Charset.defaultCharset();
        decoder = charset.newDecoder();
        byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        charBuffer = CharBuffer.allocate(BUFFER_SIZE);

        decoder.onMalformedInput(CodingErrorAction.IGNORE);
        decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
        decoder.reset();
        tokenBuilder = new StringBuilder();
    }

    public void close() throws IOException {
        source.close();
    }

    /**
     * Gives index of the next token in the pattern. Also increases
     * internal file position.
     *
     * @param pattern - pattern to search in.
     * @param token - token, to search for.
     * @param index - starn index to search from
     * @param previousTokenLength - previous token (chars) length.
     * @return - index of the next token in the pattern
     */
    private int shiftToToken(String pattern, String token, int index, int previousTokenLength) {
        int newIndex = pattern.indexOf(token, index + previousTokenLength);
        int shift = pattern.substring(index, newIndex).getBytes().length;
        index = newIndex;
        position += shift;
        return index;
    }

    /**
     * Put tokens to internal buffer from internal StringBuilder.
     */
    private void putTokensToBuffer() {
        String nextTokenS = tokenBuilder.toString();
        nextTokenS = nextTokenS.toLowerCase();
        String[] tokens = nextTokenS.split("\\s");
        if (tokens.length == 0) {
            position += tokenBuilder.toString().getBytes().length;
            tokenBuilder.delete(0, tokenBuilder.length());
            return;
        }
        int index = 0;
        String lastToken = tokens[tokens.length - 1];
        int previousTokenLength = 0;
        for (int i = 0; i < tokens.length - 1; ++i) {
            index = shiftToToken(nextTokenS, tokens[i], index, previousTokenLength);
            previousTokenLength = tokens[i].length();
            tokensBuffer.offer(new PositionedToken(tokens[i], position));
        }
        index = shiftToToken(nextTokenS, lastToken, index, previousTokenLength);
        tokenBuilder.replace(0, tokenBuilder.length(), nextTokenS.substring(index));
    }

    /**
     * Gives next token with it's position in file
     * @return - String token with it's position
     * @throws IOException
     */
    public PositionedToken nextToken() throws IOException {
        if (!tokensBuffer.isEmpty() || !source.isOpen()) {
            return tokensBuffer.poll();
        }

        int lastReadCode = -1;
        while (tokensBuffer.isEmpty() && ((lastReadCode = source.read(byteBuffer)) != -1)) {
            byteBuffer.flip();
            decoder.decode(byteBuffer, charBuffer, false);
            charBuffer.flip();
            tokenBuilder.append(charBuffer);
            putTokensToBuffer();
            charBuffer.clear();
            byteBuffer.compact();
        }

        if (tokensBuffer.isEmpty() || (lastReadCode == -1)) {
            source.close();
            byteBuffer.flip();
            decoder.decode(byteBuffer, charBuffer, true);
            decoder.flush(charBuffer);
            charBuffer.flip();
            tokenBuilder.append(charBuffer);
            putTokensToBuffer();
            tokensBuffer.offer(new PositionedToken(tokenBuilder.toString(), position));
        }
        return tokensBuffer.poll();
    }
}
