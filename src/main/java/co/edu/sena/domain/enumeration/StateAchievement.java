package co.edu.sena.domain.enumeration;

/**
 * The StateAchievement enumeration.
 */
public enum StateAchievement {
    Active("fortaleza"),
    Inactive("debilidad");

    private final String value;

    StateAchievement(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
