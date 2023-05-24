package com.tictactoe;

import java.util.*;
import java.util.stream.Collectors;

public class Field {
    private static final int FIELD_SIZE = 9;
    private final Map<Integer, Sign> field;
    private final List<List<Integer>> winPossibilities;
    private final Difficulty difficulty;
    private final Sign AISign;
    private final Sign playerSign;

    public Field(Sign playerSign, Difficulty difficulty) {
        field = new HashMap<>();
        for (int i = 0; i < FIELD_SIZE; i++) {
            field.put(i, Sign.EMPTY);
        }
        this.winPossibilities = initializeWinPossibilities();
        this.difficulty = difficulty;
        this.playerSign = playerSign;
        this.AISign = (playerSign == Sign.NOUGHT) ? Sign.CROSS : Sign.NOUGHT;
    }

//    public enum Difficulty {
//        EASY,
//        NORMAL,
//        HARD
//    }

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
        List<Integer> emptyCells = getEmptyCells();

        if (emptyCells.size() == FIELD_SIZE) {
            makeFirstMove();
            return;
        }

        if (emptyCells.size() < FIELD_SIZE - 1) {
            int winningMove = findWinningMove(AISign);
            if (winningMove != -1) {
                field.put(winningMove, AISign);
                return;
            }

            if (difficulty == Difficulty.MEDIUM || difficulty == Difficulty.HARD) {
                int blockingMove = findWinningMove(playerSign);
                if (blockingMove != -1) {
                    field.put(blockingMove, AISign);
                    return;
                }
            }

            if (difficulty == Difficulty.HARD) {
                int opponentBlockingMove = findPotentialWinningMove(playerSign);
                if (opponentBlockingMove != -1) {
                    field.put(opponentBlockingMove, AISign);
                    return;
                }
            }

            int potentialWinningMove = findPotentialWinningMove(AISign);
            if (potentialWinningMove != -1) {
                field.put(potentialWinningMove, AISign);
                return;
            }

            makeRandomMove(emptyCells);
        } else {
            makePriorityMove();
        }
    }

    private void makeFirstMove() {
        if (difficulty == Difficulty.EASY) {
            makeRandomMove(getEmptyCells());
        } else if (difficulty == Difficulty.MEDIUM) {
            int[] priorityCells = {4, 0, 2, 6, 8};
            makeRandomMove(getEmptyCells(priorityCells));
        } else if (difficulty == Difficulty.HARD) {
            field.put(4, AISign);
        }
    }

    private void makeRandomMove(List<Integer> emptyCells) {
        if (!emptyCells.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(emptyCells.size());
            int randomCell = emptyCells.get(randomIndex);
            field.put(randomCell, AISign);
        }
    }

    private void makePriorityMove() {
        int[] priorityCells = {4, 0, 2, 6, 8};
        if (field.get(4) == Sign.EMPTY) {
            field.put(4, AISign);
        } else {
            List<Integer> cornerCells = getEmptyCells(priorityCells);
            if (!cornerCells.isEmpty()) {
                int randomIndex = (int) (Math.random() * cornerCells.size());
                int randomCell = cornerCells.get(randomIndex);
                field.put(randomCell, AISign);
            }
        }
    }

    private List<Integer> getEmptyCells() {
        return field.entrySet().stream()
                .filter(e -> e.getValue() == Sign.EMPTY)
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