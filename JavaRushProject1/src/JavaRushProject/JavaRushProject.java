package JavaRushProject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JavaRushProject {
    private static char[] alphabet = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', ' ', '.', ',', '?', '!', ':', '-', '"', '\''};
    private static Scanner scanner = new Scanner(System.in);
    private static Charset utf8 = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        String text = "Выберите действие: \n1. Шифрование текста\n2. Дешифровка по ключу\n3. Дешифровка методом brute force\nИли введите \"exit\" для выхода";
        System.out.println(text);
        String choose = scanner.nextLine();
        while (!choose.equals("1") && !choose.equals("2") && !choose.equals("3") && !choose.equalsIgnoreCase("exit")) {
            System.out.println("Введены неверные данные. Попробуйте снова.");
            choose = scanner.nextLine();
        }
        if (choose.equals("1")) {
            encryption();
        } else if (choose.equals("2")) {
            decoding();
        } else if (choose.equals("3")) {
            bruteForce();
        } else if (choose.equalsIgnoreCase("exit")) {
            return;
        }
    }

    private static char[] startOfMethods() {
//метод, читающий текст из исходного файла, и возвращающий его в виде массива символов
//с него начинаются все три метода для шифрования/дешифровки
        System.out.println("Введите адрес исходного текстового файла.");
        String origFileAdress;
        String origTextString = null;
        while (origTextString == null) {
            try {
                origFileAdress = scanner.nextLine();
                if (origFileAdress.equalsIgnoreCase("exit")) {
                    origTextString = origFileAdress;
                    break;

                } else {
                    Path origFile = Path.of(origFileAdress);
                    origTextString = Files.readString(origFile).toLowerCase();
                }
            } catch (IOException e) {
                System.out.println("Ошибка! Попробуйте ввести другой адрес файла, или введите \"exit\" для выхода.");
            }
        }
        if (origTextString.equalsIgnoreCase("exit")) {
            return null;
        } else {
            char[] origTextChars = origTextString.toCharArray();
            return origTextChars;
        }
    }

    private static void endsOfMethods(char[] newTextChars) {
//метод, создающий файл с новым текстом
//им заканчиваются все три метода для шифрования/дешифровки

//я хотела, чтобы пользователь сам вводил адрес нового файла через Scanner, но не получилось
//даже просто имя файла не удается ввести, программа просто пропускает Scanner, переходя сразу к созданию файла
//подскажите, пожалуйста, как правильно было бы сделать так, чтобы пользователь сам выбирал, куда сохранять новый файл?

        String newTextString = new String(newTextChars);
        String newFileAdress = "d:\\newTextFile.txt";
        try {
            Path newFile = Path.of(newFileAdress);
            Files.createFile(newFile);
            Files.writeString(newFile, newTextString, utf8);
            System.out.println("Создан новый файл по адресу " + newFileAdress);
        } catch (IOException e) {
            System.out.println("Ошибка! Не удалось создать файл по адресу" + newFileAdress);
        }
    }

    public static void encryption() {
//метод для шифрования текста по заданному пользователем ключу
        char[] origTextChars = startOfMethods();
        if (origTextChars == null) {
            return;
        }
        char[] encryptTextChars = new char[origTextChars.length];
        System.out.println("Введите ключ");
        int key = scanner.nextInt();
        for (int i = 0; i < origTextChars.length; i++) {
            for (int j = 0; j < alphabet.length; j++) {
//перебираю символы текста и символы алфавита, сравниваю их между собой
                if (origTextChars[i] == alphabet[j]) {
//исключаю возможность выхода за границу массива с алфавитом, вместо этого возвращаю счетчик к началу алфавита
                    int tmp = j + key;
                    while ((tmp) >= alphabet.length) {
                        tmp = tmp - alphabet.length;
                    }
                    encryptTextChars[i] = alphabet[tmp];
                    break;
                }
            }
            //если символ отсутствует в моем алфавите, в пустую ячейку вставляется символ из исходного файла
            if (encryptTextChars[i] == 0) {
                encryptTextChars[i] = origTextChars[i];
            }
        }
        endsOfMethods(encryptTextChars);
    }

    public static void decoding() {
//Метод для расшифровки по известному ключу
        char[] origTextChars = startOfMethods();
        if (origTextChars == null) {
            return;
        }
        char[] decodeTextChars = new char[origTextChars.length];
        System.out.println("Введите ключ");
        int key = scanner.nextInt();
        for (int i = 0; i < origTextChars.length; i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if (origTextChars[i] == alphabet[j]) {
                    int tmp = j - key;
                    while ((tmp) < 0) {
                        tmp = tmp + alphabet.length;
                    }
                    decodeTextChars[i] = alphabet[tmp];
                    break;
                }
            }
            if (decodeTextChars[i] == 0) {
                decodeTextChars[i] = origTextChars[i];
            }
        }
        endsOfMethods(decodeTextChars);
    }

    public static void bruteForce() {
//метод для расшифровки текста методом подбором ключа
        char[] origTextChars = startOfMethods();
        if (origTextChars == null) {
            return;
        }
        char[] decodeTextChars = new char[origTextChars.length];
        char[] testDecodeTextChars = new char[origTextChars.length];
        int key = 0;
        while (key < alphabet.length) {
            key++;
            //пробую расшифровать текст с ключом = 1, если не удастся, увеличу ключ на 1
            for (int i = 0; i < origTextChars.length; i++) {
                for (int j = 0; j < alphabet.length; j++) {
                    if (origTextChars[i] == alphabet[j]) {
                        int tmp = j - key;
                        if (tmp < 0) {
                            tmp = tmp + alphabet.length;
                        }
                        testDecodeTextChars[i] = alphabet[tmp];
                        break;
                    }
                }
            }
            String testDecodeTextString = new String(testDecodeTextChars);
            //вызываю метод проверки условий, если false, то иду на следующую итерацию
            boolean ch = check(testDecodeTextString);
            if (!ch) continue;
            //если true, то выхожу из цикла
            if (ch) {
                decodeTextChars = testDecodeTextChars;
                //если есть пустые ячейки, заполняю их символами оригинального массива
                for (int i = 0; i < decodeTextChars.length; i++) {
                    if (decodeTextChars[i] == 0)
                        decodeTextChars[i] = origTextChars[i];
                }
                System.out.println("Ключ был равен " + key);
                break;
            }
        }
        endsOfMethods(decodeTextChars);
    }

    private static Boolean check(String testString) {
//метод для проверки получившихся текстов
        String[] substrings = testString.split(" ");
        boolean bool = true;
        for (int i = 0; i < substrings.length; i++) {
            char[] temp = substrings[i].toCharArray();
            for (int j = 0; j < temp.length; j++) {
                if (j < (temp.length - 1)) {
                    if (temp[j] == '.' || temp[j] == ',' || temp[j] == '?' || temp[j] == '!' || temp[j] == ':') {
                        bool = false;
                        break;
                    }
                }
            }
            if (substrings[i].length() > 30) {
                bool = false;
                break;
            }
        }
        return bool;
    }
}

/*
получаем файл для расшифровки
переводим его в массив char
прогоняем с ключом = 1 в двух вложенных циклах
преобразуем в строку
переходим в метод для проверки
разделяем на подстроки по пробелу
проверяем, что знаков препинания нет в середине подстрок
проверяем, что длина подстрок не более 30 символов
если все выполнилось,выходим из цикла, записываем строку в файл
если не выполнилось, переходим на новый виток цикла с ключом = 2
 */
