import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public class LexicalAnalyzer {
    private static final String FUN = "fun";
    private final InputStream is;
    private char curChar;
    private int curPos;
    private Token curToken;
    public String tokenValue;

    public LexicalAnalyzer(final InputStream is) throws ParseException {
        this.is = is;
        curPos = 0;
        nextChar();
        nextToken();
    }

    private void nextChar() throws ParseException {
        curPos++;

        try {
            curChar = (char) is.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), curPos);
        }
    }

    public void nextToken() throws ParseException {
        while (Character.isWhitespace(curChar) && curChar != (char) -1) {
            nextChar();
        }

        if (curChar == (char) -1) {
            curToken = Token.END;
            curChar = '$';
            return;
        }

        if (Character.isLetter(curChar) || curChar == '_') {
            tokenValue = getString();

            if (tokenValue.equals(FUN)) {
                curToken = Token.FUN;
            } else {
                curToken = Token.WORD;
            }

            return;
        }

        switch (curChar) {
            case '(' -> {
                curToken = Token.OPENED_BRACKET;
                nextChar();
            }

            case ')' -> {
                curToken = Token.CLOSED_BRACKET;
                nextChar();
            }

            case ':' -> {
                curToken = Token.COLON;
                nextChar();
            }

            case ',' -> {
                curToken = Token.COMMA;
                nextChar();
            }

            default -> throw new ParseException("Illegal character " + curChar, curPos);
        }
    }

    public Token curToken() {
        return curToken;
    }

    public int curPos() {
        return curPos;
    }

    public String getString() throws ParseException {
        StringBuilder value = new StringBuilder();

        if (Character.isLetter(curChar) || curChar == '_') {
            value.append(curChar);
            nextChar();

            while(Character.isLetter(curChar) || curChar == '_' || curChar == '[' || curChar == ']'
                    || Character.isDigit(curChar) || curChar == '<' || curChar == '>'){
                value.append(curChar);
                nextChar();
            }
        }

        return value.toString();
    }

}

