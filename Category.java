
public enum Category {
    BIKES,
    SUPERBIKES,
    XUVS,
    SEDAN,
    SUPERCARS,
    OTHER;

    public static Category fromString(String s) {
        try {
            return Category.valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
