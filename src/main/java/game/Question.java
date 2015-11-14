package game;

/**
 * alex on 11.11.15.
 */
public class Question {

    private final String title;
    private final String[] answers;
    private final int type;

    public Question(String title, int type, String[] answers) {
        this.title = title;
        this.answers = answers;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getType() {
        return type;
    }
}
