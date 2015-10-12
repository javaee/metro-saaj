package mime;

import com.sun.xml.messaging.saaj.packaging.mime.internet.MimeUtility;
import com.sun.xml.messaging.saaj.packaging.mime.internet.ParseException;

import java.io.UnsupportedEncodingException;
import junit.framework.TestCase;
/**
 * Created by desagar on 10/11/15.
 */
public class MimeUtilityTest extends TestCase {
  public void testDecodeWord() throws UnsupportedEncodingException, ParseException {
    //decode sample encoded words from RFC 2047
    String encodedWord1 = "=?ISO-8859-1?Q?a?=";
    String decodedWord1 = MimeUtility.decodeWord(encodedWord1);
    assertEquals("a", decodedWord1);

    String encodedWord2 = "=?ISO-8859-1?Q?a?= b";
    String decodedWord2 = MimeUtility.decodeWord(encodedWord2);
    assertEquals("a b", decodedWord2);
  }

  public void testEncodeDecodeWithDefaults() throws UnsupportedEncodingException, ParseException {
    //use non-ascii word since ascii is not encoded
    String word = "abc\u0b85";
    String encodedWord = MimeUtility.encodeWord(word);
    String decodedWord = MimeUtility.decodeWord(encodedWord);
    assertEquals(word, decodedWord);
  }
}
