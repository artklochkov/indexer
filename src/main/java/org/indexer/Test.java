package org.indexer;

import org.indexer.core.index.Index;
import org.indexer.core.index.PostingList;
import org.indexer.core.index.PostingNode;
import org.indexer.core.trie.Trie;

import java.io.*;
import java.util.*;

/**
 * Created by artem on 12/13/14.
 */
public class Test {

    public static void main(String[] args) throws IOException,ClassNotFoundException {
//        System.out.println("Hello, world!");
//
//        Trie trie = new Trie();
//
//        FileInputStream fis = new FileInputStream("/home/artem/index.out");
//        ObjectInputStream oin = new ObjectInputStream(fis);
//        Index index = (Index) oin.readObject();
//
//        Map<String, Collection<PostingNode>> postingListMap = index.getPostingNodes("war");
//        Collection<PostingNode> postingNodes;
//        int wordsCount = 0;
//        for (Map.Entry<String, Collection<PostingNode>> entry : postingListMap.entrySet()) {
////            System.out.println("for word: " + entry.getKey() + " " + entry.getValue().getNodes().size() + " nodes found");
//            postingNodes = entry.getValue();
//            wordsCount += postingNodes.size();
////            for (PostingList.PostingNode postingNode : postingNodes) {
////                System.out.println(postingNode.getDocumentId() + " | " + postingNode.getDocumentPosition());
////            }
//        }
//        System.out.println("deserialized war: " + wordsCount);

//        Scanner sc2 = null;
//        try {
//            sc2 = new Scanner(new File("/home/artem/IdeaProjects/indexer/src/test/resources/crimeAndPunishment.txt"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//
//
//        long wordsCount = 0;
//////        Set<String> allWords = new HashSet();
////        IndexBuilder indexBuilder = new IndexBuilder(1);
//        while (sc2.hasNext()) {
//            String nextLine = sc2.nextLine();
//            String[] words = nextLine.split("\\s");
//            for (String word : words) {
//                word = word.toLowerCase();
//                if (word.indexOf("war") >= 0) {
//                    ++wordsCount;
//                }
//                //totalWords.add(word);
////                indexBuilder.append(word, 1, 123);
////                allWords.add(word);
////                for (int i = 0; i < word.length(); ++i) {
////                    allWords.add(word.substring(1));
////                }
////                wordsCount++;
//            }
//        }
//        System.out.println("war: " + wordsCount);
////        Index index = indexBuilder.build();
////        List<String> results = index.findWordsByPattern("th");
////        for (String result : results) {
////            System.out.println(result);
////        }
//
//
//        Index index = new Index();
//        long startTime = System.currentTimeMillis();
//        index.addFile("/home/artem/IdeaProjects/indexer/src/test/resources/crimeAndPunishment.txt");
//        long elapsed = System.currentTimeMillis() - startTime;
//        System.out.println("time spent: " + elapsed);
//        System.gc();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Runtime rt = Runtime.getRuntime();
//        long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
//        System.out.println("memory usage: " + usedMB + " MB");
////        FileStreamPositionedTokenizer fileStreamPositionedTokenizer = new FileStreamPositionedTokenizer("/home/artem/IdeaProjects/indexer/src/main/java/simple-test.txt");
////        PositionedToken positionedToken;
////        while ((positionedToken = fileStreamPositionedTokenizer.nextToken()) != null) {
////            System.out.println(positionedToken.getToken() + " | " + positionedToken.getPosition());
////        }
////        return;
//        Map<String, PostingList> postingListMap = index.getPostingNodes("war");
//        PostingList postingList;
//        wordsCount = 0;
//        for (Map.Entry<String, PostingList> entry : postingListMap.entrySet()) {
////            System.out.println("for word: " + entry.getKey() + " " + entry.getValue().getNodes().size() + " nodes found");
//            postingList = entry.getValue();
//            Collection<PostingList.PostingNode> postingNodes = postingList.getNodes();
//            wordsCount += postingNodes.size();
////            for (PostingList.PostingNode postingNode : postingNodes) {
////                System.out.println(postingNode.getDocumentId() + " | " + postingNode.getDocumentPosition());
////            }
//        }
//        System.out.println("war: " + wordsCount);
//
//        FileOutputStream fos = new FileOutputStream("/home/artem/index.out");
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//
//        oos.writeObject(index);
//        oos.flush();
//        oos.close();

//        IndexBuilder indexBuilder = new IndexBuilder(1);
//        System.out.println("let's calculate time...");
//        long startTime = System.currentTimeMillis();
//        for (String word : totalWords) {
//            indexBuilder.append(word);
//        }
//        Index index = indexBuilder.build();
//        long elapsed = System.currentTimeMillis() - startTime;
//        System.out.println("Index build took total: " + elapsed + " milliseconds");


//        System.gc();
//        try {
//            Thread.sleep(7500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Runtime rt = Runtime.getRuntime();
//        long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
//        System.out.println(wordsCount + " words count");
//        System.out.println("memory usage: " + usedMB + " MB");
//        System.out.println("total memory: " + rt.totalMemory() / 1024/ 1024);
////        System.out.println("trie contains: " + index.calculateNodes() + " nodes for " + wordsCount + " words");
////        System.out.println("set contains: " + allWords.size() + " words");
////        System.out.println("char map: " + TrieNode.chars());
    }
}
