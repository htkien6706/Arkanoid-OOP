import java.io.*;
import java.util.*;

public class ScoreManager {
    private static final String SCORE_FILE = "highscores.dat";
    private List<ScoreEntry> highScores;

    public ScoreManager() {
        highScores = new ArrayList<>();
        loadScores();
    }

    // Thêm điểm mới
    public void addScore(String playerName, int score) {
        highScores.add(new ScoreEntry(playerName, score));

        // Loại bỏ trùng lặp - chỉ giữ điểm cao nhất của mỗi tên
        Map<String, ScoreEntry> uniqueMap = new LinkedHashMap<>();
        for (ScoreEntry entry : highScores) {
            String name = entry.getPlayerName();
            if (!uniqueMap.containsKey(name) || entry.getScore() > uniqueMap.get(name).getScore()) {
                uniqueMap.put(name, entry);
            }
        }

        // Tạo lại danh sách từ map
        highScores.clear();
        for (ScoreEntry entry : uniqueMap.values()) {
            highScores.add(entry);
        }

        // Sắp xếp giảm dần theo điểm
        Collections.sort(highScores, new Comparator<ScoreEntry>() {
            public int compare(ScoreEntry a, ScoreEntry b) {
                return Integer.compare(b.getScore(), a.getScore());
            }
        });

        // Chỉ giữ 10 điểm cao nhất
        if (highScores.size() > 10) {
            List<ScoreEntry> top10 = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                top10.add(highScores.get(i));
            }
            highScores = top10;
        }

        saveScores();
    }

    // Lấy danh sách điểm cao
    public List<ScoreEntry> getHighScores() {
        return new ArrayList<>(highScores);
    }

    // Kiểm tra có phải high score không
    public boolean isHighScore(int score) {
        if (highScores.size() < 10) {
            return true;
        }
        int lowestScore = highScores.get(highScores.size() - 1).getScore();
        return score > lowestScore;
    }

    // Lưu điểm vào file
    private void saveScores() {
        try {
            FileOutputStream fileOut = new FileOutputStream(SCORE_FILE);  // tạo file để ghi
            ObjectOutputStream out = new ObjectOutputStream(fileOut);  // tạo đối tượng ghi dữ liệu vào luồng
            out.writeObject(highScores);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Không thể lưu điểm: " + e.getMessage());
        }
    }

    // Đọc điểm từ file
    private void loadScores() {
        try {
            FileInputStream fileIn = new FileInputStream(SCORE_FILE);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            highScores = (List<ScoreEntry>) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception e) {
            System.out.println("Không thể đọc điểm: " + e.getMessage());
        }
    }

    // Xóa tất cả điểm
    public void clearScores() {
        highScores.clear();
    }
}

// Class lưu thông tin 1 entry điểm
class ScoreEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private String playerName;
    private int score;
    private long timestamp;

    public ScoreEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public long getTimestamp() {
        return timestamp;
    }
}