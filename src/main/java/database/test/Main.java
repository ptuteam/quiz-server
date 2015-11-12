package database.test;

import game.QuestionHelper;

/**
 * Created by dima on 12.11.15.
 */
public class Main {
    public static void main(String[] args) {
        QuestionHelper helper = new QuestionHelper();

        System.out.print(helper.getQuestionWithAnswersJson(1).toString());
        System.out.print(helper.getQuestionWithAnswersJson(2).toString());
        System.out.print(helper.getQuestionWithAnswersJson(3).toString());
        System.out.print(helper.getQuestionWithAnswersJson(4).toString());
        System.out.print(helper.getQuestionWithAnswersJson(5).toString());
    }
}
