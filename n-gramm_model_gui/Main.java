import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Выберите удобный для вас вариант: ");
        int choice = sc.nextInt();
        TextGenerationModel tgm = new TextGenerationModel(choice);
        tgm.train(choice);
        GUI menu = new GUI(tgm.get_word_dict());
    }
}