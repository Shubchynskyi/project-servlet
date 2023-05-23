package com.tictactoe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Field {
    private final Map<Integer, Sign> field;

    public Field() {
        field = new HashMap<>();
        field.put(0, Sign.EMPTY);
        field.put(1, Sign.EMPTY);
        field.put(2, Sign.EMPTY);
        field.put(3, Sign.EMPTY);
        field.put(4, Sign.EMPTY);
        field.put(5, Sign.EMPTY);
        field.put(6, Sign.EMPTY);
        field.put(7, Sign.EMPTY);
        field.put(8, Sign.EMPTY);
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
        List<List<Integer>> winPossibilities = List.of(
                List.of(0, 1, 2),
                List.of(3, 4, 5),
                List.of(6, 7, 8),
                List.of(0, 3, 6),
                List.of(1, 4, 7),
                List.of(2, 5, 8),
                List.of(0, 4, 8),
                List.of(2, 4, 6)
        );

        for (List<Integer> winPossibility : winPossibilities) {
            if (field.get(winPossibility.get(0)) == field.get(winPossibility.get(1))
                && field.get(winPossibility.get(0)) == field.get(winPossibility.get(2))) {
                return field.get(winPossibility.get(0));
            }
        }
        return Sign.EMPTY;
    }

    public Sign checkWin(Map<Integer, Sign> field) {
        List<List<Integer>> winPossibilities = List.of(
                List.of(0, 1, 2),
                List.of(3, 4, 5),
                List.of(6, 7, 8),
                List.of(0, 3, 6),
                List.of(1, 4, 7),
                List.of(2, 5, 8),
                List.of(0, 4, 8),
                List.of(2, 4, 6)
        );

        for (List<Integer> winPossibility : winPossibilities) {
            if (field.get(winPossibility.get(0)) == field.get(winPossibility.get(1))
                && field.get(winPossibility.get(0)) == field.get(winPossibility.get(2))) {
                return field.get(winPossibility.get(0));
            }
        }
        return Sign.EMPTY;
    }

    public void makeMove() {
        Sign playerSign = Sign.NOUGHT; // TODO передавать в конструктор (нужно для игры разными знаками)
        Map<Integer, Sign> fieldCopy = new HashMap<>(field);

        // 1) Проверка на возможность выигрышного хода
        for (int i = 0; i < 9; i++) {
            if (fieldCopy.get(i) == Sign.EMPTY) {
                fieldCopy.put(i, playerSign);
                if (checkWin(fieldCopy) == playerSign) {
                    field.put(i, playerSign);
                    return; // Выполнение выигрышного хода
                }
                fieldCopy.put(i, Sign.EMPTY);
            }
        }

        // 2) Проверка на возможность блокировки выигрышного хода соперника
        Sign opponentSign = Sign.CROSS;
        for (int i = 0; i < 9; i++) {
            if (fieldCopy.get(i) == Sign.EMPTY) {
                fieldCopy.put(i, opponentSign);
                if (checkWin(fieldCopy) == opponentSign) {
                    field.put(i, playerSign);
                    return; // Выполнение блокирующего хода
                }
                fieldCopy.put(i, Sign.EMPTY);
            }
        }

        // 3) Поиск потенциально победного хода
        for (int i = 0; i < 9; i++) {
            if (field.get(i) == Sign.EMPTY) {
                fieldCopy.put(i, playerSign);
                if (checkPotentialWin(fieldCopy, playerSign)) {
                    field.put(i, playerSign);
                    return; // Выполнение потенциально победного хода
                }
                fieldCopy.put(i, Sign.EMPTY);
            }
        }

        // 4) Проверка на возможность поставить знак в среднюю ячейку
        int middleIndex = 4;
        if (field.get(middleIndex) == Sign.EMPTY) {
            field.put(middleIndex, playerSign);
            return;
        }

        // 5) Поставить знак в рандомную клетку
        Random random = new Random();
        int randomIndex;
        do {
            randomIndex = random.nextInt(9);
        } while (field.get(randomIndex) != Sign.EMPTY);
        field.put(randomIndex, playerSign);
    }

    private boolean checkPotentialWin(Map<Integer, Sign> fieldCopy, Sign playerSign) {
        List<List<Integer>> winPossibilities = List.of(
                List.of(0, 1, 2),
                List.of(3, 4, 5),
                List.of(6, 7, 8),
                List.of(0, 3, 6),
                List.of(1, 4, 7),
                List.of(2, 5, 8),
                List.of(0, 4, 8),
                List.of(2, 4, 6)
        );

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
}