package database.data;

/**
 * Created by dima on 12.11.15.
 */
public class AnswersDataSet {
    private int answerId;
    private String text;
    private int questionId;
    private boolean isCorrect;

    public AnswersDataSet(int answerId, String text, int questionId, boolean isCorrect) {
        this.answerId = answerId;
        this.text = text;
        this.questionId = questionId;
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return answerId;
    }

    public String getText() {
        return text;
    }

    public int getQuestionId() {
        return questionId;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
