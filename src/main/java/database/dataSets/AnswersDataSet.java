package database.dataSets;

/**
 * Created by dima on 12.11.15.
 */
public class AnswersDataSet {
    private int id;
    private String text;
    private int questionId;
    private boolean isCorrect;

    public AnswersDataSet(int id, String text, int questionId, boolean isCorrect) {
        this.id = id;
        this.text = text;
        this.questionId = questionId;
        this.isCorrect = isCorrect;
    }

    public int getId() {
        return id;
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
