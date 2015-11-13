package game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * alex on 26.10.15.
 */
public class QuestionHelper {
    public static final int QUESTIONS_COUNT = 5;

    private final Map<String, String[]> questions = new HashMap<>();
    private final Map<String, String> answersByQuestions = new HashMap<>();
    private final List<String> questionsArray;

    public QuestionHelper() {
        questions.put(
                "Выдающийся поэт Востока Омар Хайям ...",
                new String[]{
                        "был автором только литературных произведений",
                        "в средневековом мире был известен лишь как поэт",
                        "на Востоке был широко известен как государственный деятель, поэт и писатель XI в.",
                        "был не только поэтом, но и одним из крупнейших ученых XI в."
                }
        );
        answersByQuestions.put("Выдающийся поэт Востока Омар Хайям ...", "был не только поэтом, но и одним из крупнейших ученых XI в.");

        questions.put(
                "Какие бывшие союзные республики осуществили перераспределение земель в период, когда РСФСР передала Украине Крым?",
                new String[]{
                        "Узбекистан и Таджикистан",
                        "Казахстан и Кыргызстан",
                        "Туркменистан и Казахстан",
                        "Узбекистан и Туркменистан"
                }
        );
        answersByQuestions.put(
                "Какие бывшие союзные республики осуществили перераспределение земель в период, когда РСФСР передала Украине Крым?",
                "Узбекистан и Таджикистан"
        );

        questions.put(
                "Исторически наиболее ранний тип экономической системы — традиционная экономика. Характерным ее признаком является",
                new String[]{
                        "доминирование интересов производителя над интересами потребителя",
                        "регулирование объемов и номенклатуры производства посредством обычаев",
                        "образование монополий, диктующих рынку цены и правила игры",
                        "экономическая свобода потребителя, делающего выбор товаров в условиях альтернативы"
                }
        );
        answersByQuestions.put(
                "Исторически наиболее ранний тип экономической системы — традиционная экономика. Характерным ее признаком является",
                "регулирование объемов и номенклатуры производства посредством обычаев"
        );

        questions.put(
                "По какому признаку выделены такие социальные группы как горожане, селяне, провинциалы, столичные жители?",
                new String[]{
                        "демографическому",
                        "конфессиональному",
                        "этносоциальному",
                        "территориальному"
                }
        );
        answersByQuestions.put(
                "По какому признаку выделены такие социальные группы как горожане, селяне, провинциалы, столичные жители?",
                "территориальному"
        );

        questions.put(
                "Производственное объединение предъявило иск фирме-поставщику о ненадлежащем исполнении договора поставки. Данное дело будет рассматриваться в суде",
                new String[]{
                        "конституционном",
                        "арбитражном",
                        "мировом",
                        "уголовном"
                }
        );
        answersByQuestions.put(
                "Производственное объединение предъявило иск фирме-поставщику о ненадлежащем исполнении договора поставки. Данное дело будет рассматриваться в суде",
                "арбитражном"
        );

        questionsArray = new ArrayList<>(questions.keySet());
    }

    public JsonObject getQuestionWithAnswersJson(int questionId) {
        String question = questionsArray.get(questionId);
        JsonObject jsonObject = new JsonObject();
        JsonArray answersArray = new JsonArray();
        jsonObject.addProperty("question", question);
        jsonObject.add("answers", answersArray);
        for (String answer : questions.get(question)) {
            answersArray.add(answer);
        }
        return jsonObject;
    }

    public boolean checkAnswer(int questionId, String answer) {
        String question = questionsArray.get(questionId);
        String correctAnswer = answersByQuestions.get(question);
        return Objects.equals(correctAnswer, answer);
    }
}
