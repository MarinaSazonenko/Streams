import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainGames {
    private static final String GAMES_PATH = "D:\\Games";
    private static StringBuilder log = new StringBuilder();

    public static void main(String[] args) {
        if (!checkGamesDirectory()) {
            System.out.println("Программа завершена из-за отсутствия папки Games");
            System.out.println(log.toString());
            return;
        }
        List<String> directoriesToCreate = Arrays.asList(
                GAMES_PATH + "\\src",
                GAMES_PATH + "\\res",
                GAMES_PATH + "\\savegames",
                GAMES_PATH + "\\temp",

                GAMES_PATH + "\\src\\main",
                GAMES_PATH + "\\src\\test",

                GAMES_PATH + "\\res\\drawables",
                GAMES_PATH + "\\res\\vectors",
                GAMES_PATH + "\\res\\icons"
        );

        List<String> filesToCreate = Arrays.asList(

                GAMES_PATH + "\\src\\main\\Main.java",
                GAMES_PATH + "\\src\\main\\Utils.java",

                GAMES_PATH + "\\temp\\temp.txt"
        );

        createDirectories(directoriesToCreate);
        createFiles(filesToCreate);
        saveLogToFile();
        System.out.println("Установка завершена. Проверьте файл temp.txt для деталей.");
    }

    private static boolean checkGamesDirectory() {
        File gamesDir = new File(GAMES_PATH);
        if (!gamesDir.exists()) {
            log.append("ОШИБКА: Папка Games не найдена по пути ").append(GAMES_PATH).append("\n");
            return false;
        }
        return true;
    }

    private static void createDirectories(List<String> directories) {

             for (String dirPath : directories) {
            createDirectory(dirPath);
        }
    }

    private static void createFiles(List<String> files) {

        for (String filePath : files) {
            createFile(filePath);
        }
    }

    private static void createDirectory(String path) {
        File dir = new File(path);

        if (dir.mkdir()) {
            log.append("✓ Директория создана: ").append(path).append("\n");
        } else {
            if (dir.exists()) {
                log.append("→ Директория уже существует: ").append(path).append("\n");
            } else {
                log.append("✗ НЕ УДАЛОСЬ создать директорию: ").append(path)
                        .append(" (возможно, нет прав доступа или не существует родительской папки)\n");
            }
        }
    }

    private static void createFile(String path) {
        File file = new File(path);

        try {
            if (file.createNewFile()) {
                log.append("✓ Файл создан: ").append(path).append("\n");
            } else {
                if (file.exists()) {
                    log.append("→ Файл уже существует: ").append(path).append("\n");
                } else {
                    log.append("✗ НЕ УДАЛОСЬ создать файл: ").append(path).append("\n");
                }
            }
        } catch (IOException e) {
            log.append("✗ ОШИБКА при создании файла ").append(path)
                    .append(": ").append(e.getMessage()).append("\n");
        }
    }

    private static void saveLogToFile() {

        File tempFile = new File(GAMES_PATH + "\\temp\\temp.txt");

        File parentDir = tempFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
            log.append("✓ Создана недостающая директория: ").append(parentDir.getPath()).append("\n");
        }

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(log.toString());
            System.out.println("\n✅ Лог успешно записан в файл: " + tempFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("\n❌ КРИТИЧЕСКАЯ ОШИБКА: Не удалось записать лог в файл!");
            System.out.println("📋 Содержимое лога:");
            System.out.println("------------------------");
            System.out.println(log.toString());
            System.out.println("------------------------");
            System.err.println("Техническая деталь ошибки:");
            e.printStackTrace();
        }
    }
}
