import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {

    public static AtomicInteger counterThreeLetterWord = new AtomicInteger(0);
    public static AtomicInteger counterFourLetterWord = new AtomicInteger(0);
    public static AtomicInteger counterFiveLetterWord = new AtomicInteger(0);
    public static ArrayList<String> listOfAddedNickNames = new ArrayList<>();

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        new Thread(() -> { //поток, который переворачивает все имена и добавляет палиндромы
            for (String nickName : texts) {
                String flipperNickname = new StringBuilder(nickName).reverse().toString();
                if (nickName.equals(flipperNickname)) {
                   addNickname(nickName);
                }
            }
        }).start();

        new Thread(() -> { //поток, который добавляет все никнеймы, состоящие из одинаковых букв

            for (String nickname : texts) {
                int count = 0;
                for (int i = 0; i < nickname.length() - 1; i++) {
                    if (nickname.charAt(i + 1) != nickname.charAt(i)) {
                        break;
                    } else {
                        count++;
                    }
                }
                if (count == (nickname.length() - 1)) {
                    addNickname(nickname);
                }
            }
        }).start();

        new Thread(() -> { //поток, который переводит никнейм в массив, сортирует его, сравнивает с исходным, и выдает
            //никнеймы, сформированные по возрастанию символов
            for (String nickname : texts) {
                char[] letters = nickname.toCharArray();
                Arrays.sort(letters);
                StringBuilder sort = new StringBuilder();
                for (char letter : letters) {
                    sort.append(letter);
                }
                String sortNickname = sort.toString();
                if (sortNickname.equals(nickname)) {
                    addNickname(nickname);
                }
            }
        }).start();

        System.out.println("Красивых слов с длиной 3: " + counterThreeLetterWord + " шт." + "\n" +
                "Красивых слов с длиной 4: " + counterFourLetterWord + " шт." + "\n" +
                "Красивых слов с длиной 5: " + counterFiveLetterWord + " шт.");
        System.out.println(listOfAddedNickNames); //для быстрой проверки
    }

    public static void addNickname(String nickname) {
        if (!listOfAddedNickNames.contains(nickname)) { //задает оригинальность имени
            if (nickname.length() == 3) {
                counterThreeLetterWord.getAndIncrement();
            } else if (nickname.length() == 4) {
                counterFourLetterWord.getAndIncrement();
            } else if (nickname.length() == 5) {
                counterFiveLetterWord.getAndIncrement();
            }
            listOfAddedNickNames.add(nickname);
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
