package com.example.wordlefx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class WordLoader {

    public List<String> loadWordsFromFile(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .filter(word -> word.length() == 5)
                .collect(Collectors.toList());
    }
}