package database.data;

/**
 * Created by dima on 12.11.15.
 */
public class QuestionsDataSet {

    private int questionId;
    private String text;
    private int type;

    public QuestionsDataSet(int questionId, String text, int type) {
        this.questionId = questionId;
        this.text = text;
        this.type = type;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }
}
