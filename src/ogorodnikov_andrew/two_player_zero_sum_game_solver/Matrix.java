package ogorodnikov_andrew.two_player_zero_sum_game_solver;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Класс матрицы.
 */
public class Matrix {

    /*
    * PUBLIC
    * */

    /**
     * Конструктор матрицы по двумерному массиву.
     * @param source_array Двумерный массив.
     */
    public Matrix(double[][] source_array)
    {
        elements = new ArrayList<ArrayList<Double>>();
        int rowDimension = source_array.length;
        int columnDimension = source_array[0].length;
        for (int i = 0; i < rowDimension; i++)
        {
            if (source_array[i].length != columnDimension) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
            elements.add(new ArrayList<Double>(columnDimension));
            for(int j=0; j < columnDimension; j++)
            {
                elements.get(i).add(source_array[i][j]);
            }
        }
    }

    /**
     * Конструктор матрицы по одномерному массиву и количеству строк.
     * @param one_dimensional_source_array Одномерный массив.
     * @param rowDimension Количество строк.
     */
    public Matrix(double[] one_dimensional_source_array, int rowDimension)
    {
        elements = new ArrayList<ArrayList<Double>>();
        int columnDimension = (rowDimension != 0 ? one_dimensional_source_array.length/rowDimension : 0);
        if (rowDimension*columnDimension != one_dimensional_source_array.length) {
            throw new IllegalArgumentException("Array length must be a multiple of m.");
        }
        for (int i = 0; i < rowDimension; i++)
        {
            elements.add(new ArrayList<Double>(columnDimension));
            for (int j = 0; j < columnDimension; j++) {
                elements.get(i).add(one_dimensional_source_array[j+i*columnDimension]);
            }
        }
    }

    /** Фабричный метод для генерации матрицы указанного размера со случайными элементами в диапазоне от -10 до 10.
     @param rowDimension Количество строк.
     @param columnDimension Количество столбцов.
     @return Матрица размером rowDimension*columnDimension со случайными элементами в диапзоне от -10 до 10
     */
    public static Matrix random (int rowDimension, int columnDimension) {
        double[][] X = new double[rowDimension][columnDimension];
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++)
            {
                X[i][j] = Math.random()*20-10;
            }
        }
        return new Matrix(X);
    }

    /** Фабричный метод для генерации матрицы заданных размеров с элементами в заданных границах.
     @param rowDimension Количество строк.
     @param columnDimension Количество столбцов.
     @param elementLowerBound Нижняя граница элементов.
     @param elementUpperBound Верхняя граница элементов.
     @exception IllegalArgumentException Верхняя граница должна быть больше или равной нижней границы.
     @return Матрица размером rowDimension*columnDimension с элементами в заданных границах.
     */
    public static Matrix randomSpecifiedElementRange (int rowDimension, int columnDimension, int elementLowerBound, int elementUpperBound) {
        if (elementUpperBound<=elementLowerBound)
        {
            throw new IllegalArgumentException("Upper bound must be greater than lower bound");
        }
        double[][] X = new double[rowDimension][columnDimension];
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++)
            {
                X[i][j] = Math.random()*(elementUpperBound-elementLowerBound)+elementLowerBound;
            }
        }
        return new Matrix(X);
    }

    /**
     * Добавить к элементам матрицы число.
     * @param value Число для добавления.
     */
    public void addConstValue(double value){
        for(int i=0;i<elements.size();i++) {
            for(int j=0;j<elements.get(i).size();j++){
                elements.get(i).set(j, elements.get(i).get(j)+value);
            }
        }

    }

    /**
     * Преобразовать матрицу в строку с указанным числовым форматом.
     * @param decimalFormatString Формат вывода чисел.
     * @return Матрица в виде строки с указанным числовым форматом.
     */
    public String toString(String decimalFormatString)
    {
        DecimalFormat decimalFormat = new DecimalFormat(decimalFormatString);
        StringBuilder result = new StringBuilder("");
        for(ArrayList<Double> row : elements)
        {
            for(double element : row)
            {
                result.append(decimalFormat.format(element)).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Получить элемент матрицы по указанным координатам.
     * @param rowPosition   Номер строки.
     * @param columnPosition Номер стролбца.
     * @return  Элемент матрицы по указанным координатам.
     */
    public double get(int rowPosition,int columnPosition)
    {
        return elements.get(rowPosition).get(columnPosition);
    }

    /**
     * Преобразовать матрицу в строку с числовым форматом #.00.
     * @return Матрица в виде строки с числовым форматом #.00.
     */
    @Override
    public String toString() {
        return toString("#0.0");
    }

    /**
     * Удалить указанный ряд из матрицы.
     * @param rowNumber Номер ряда.
     */
    public void removeRow(int rowNumber)
    {
        elements.remove(rowNumber);
    }

    /**
     * Удалить указанный столбец из матрицы.
     * @param columnNumber Номер столбца.
     */
    public void removeColumn(int columnNumber)
    {
        for(ArrayList<Double> rows : elements)
        {
            rows.remove(columnNumber);
        }
    }

    /**
     * Получить количество строк матрицы.
     * @return Количество строк матрицы.
     */
    public int getRowDimension()
    {
        return elements.size();
    }

    /**
     * Получить количество столбцов матрицы.
     * @return Количество столбцов матрицы.
     */
    public int getColumnDimension()
    {
        if(!elements.isEmpty())
        {
            return elements.get(0).size();
        }
        else
        {
            throw new IndexOutOfBoundsException("Empty array.");
        }
    }

    /*
    * PRIVATE
    * */

    /**
     * Элементы матрицы.
     */
    private ArrayList<ArrayList<Double>> elements;
}
