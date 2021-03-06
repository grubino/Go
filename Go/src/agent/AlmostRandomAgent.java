package agent;

import game.Board;
import game.Group;
import game.Point;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by grubino on 4/28/15.
 */
public class AlmostRandomAgent implements IAgent {

    private static Random rand;
    static {
        rand = new Random();
    }

    @Override
    public Point getNextMove(final Board board) {
        List<Point> possibleMoves = board.getValidMoves();
        while(possibleMoves.size() != 0) {
            int i = rand.nextInt(possibleMoves.size());
            final Point p = possibleMoves.get(i);
            possibleMoves.remove(i);
            if(board.getGroups().stream().filter(g -> isInternal(p, g)).findAny().isPresent()) {
                continue;
            } else {
                return p;
            }
        }
        return new Point(-1, -1);
    }

    public boolean isInternal(final Point p, final Group group) {

        Optional<Point> northPoint = group.getPoints().stream().filter(gp -> gp.getX() == p.getX() && gp.getY() < p.getY()).findAny();
        Optional<Point> eastPoint = group.getPoints().stream().filter(gp -> gp.getY() == p.getY() && gp.getX() > p.getX()).findAny();
        Optional<Point> westPoint = group.getPoints().stream().filter(gp -> gp.getY() == p.getY() && gp.getX() < p.getX()).findAny();
        Optional<Point> southPoint = group.getPoints().stream().filter(gp -> gp.getX() == p.getX() && gp.getY() > p.getY()).findAny();

        List<Point> enclosedSides =
                Arrays.asList(northPoint, eastPoint, westPoint, southPoint).stream()
                        .filter(op -> op.isPresent())
                        .map(op -> op.get())
                        .collect(Collectors.toList());
        if(enclosedSides.size() == 0) {
            return false;
        } else if(enclosedSides.size() == 1) {
            if(enclosedSides.get(0).getX() != p.getX()) {
                Point refPoint = eastPoint.isPresent() ? eastPoint.get() : westPoint.get();
                return group.getPoints().stream().filter(gp -> gp.getY() == 0).findAny().isPresent() &&
                        group.getPoints().stream().filter(gp -> gp.getY() == 18).findAny().isPresent() &&
                        Math.abs(p.getX() - refPoint.getX()) < 5;
            } else {
                Point refPoint = northPoint.isPresent() ? northPoint.get() : southPoint.get();
                return group.getPoints().stream().filter(gp -> gp.getX() == 0).findAny().isPresent() &&
                        group.getPoints().stream().filter(gp -> gp.getX() == 18).findAny().isPresent() &&
                        Math.abs(p.getY() - refPoint.getY()) < 5;
            }
        } else if (enclosedSides.size() == 2) {
            Optional<Point> northOrSouth = enclosedSides.stream().filter(rp -> rp.getX() != p.getX()).findAny();
            Optional<Point> eastOrWest = enclosedSides.stream().filter(rp -> rp.getY() != p.getY()).findAny();
            if(northOrSouth.isPresent() && eastOrWest.isPresent()) {

                boolean hEnclosed = false;
                if(northPoint.isPresent()) {
                    hEnclosed = group.getPoints().stream().filter(gp -> gp.getX() == 18).findAny().isPresent();
                } else {
                    hEnclosed = group.getPoints().stream().filter(gp -> gp.getY() == 0).findAny().isPresent();
                }

                boolean vEnclosed = false;
                if(eastPoint.isPresent()) {
                    vEnclosed = group.getPoints().stream().filter(gp -> gp.getX() == 0).findAny().isPresent();
                } else {
                    vEnclosed = group.getPoints().stream().filter(gp -> gp.getX() == 18).findAny().isPresent();
                }

                return hEnclosed && vEnclosed;

            } else {
                return false;
            }
        } else if (enclosedSides.size() == 3) {
            return true;
        } else {
            return true;
        }
    }
}
