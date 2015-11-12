package database.dataSets;

/**
 * Created by dima on 12.11.15.
 */
public class QuestionsDataSet {
    private int id;
    private String text;

    public QuestionsDataSet(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
