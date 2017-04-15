package com.company;


import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String FILE_PATH = "D://input.txt"; //Enter path here
    private static final String TARGET_STRING = "OneTwoTrip";

    public static void main(String[] args) {
        Map<Character, CharacterInfo> charsToFind = parseTargetString(TARGET_STRING);
        Map<Character, CharacterInfo> result = new HashMap<>(charsToFind);
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine(); //нас не интересует размерность матрицы
            int currentRow = 0;
            int currentCol = 0;
            String nextLine;
            while ((nextLine = reader.readLine()) != null && charsToFind.size() > 0) { //до тех пор пока не кончится матрица, либо не найдем все нужные символы
                for (Character character : nextLine.toCharArray()) {
                    Character charInLowerCase = Character.toLowerCase(character);
                    if (charsToFind.containsKey(charInLowerCase)) {
                        CharacterInfo info = charsToFind.get(charInLowerCase);
                        info.addPoint(character, new MatrixPoint(currentRow, currentCol));
                        if (info.getPoints().size() >= info.getCountToFind()) {
                            charsToFind.remove(charInLowerCase);
                        }
                    }
                    currentCol++;
                }
                currentRow++;
                currentCol = 0;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (charsToFind.size() == 0) {
            for (Character character : TARGET_STRING.toLowerCase().toCharArray()) {
                CharacterInfo info = result.get(Character.toLowerCase(character));
                Pair<Character, MatrixPoint> pair = info.getPoints().get(0);
                info.removePoint(0);
                System.out.println(pair.getKey() + " - (" + pair.getValue().row + ", " + pair.getValue().col + ");");
            }
        } else {
            System.out.println("Impossible");
        }
    }

    private static Map<Character, CharacterInfo> parseTargetString(String targetString) {
        Map<Character, CharacterInfo> resultMap = new HashMap<>();
        for (Character ch : targetString.toLowerCase().toCharArray()) {
            if (resultMap.containsKey(ch)) {
                CharacterInfo info = resultMap.get(ch);
                info.setCountToFind(info.getCountToFind() + 1);
            } else {
                CharacterInfo info = new CharacterInfo(1);
                resultMap.put(ch, info);
            }
        }

        return resultMap;
    }

    private static class CharacterInfo {
        private int countToFind;
        private List<Pair<Character, MatrixPoint>> points;

        CharacterInfo(int count) {
            this.countToFind = count;
            points = new ArrayList<>(count);
        }

        int getCountToFind() {
            return countToFind;
        }

        void setCountToFind(int countToFind) {
            this.countToFind = countToFind;
        }

        List<Pair<Character, MatrixPoint>> getPoints() {
            return points;
        }

        void addPoint(Character character, MatrixPoint point) {
            points.add(new Pair<>(character, point));
        }

        void removePoint(int index) {
            points.remove(index);
        }
    }

    private static class MatrixPoint {
        final int row;
        final int col;

        MatrixPoint(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}
