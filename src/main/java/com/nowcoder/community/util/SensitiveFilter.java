package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author byfgg
 * @create 2022-09-05 11:49
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger((SensitiveFilter.class));

    private static final String REPLACEMENT = "***";

    //定义根节点
    private TrieNode rootNode = new TrieNode();

    //类构造器运行后，初始化前缀树
    @PostConstruct
    public void init() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                //将这个词添加到前缀树中去
                this.addKeyWord(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败" + e.getMessage());
        }
    }

    //将一个敏感词添加到前缀树中去
    private void addKeyWord(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null) {
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            tempNode = subNode;
            //如果是最后一个字符，则将其END设置为true
            if (i == keyword.length() - 1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (text == null) {
            return null;
        }

        //定义三个指针和存放可变字符串的sb
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {

            char c = text.charAt(position);

            //跳过特殊符号（除了数字字母之外）
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                position++;
                //跳过本次循环
                continue;
            }
            //检查SubNode
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                sb.append(c);
                position = ++begin;
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd) {
                sb.append(REPLACEMENT);
                begin = ++position;
                tempNode = rootNode;
            } else {
                position++;
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();


    }

    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //定义前缀树
    private class TrieNode {
        private boolean isKeyWordEnd = false;

        private Map<Character, TrieNode> subNodes = new HashMap<>();


        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        //添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        //获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }


    }

}
