package game;

/**
 * alex on 11.11.15.
 */
public class Question {

    private final String text;
    private final String[] answers;
    private final int type;

    public Question(String text, int type, String[] answers) {
        this.text = text;
        this.answers = answers;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getType() {
        return type;
    }
}
