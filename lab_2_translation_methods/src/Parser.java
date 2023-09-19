import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class Parser {
    private LexicalAnalyzer lex;
    public static final String START_NONTERMINAL = "START";
    public static final String ARGS_NONTERMINAL = "ARGS";
    public static final String REST_ARGS_NONTERMINAL = "REST_ARGS";
    public static final String FUN_NAME_NONTERMINAL = "FUN_NAME";
    private static final String COMMA_NONTERMINAL = "COMMA";
    private static final String RETURN_TYPE_NONTERMINAL = "RETURN_TYPE";
    public Tree parse(final InputStream is) throws ParseException {
        lex = new LexicalAnalyzer(is);
        final Tree res = start();
        expect(Token.END);
        return res;
    }

    public Tree parse(final String s) throws ParseException {
        return parse(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
    }


    private Tree start() throws ParseException {
        return new Tree(START_NONTERMINAL, terminal(Token.FUN), funName());
    }

    private Tree funName() throws ParseException {
        return new Tree(FUN_NAME_NONTERMINAL, terminal(Token.WORD), terminal(Token.OPENED_BRACKET),
                args(), terminal(Token.CLOSED_BRACKET), returnType());
    }

    private Tree args() throws ParseException {
        if (lex.curToken() == Token.WORD) {
            return new Tree(ARGS_NONTERMINAL, restArgs());
        }

        return new Tree("eps");
    }

    private Tree restArgs() throws ParseException {
        return new Tree(REST_ARGS_NONTERMINAL, terminal(Token.WORD), terminal(Token.COLON), terminal(Token.WORD), comma());
    }

    private Tree comma() throws ParseException {
        if (lex.curToken() == Token.COMMA) {
            return new Tree(COMMA_NONTERMINAL, terminal(Token.COMMA), restArgs());
        }

        return new Tree("eps");
    }

    private Tree returnType() throws ParseException {
        if (lex.curToken() == Token.COLON) {
            return new Tree(RETURN_TYPE_NONTERMINAL, terminal(Token.COLON), terminal(Token.WORD));
        }

        return new Tree("eps");
    }

    private Tree terminal(final Token expected) throws ParseException {
        expect(expected);
        final String value = lex.curToken() == Token.WORD ? lex.tokenValue : lex.curToken().name();
        lex.nextToken();
        return new Tree(value);
    }

    private void expect(final Token token) throws ParseException {
        if (lex.curToken() == token) {
            return;
        }

        throw error("Expect: " + token + ", actual: " + lex.curToken());
    }
    private ParseException error(final String msg) {
        return new ParseException(msg + ", pos: " + lex.curPos(), lex.curPos());
    }
}