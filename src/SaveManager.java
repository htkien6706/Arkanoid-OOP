import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE = "ark_save.dat";

    // Lưu game
    public static void saveGame(GameSave save) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(save);
            System.out.println("Game saved!");
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    // Tải game
    public static GameSave loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof GameSave) {
                System.out.println("Game loaded!");
                return (GameSave) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No save file found or corrupted.");
        }
        return null;
    }

    // Kiểm tra có save không
    public static boolean hasSave() {
        return new File(SAVE_FILE).exists();
    }

    // Xóa save
    public static void deleteSave() {
        new File(SAVE_FILE).delete();
    }
}