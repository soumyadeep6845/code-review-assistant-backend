package com.example.personal.util;

public class CodeNormalizer {

    public static String normalize(String code) {
        if (code == null) return "";

        // Removing single-line comments (Java, JS, etc.)
        code = code.replaceAll("//.*", "");

        // Removing multi-line comments (/* ... */)
        code = code.replaceAll("/\\*.*?\\*/", "");

        // Normalizing whitespace: collapse multiple whitespace chars into one space
        code = code.replaceAll("\\s+", " ");

        // Trim leading/trailing whitespace
        return code.trim();
    }
}
