import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Random;

public class TestParser {
    private static final Parser parser = new Parser();
    private static final Random random = new Random();

    private static final String[] Types = new String[]{"Int", "String", "Long", "Random", "Parser", "char", "Int[]",
            "String[]", "Long[]", "Random[]", "Parser[]", "List<Int>" , "ArrayList<Int>", "Collection<Int[]>"};

    @Test
    public void basicAccept() {
        accept("fun @()");
        accept("fun @(): #");
        accept("fun @(@: #)");
        accept("fun @(@: #, @: #)");
        accept("fun @(@: #, @: #): #");
    }

    @Test
    public void spacesAccept() {
        accept("fun @(@:#,@:#):#");
        accept("fun @ ( @ : # , @ : # ) : #");
        accept("fun\r@\r(\r@\r:\r#\r,\r@\r:\r#\r)\r:\r#");
        accept("fun\t@\t(\t@\t:\t#\t,\t@\t:\t#\t)\t:\t#");
        accept("fun\n@\n(\n@\n:\n#\n,\n@\n:\n#\n)\n:\n#");
    }

    @Test
    public void Reject() {
        reject("fun@()");
        reject("fun ()");
        reject("fun @(");
        reject("fun @)");
        reject("fun @() #");
        reject("fun @():");
        reject("fun @(@ #)");
        reject("fun @(: #)");
        reject("fun @(@: )");
        reject("fun @(@: # @: #)");
    }

    private static void accept(final String s) {
        multiplyTest(s, true);
    }

    private static void reject(final String s) {
        multiplyTest(s, false);
    }

    private static void multiplyTest(final String s, final boolean isAcceptance) {
        for (int j = 0; j < 10; j++) {
            final String start = insertNames(s);
            final String end = insertTypes(start);

            if (isAcceptance) {
                assertAcceptance(end);
            } else {
                assertReject(end);
            }
        }
    }

    private static String insertNames(final String s) {
        final int countOperations = countChar(s, '@');
        String res = s;

        for (int i = 0; i < countOperations; i++) {
            res = res.replaceFirst("@", generateRandomWords(1)[0]);
        }

        return res;
    }

    private static String insertTypes(final String s) {
        final int countVars = countChar(s, '#');
        String res = s;

        for (int i = 0; i < countVars; i++) {
            res = res.replaceFirst("#", Types[random.nextInt(Types.length)]);
        }

        return res;
    }

    private static int countChar(final String s, final char ch) {
        int res = 0;

        for (final char c : s.toCharArray()) {
            if (c == ch) {
                res++;
            }
        }

        return res;
    }

    private static void assertAcceptance(final String s) {
        System.out.printf("expression: \"%s\"%n", s);

        try {
            Assert.assertNotNull(parser.parse(s));
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
        }
    }

    private static void assertReject(final String s) {
        System.out.printf("expression: \"%s\"%n", s);

        try {
            parser.parse(s);
            Assert.fail();
        } catch (final ParseException e) {
            Assert.assertNotNull(e.getMessage());
        }

    }

    public static String[] generateRandomWords(final int numberOfWords) {
        final String[] randomStrings = new String[numberOfWords];
        final Random random = new Random();

        for (int i = 0; i < numberOfWords; i++) {
            final char[] word = new char[random.nextInt(8) + 3];

            for (int j = 0; j < word.length; j++) {
                word[j] = random.nextBoolean() ? (char) ('a' + random.nextInt(26)) : '_';
            }

            randomStrings[i] = new String(word);
        }

        return randomStrings;
    }
}