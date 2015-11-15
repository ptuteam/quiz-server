package database.data;

/**
 * Created by dima on 12.11.15.
 */
public class QuestionsDataSet {

    private final int questionId;
    private final String title;
    private final int type;

    public QuestionsDataSet(int questionId, String title, int type) {
        this.questionId = questionId;
        this.title = title;
        this.type = type;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }
}
