package database.test;

import game.QuestionHelper;

/**
 * Created by dima on 12.11.15.
 */
public class Main {
    public static void main(String[] args) {
        QuestionHelper helper = new QuestionHelper();

        System.out.println(helper.getQuestionWithAnswersJson(1).toString());
        System.out.println(helper.getQuestionWithAnswersJson(2).toString());
        System.out.println(helper.getQuestionWithAnswersJson(3).toString());
        System.out.println(helper.getQuestionWithAnswersJson(4).toString());
        System.out.println(helper.getQuestionWithAnswersJson(5).toString());

        System.out.println(helper.checkAnswer(1, "answer 1, question 1"));
        System.out.println(helper.checkAnswer(1, "answer 2, question 1"));
        System.out.println(helper.checkAnswer(2, "answer 1, question 2"));
        System.out.println(helper.checkAnswer(2, "answer 2, question 2"));
    }
}
