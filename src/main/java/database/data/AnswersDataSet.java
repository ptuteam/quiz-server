package database.data;

/**
 * Created by dima on 12.11.15.
 */
public class AnswersDataSet {
    private final int answerId;
    private final String text;
    private final int questionId;
    private final boolean isCorrect;

    public AnswersDataSet(int answerId, String text, int questionId, boolean isCorrect) {
        this.answerId = answerId;
        this.text = text;
        this.questionId = questionId;
        this.isCorrect = isCorrect;
    }

    @SuppressWarnings("unused")
    public int getId() {
        return answerId;
    }

    public String getText() {
        return text;
    }

    @SuppressWarnings("unused")
    public int getQuestionId() {
        return questionId;
    }

    @SuppressWarnings("unused")
    public boolean isCorrect() {
        return isCorrect;
    }
}
