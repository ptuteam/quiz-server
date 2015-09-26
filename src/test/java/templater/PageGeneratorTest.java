package templater;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * alex on 26.09.15.
 */
public class PageGeneratorTest {

    @Test
    public void testGetPage() throws Exception {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("msg", "Test message");
        pageVariables.put("type", "test");
        String result = PageGenerator.getPage("testresponse.txt", pageVariables);
        String expectedResult = "{\n" +
                "    message: \"Test message\",\n" +
                "    type: \"test\"\n" +
                "}";
        assertEquals(expectedResult, result);
    }
}