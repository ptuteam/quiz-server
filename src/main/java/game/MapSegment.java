package game;

/**
 * alex on 25.01.16.
 */
public class MapSegment {

    @SuppressWarnings("InstanceVariableNamingConvention")
    private final int id;
    private final int value;
    private String user;
    private final int[] neighborSegments;

    public MapSegment(int id, int value, String user, int[] neighborSegments) {
        this.id = id;
        this.value = value;
        this.user = user;
        this.neighborSegments = neighborSegments;
    }

    public MapSegment(int id, int value, int[] neighborSegments) {
        this(id, value, null, neighborSegments);
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public int[] getNeighborSegments() {
        return neighborSegments;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
