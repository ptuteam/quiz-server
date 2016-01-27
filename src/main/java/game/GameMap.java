package game;

import model.UserProfile;
import utils.ConfigGeneral;

import java.util.*;
import java.util.stream.Collectors;

/**
 * alex on 25.01.16.
 */
public class GameMap {

    private List<MapSegment> segmentList;

    public GameMap() {
        segmentList = ConfigGeneral.getMapSegmentList();
    }

    public List<MapSegment> getMapSegments() {
        return segmentList;
    }

    public int invadeSegment(UserProfile invader, int segmentId) {
        String user = invader.getEmail();
        for (MapSegment segment : segmentList) {
            if (segment.getId() == segmentId) {
                segment.setUser(user);
                return segment.getValue();
            }
        }
        return 0;
    }

    public Set<Integer> getAllowableMoveForUser(String user) {
        Set<Integer> allowableMoveSet = new HashSet<>();
        segmentList.stream()
                .filter(segment -> Objects.equals(segment.getUser(), user))
                .forEach(segment -> allowableMoveSet.addAll(
                        Arrays.stream(segment.getNeighborSegments()).boxed().collect(Collectors.toList())
                ));
        segmentList.stream()
                .filter(segment -> Objects.equals(segment.getUser(), user))
                .forEach(segment -> allowableMoveSet.remove(segment.getId()));

        return allowableMoveSet;
    }
}
