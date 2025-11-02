public enum PowerUpType {
    EXPAND_PADDLE("Expand", "Paddle dài hơn", 0),
    SHRINK_PADDLE("Shrink", "Paddle ngắn lại", 1),
    SLOW_BALL("Slow", "Bóng chậm lại", 2),
    FAST_BALL("Fast", "Bóng nhanh hơn", 3),
    MULTI_BALL("Multi", "Thêm 2 bóng", 4),
    LASER("Laser", "Bắn laser", 5),
    EXTRA_LIFE("Life", "+1 mạng", 6),
    BONUS_POINTS("Bonus", "+500 điểm", 7);

    private final String name;
    private final String description;
    private final int colorIndex;

    PowerUpType(String name, String description, int colorIndex) {
        this.name = name;
        this.description = description;
        this.colorIndex = colorIndex;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getColorIndex() { return colorIndex; }
}