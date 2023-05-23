package com.tictactoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Field {
    private static final int FIELD_SIZE = 9;
    private final Map<Integer, Sign> field;
    private final List<List<Integer>> winPossibilities;
    private final Difficulty difficulty;
    private final Sign playerSign;
    private final Sign opponentSign;

    public Field(Sign playerSign) {
        field = new HashMap<>();
        for (int i = 0; i < FIELD_SIZE; i++) {
            field.put(i, Sign.EMPTY);
        }
        this.winPossibilities = initializeWinPossibilities();
        this.difficulty = Difficulty.HARD;
        this.playerSign = playerSign;
        this.opponentSign = (playerSign == Sign.NOUGHT) ? Sign.CROSS : Sign.NOUGHT;
    }

    public enum Difficulty {
        EASY,
        NORMAL,
        HARD
    }

    public Map<Integer, Sign> getField() {
        return field;
    }

    public int getEmptyFieldIndex() {
        return field.entrySet().stream()
                .filter(e -> e.getValue() == Sign.EMPTY)
                .map(Map.Entry::getKey)
                .findFirst().orElse(-1);
    }

    public List<Sign> getFieldData() {
        return field.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public Sign checkWin() {
        return checkWin(field);
    }

    private Sign checkWin(Map<Integer, Sign> field) {
        for (List<Integer> winPossibility : winPossibilities) {
            int index1 = winPossibility.get(0);
            int index2 = winPossibility.get(1);
            int index3 = winPossibility.get(2);

            if (field.get(index1) == field.get(index2) && field.get(index1) == field.get(index3)) {
                return field.get(index1);
            }
        }
        return Sign.EMPTY;
    }

    public void makeMove() {
        List<Integer> occupiedCells = getOccupiedCells();

        if (occupiedCells.size() > 1) {

            // 1
            int winningMove = findWinningMove(playerSign);
            if (winningMove != -1) {
                field.put(winningMove, playerSign);
                return;
            }

            // 2
            int blockingMove = findWinningMove(opponentSign);
            if (blockingMove != -1) {
                field.put(blockingMove, playerSign);
                return;
            }

            // 4
            if (difficulty == Difficulty.HARD) {
                int opponentBlockingMove = findPotentialWinningMove(opponentSign);
                if (opponentBlockingMove != -1) {
                    field.put(opponentBlockingMove, playerSign);
                    return;
                }
            }

            // 3
            int potentialWinningMove = findPotentialWinningMove(playerSign);
            if (potentialWinningMove != -1) {
                field.put(potentialWinningMove, playerSign);
                return;
            }

        } else {
            int[] priorityCells = {4, 0, 2, 6, 8};
            if (field.get(4) == Sign.EMPTY) {
                field.put(4, playerSign);
            } else {
                List<Integer> emptyCells = getEmptyCells(priorityCells);
                if (!emptyCells.isEmpty()) {
                    int randomIndex = (int) (Math.random() * emptyCells.size());
                    int randomCell = emptyCells.get(randomIndex);
                    field.put(randomCell, playerSign);
                }
            }
        }
    }

    private List<Integer> getOccupiedCells() {
        return field.entrySet().stream()
                .filter(e -> e.getValue() != Sign.EMPTY)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private int findWinningMove(Sign playerSign) {
        Map<Integer, Sign> fieldCopy = new HashMap<>(field);
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (fieldCopy.get(i) == Sign.EMPTY) {
                fieldCopy.put(i, playerSign);
                if (checkWin(fieldCopy) == playerSign) {
                    return i;
                }
                fieldCopy.remove(i);
            }
        }
        return -1;
    }

    private int findPotentialWinningMove(Sign playerSign) {
        Map<Integer, Sign> fieldCopy = new HashMap<>(field);
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (fieldCopy.get(i) == Sign.EMPTY) {
                fieldCopy.put(i, playerSign);
                if (checkPotentialWin(fieldCopy, playerSign)) {
                    return i;
                }
                fieldCopy.remove(i);
            }
        }
        return -1;
    }

    private List<Integer> getEmptyCells(int[] priorityCells) {
        List<Integer> emptyCells = new ArrayList<>();
        for (int cell : priorityCells) {
            if (field.get(cell) == Sign.EMPTY) {
                emptyCells.add(cell);
            }
        }
        return emptyCells;
    }

    private int findWinningMove(Map<Integer, Sign> fieldCopy, Sign playerSign) {
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (fieldCopy.get(i) == Sign.EMPTY) {
                fieldCopy.put(i, playerSign);
                if (checkWin(fieldCopy) == playerSign) {
                    return i;
                }
                fieldCopy.remove(i);
            }
        }
        return -1;
    }

    private int findBlockingMove(Map<Integer, Sign> fieldCopy, Sign playerSign) {
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (fieldCopy.get(i) == Sign.EMPTY) {
                fieldCopy.put(i, playerSign);
                if (checkPotentialWin(fieldCopy, playerSign)) {
                    return i;
                }
                fieldCopy.remove(i);
            }
        }
        return -1;
    }

    private boolean checkPotentialWin(Map<Integer, Sign> fieldCopy, Sign playerSign) {
        for (List<Integer> winPossibility : winPossibilities) {
            int index1 = winPossibility.get(0);
            int index2 = winPossibility.get(1);
            int index3 = winPossibility.get(2);

            if (fieldCopy.get(index1) == playerSign && fieldCopy.get(index2) == playerSign && fieldCopy.get(index3) == Sign.EMPTY) {
                return true;
            }
            if (fieldCopy.get(index1) == playerSign && fieldCopy.get(index2) == Sign.EMPTY && fieldCopy.get(index3) == playerSign) {
                return true;
            }
            if (fieldCopy.get(index1) == Sign.EMPTY && fieldCopy.get(index2) == playerSign && fieldCopy.get(index3) == playerSign) {
                return true;
            }
        }
        return false;
    }

    private List<List<Integer>> initializeWinPossibilities() {
        List<List<Integer>> winPossibilities = new ArrayList<>();
        winPossibilities.add(List.of(0, 1, 2));
        winPossibilities.add(List.of(3, 4, 5));
        winPossibilities.add(List.of(6, 7, 8));
        winPossibilities.add(List.of(0, 3, 6));
        winPossibilities.add(List.of(1, 4, 7));
        winPossibilities.add(List.of(2, 5, 8));
        winPossibilities.add(List.of(0, 4, 8));
        winPossibilities.add(List.of(2, 4, 6));
        return winPossibilities;
    }
}