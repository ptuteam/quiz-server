package database.test;

import game.QuestionHelper;

/**
 * Created by dima on 12.11.15.
 */
public class Main {
    public static void main(String[] args) {
        QuestionHelper helper = new QuestionHelper();

        System.out.println(helper.getQuestion(1).getText());
        for (String answer : helper.getQuestion(1).getAnswers()){
            System.out.println(answer);
        }
        System.out.println(helper.getQuestion(9).getText());
        for (String answer : helper.getQuestion(9).getAnswers()){
            System.out.println(answer);
        }

        System.out.println(helper.checkAnswer(1, "answer 1, question 1"));
        System.out.println(helper.checkAnswer(1, "answer 2, question 1"));
        System.out.println(helper.checkAnswer(2, "answer 1, question 2"));
        System.out.println(helper.checkAnswer(2, "answer 2, question 2"));
    }
}
