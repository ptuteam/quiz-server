package database.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dima on 24.11.15.
 */
@SuppressWarnings("unused")
public class AnswersDataSetTest {

    private AnswersDataSet correctAnswer;
    private AnswersDataSet incorrectAnswer;

    @Before
    public void setUp() throws Exception {
        correctAnswer = new AnswersDataSet(5, "correct", 8, true);
        incorrectAnswer = new AnswersDataSet(6, "incorrect", 8, false);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(5, correctAnswer.getId());
        assertEquals(6, incorrectAnswer.getId());
    }

    @Test
    public void testGetText() throws Exception {
        assertEquals("correct", correctAnswer.getText());
        assertEquals("incorrect", incorrectAnswer.getText());
    }

    @Test
    public void testGetQuestionId() throws Exception {
        assertEquals(8, correctAnswer.getQuestionId());
        assertEquals(8, incorrectAnswer.getQuestionId());
    }

    @Test
    public void testIsCorrect() throws Exception {
        assertTrue(correctAnswer.isCorrect());
        assertFalse(incorrectAnswer.isCorrect());
    }
}