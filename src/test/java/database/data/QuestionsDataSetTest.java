package database.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dima on 24.11.15.
 */
@SuppressWarnings("unused")
public class QuestionsDataSetTest {

    private QuestionsDataSet question;

    @Before
    public void setUp() throws Exception {
        question = new QuestionsDataSet(8, "question", 1);
    }

    @Test
    public void testGetQuestionId() throws Exception {
        assertEquals(8, question.getQuestionId());
    }

    @Test
    public void testGetTitle() throws Exception {
        assertEquals("question", question.getTitle());
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(1, question.getType());
    }
}