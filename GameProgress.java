import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GameProgress implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int health;
    private final int weapons;
    private final int lvl;
    private final double distance;

    public GameProgress(int health, int weapons, int lvl, double distance) {
        this.health = health;
        this.weapons = weapons;
        this.lvl = lvl;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "GameProgress{" +
                "health=" + health +
                ", weapons=" + weapons +
                ", lvl=" + lvl +
                ", distance=" + distance +
                '}';
    }

    public static void saveGame(String filePath, GameProgress progress) {
        try (FileOutputStream fos = new
                FileOutputStream(filePath);
             ObjectOutputStream oos = new
                     ObjectOutputStream(fos)) {
            oos.writeObject(progress);
            System.out.println("✓ Сохранен файл: " + filePath);
        } catch (IOException ex) {
            System.out.println("✗ Ошибка при сохранении файла: " + filePath);
            System.out.println("Причина: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void zipFiles(String zipFilePath, List<String> filesToZip) {

        try (ZipOutputStream zout = new ZipOutputStream(
                new FileOutputStream(zipFilePath))) {
            System.out.println("Начинаем создание архива: " + zipFilePath);

            for (String filePath : filesToZip) {
                File fileToZip = new File(filePath);
                if (!fileToZip.exists()) {
                    System.err.println("  ✗ Файл не найден, пропускаем: " + filePath);
                    continue;
                }
                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry entry = new ZipEntry(fileToZip.getName());
                    zout.putNextEntry(entry);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        zout.write(buffer, 0, bytesRead);
                    }
                    zout.closeEntry();
                    System.out.println("  ✓ Добавлен в архив: " + filePath);
                } catch (IOException e) {
                    System.err.println("  ✗ Ошибка при обработке файла: " + filePath);
                    System.err.println("    Причина: " + e.getMessage());

                }
            }
            System.out.println("✓ Архив успешно создан: " + zipFilePath);
        } catch (IOException e) {
            System.err.println("✗ Ошибка при создании архива: " + zipFilePath);
            e.printStackTrace();
        }
    }

    public static void deleteFiles(List<String> filesToDelete) {
        System.out.println("\nНачинаем удаление исходных файлов...");

        for (String filePath : filesToDelete) {
            File file = new File(filePath);

            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("  ✓ Удален файл: " + filePath);
                } else {
                    System.err.println("  ✗ Не удалось удалить файл: " + filePath);
                    System.err.println("    Возможно, файл открыт в другой программе или нет прав");
                }
            } else {
                System.out.println("  ℹ Файл не найден (возможно уже удален): " + filePath);

            }
        }
        System.out.println("Удаление завершено!");
    }

    public static void main(String[] args) {

        GameProgress progress1 =
                new GameProgress(100, 1, 1, 0.5);
        GameProgress progress2 =
                new GameProgress(85, 3, 3, 15.7);
        GameProgress progress3 =
                new GameProgress(45, 7, 8, 125.3);

        System.out.println("Созданы объекты:");
        System.out.println(progress1);
        System.out.println(progress2);
        System.out.println(progress3);

        String savesDir = "D:\\Games\\savegames\\";

        System.out.println("\nНачинаем сохранение:");
        saveGame(savesDir + "save1.dat", progress1);
        saveGame(savesDir + "save2.dat", progress2);
        saveGame(savesDir + "save3.dat", progress3);

        System.out.println("\nСохранение завершено!");

        List<String> filesToZip = new ArrayList<>();
        filesToZip.add("D:/Games/savegames/save1.dat");
        filesToZip.add("D:/Games/savegames/save2.dat");
        filesToZip.add("D:/Games/savegames/save3.dat");
        String zipPath = "D:/Games/savegames/saves.zip";
        GameProgress.zipFiles(zipPath, filesToZip);

        List<String> filesToDelete = new ArrayList<>();
        filesToDelete.add("D:/Games/savegames/save1.dat");
        filesToDelete.add("D:/Games/savegames/save2.dat");
        filesToDelete.add("D:/Games/savegames/save3.dat");

        deleteFiles(filesToDelete);
    }
}
