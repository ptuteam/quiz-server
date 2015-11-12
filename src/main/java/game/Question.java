package game;

/**
 * alex on 11.11.15.
 */
public class Question {

    private final String text;
    private final String[] answers;

    public Question(String text, String[] answers) {
        this.text = text;
        this.answers = answers;
    }

    public String getText() {
        return text;
    }

    public String[] getAnswers() {
        return answers;
    }
}
