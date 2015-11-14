package database.data;

/**
 * Created by dima on 12.11.15.
 */
public class QuestionsDataSet {

    private final int questionId;
    private final String text;
    private final int type;

    public QuestionsDataSet(int questionId, String text, int type) {
        this.questionId = questionId;
        this.text = text;
        this.type = type;
    }

    @SuppressWarnings("unused")
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
