package game;

/**
 * alex on 11.11.15.
 */
public class Question {

    private final String title;
    private final String[] answers;
    private final int type;
    private final String correctAnswer;

    public Question(String title, int type, String[] answers, String correctAnswer) {
        this.title = title;
        this.answers = answers;
        this.type = type;
        this.correctAnswer = correctAnswer;
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

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
