package com.scanner.project;
import java.io.*;
import java.util.*;

/**
 * Uday Brathwaite and Daisy Molina
 * TokenStream.java
 * KAY Language Scanner for CS361
 * Performs lexical analysis for the KAY language.
 */
public class TokenStream {
    private BufferedReader reader;
    private int currentChar;

    private static final Set<String> keywords = new HashSet<>(Arrays.asList(
        "main", "integer", "bool", "if", "else", "while", "True", "False"
    ));

    private static final Set<String> operators = new HashSet<>(Arrays.asList(
        ":=", "+", "-", "*", "/", "<", ">", "<=", ">=", "==", "!=", "&&", "||", "!"
    ));

    private static final Set<Character> separators = new HashSet<>(Arrays.asList(
        '(', ')', '{', '}', ';', ','
    ));

    // Use the filename passed in (no hardcoded path)
    public TokenStream(String filename) {
        try {
            reader = new BufferedReader(new FileReader(filename));
            currentChar = reader.read();
        } catch (IOException e) {
            System.out.println("File not found: " + filename);
        }
    }

    private void readNextChar() {
        try {
            currentChar = reader.read();
        } catch (IOException e) {
            currentChar = -1;
        }
    }

    private boolean isLetter(int c) {
        return Character.isLetter((char) c);
    }

    private boolean isDigit(int c) {
        return Character.isDigit((char) c);
    }

    private void skipWhitespaceAndComments() {
        while (currentChar != -1) {
            if (Character.isWhitespace(currentChar)) {
                readNextChar();
            } else if (currentChar == '/') {
                try {
                    reader.mark(2);
                    readNextChar();
                    if (currentChar == '/') {
                        // skip the rest of the line
                        while (currentChar != -1 && currentChar != '\n') {
                            readNextChar();
                        }
                    } else {
                        reader.reset();
                        break;
                    }
                } catch (IOException e) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    public Token nextToken() {
        skipWhitespaceAndComments();

        if (currentChar == -1) {
            return new Token("EOF", "");
        }

        // Identifiers or keywords
        if (isLetter(currentChar)) {
            StringBuilder sb = new StringBuilder();
            while (isLetter(currentChar) || isDigit(currentChar)) {
                sb.append((char) currentChar);
                readNextChar();
            }
            String word = sb.toString();
            if (keywords.contains(word))
                return new Token("Keyword", word);
            else
                return new Token("Identifier", word);
        }

        // Numbers
        if (isDigit(currentChar)) {
            StringBuilder sb = new StringBuilder();
            while (isDigit(currentChar)) {
                sb.append((char) currentChar);
                readNextChar();
            }
            return new Token("Literal", sb.toString());
        }

        // Separators
        if (separators.contains((char) currentChar)) {
            char c = (char) currentChar;
            readNextChar();
            return new Token("Separator", String.valueOf(c));
        }

        // Operators and special cases
        if ((char) currentChar == ':' || (char) currentChar == '<' || (char) currentChar == '>' ||
            (char) currentChar == '=' || (char) currentChar == '!' ||
            (char) currentChar == '&' || (char) currentChar == '|') {

            StringBuilder sb = new StringBuilder();
            sb.append((char) currentChar);
            readNextChar();

            if ((char) currentChar == '=' ||
                ((sb.charAt(0) == '&' || sb.charAt(0) == '|') && sb.charAt(0) == (char) currentChar)) {
                sb.append((char) currentChar);
                readNextChar();
            }

            String op = sb.toString();
            if (operators.contains(op))
                return new Token("Operator", op);
            else
                return new Token("Other", op);
        }

        // Everything else is Other
        char bad = (char) currentChar;
        readNextChar();
        return new Token("Other", String.valueOf(bad));
    }
}

