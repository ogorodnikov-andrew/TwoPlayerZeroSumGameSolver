package ogorodnikov_andrew.two_player_zero_sum_game_solver;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Класс результата игры.
 */
public final class Result {

    /*
    * PUBLIC
    * */

    /**
     * Функция для получения массива вероятностей использования стратегий первого игрока.
     * @return Массив вероятностей использования стратегий первого игрока.
     */
    public ArrayList<Double> getFirstPlayerResultArray(){
        return firstPlayerResultArray;
    }

    /**
     * Функция для получения массива вероятностей использования стратегий второго игрока.
     * @return Массив вероятностей использования стратегий второго игрока.
     */
    public ArrayList<Double> getSecondPlayerResultArray() {
        return secondPlayerResultArray;
    }

    /**
     * Получить цену игры.
     * @return Цена игры.
     */
    public double getGameValue() {
        return gameValue;
    }

    /**
     * Преобразовать результат в строку с указанным числовым форматом.
     * @param decimalFormatString Формат вывода чисел.
     * @return Результат в виде строки с указанным числовым форматом.
     */
    public String toString(String decimalFormatString) {
        StringBuilder result = new StringBuilder("");
        DecimalFormat decimalFormat = new DecimalFormat(decimalFormatString);
        for(int i=0;i< firstPlayerResultArray.size();i++)
        {
            result.append("p").append(i).append("=").append(decimalFormat.format(firstPlayerResultArray.get(i))).append("; ");
        }
        result.append("\n");
        for(int i=0;i< secondPlayerResultArray.size();i++)
        {
            result.append("q").append(i).append("=").append(decimalFormat.format(secondPlayerResultArray.get(i))).append("; ");
        }
        result.append("\n").append(decimalFormat.format(getGameValue()));
        return result.toString();
    }

    /**
     * Преобразовать результат в строку с  числовым форматом #.00.
     * @return Результат в виде строки с числовым форматом #.00.
     */
    @Override
    public String toString(){
        return toString("#.00");
    }

    /*
    * PACKAGE PRIVATE
    * */

    /**
     * Конструктор.
     * @param firstPlayerResultArray Массив вероятностей использования стратегий первого игрока.
     * @param secondPlayerResultArray Массив вероятностей использования стратегий первого игрока.
     * @param gameValue Цена игры.
     */
     Result(ArrayList<Double> firstPlayerResultArray, ArrayList<Double> secondPlayerResultArray, double gameValue) {
        this.firstPlayerResultArray = firstPlayerResultArray;
        this.secondPlayerResultArray = secondPlayerResultArray;
        this.gameValue = gameValue;
    }

    /**
     * Конструктор результата игры по седловой точке.
     * @param saddle_point  Седловая точка.
     * @param number_of_rows Количество строк.
     * @param number_of_columns Количество столбцов.
     */
    Result(SaddlePoint saddle_point, int number_of_rows, int number_of_columns) {

        firstPlayerResultArray = new ArrayList<Double>();
        for(int i=0;i<number_of_rows;i++)
        {
            if(i==(int)saddle_point.getX()) firstPlayerResultArray.add(1d); else firstPlayerResultArray.add(0d);
        }
        secondPlayerResultArray = new ArrayList<Double>();
        for(int j=0;j<number_of_columns;j++)
        {
            if(j==(int)saddle_point.getY()) secondPlayerResultArray.add(1d); else secondPlayerResultArray.add(0d);
        }
        gameValue = saddle_point.getValue();
    }

    /*
    * PRIVATE
     */

    /**
     * Массив вероятностей использования стратегий первого игрока.
     */
    private final ArrayList<Double> firstPlayerResultArray;

    /**
     * Массив вероятностей использования стратегий первого игрока.
     */
    private final ArrayList<Double> secondPlayerResultArray;

    /**
     * Цена игры.
     */
    private final double gameValue;
}
