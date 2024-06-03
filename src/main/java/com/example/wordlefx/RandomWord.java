package com.example.wordlefx;

import java.util.List;
import java.util.Random;

public class RandomWord {

    private final List<String> words;
    private final Random random;

    public RandomWord(List<String> words) {
        this.words = words;
        this.random = new Random();
    }

    public String getRandomWord() {
        int index = random.nextInt(words.size());
        return words.get(index);
    }
}
