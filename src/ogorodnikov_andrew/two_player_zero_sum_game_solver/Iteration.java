package ogorodnikov_andrew.two_player_zero_sum_game_solver;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Класс итерации
 */
public class Iteration {

    /*
    * PUBLIC
    * */

    /**
     * Конструктор итерации.
     * @param iterationNumber Номер итерации. Необходим для подсчета цены игры.
     * @param payoffMatrix Платежная матрица.
     * @param firstPersonStrategyIndex Номер следующей стратегии первого игрока. Нужен для следующей итерации.
     * @param firstPlayerGains Массив выигрышей первого игрока.
     * @param secondPlayerLosses Массив проигрышей второго игрока.
     */
    Iteration(int iterationNumber, Matrix payoffMatrix, int firstPersonStrategyIndex, double[] firstPlayerGains,  double[] secondPlayerLosses) {
        this.iterationNumber = iterationNumber;
        this.payoffMatrix = payoffMatrix;
        this.firstPlayerStrategyIndex = firstPersonStrategyIndex;
        this.firstPlayerGainsArray = firstPlayerGains;
        this.secondPlayerLossesArray = secondPlayerLosses;

        //Считаем выигрыши первого игрока на данной итерации
        for(int j=0;j<this.payoffMatrix.getColumnDimension();j++)
        {
            firstPlayerGainsArray[j]+=payoffMatrix.get(firstPlayerStrategyIndex,j);

        }
        double min=firstPlayerGainsArray[0];

        //Теперь определимся с выбором стратегии второго игрока
        // в соответствии с минимальным выигрышем первого
        int secondPlayerStrategyIndex=0;
        for(int j=1;j<payoffMatrix.getColumnDimension();j++)
        {
            if(min>firstPlayerGainsArray[j])
            {
                min=firstPlayerGainsArray[j];
                secondPlayerStrategyIndex=j;
            }
        }

        this.secondPlayerStrategyIndex = secondPlayerStrategyIndex;

        //Подсчитаем проигрыши второго игрока в соответствии с выбранной стратегией
        for(int i=0;i<payoffMatrix.getRowDimension();i++)
        {
            secondPlayerLossesArray[i]+=payoffMatrix.get(i, secondPlayerStrategyIndex);

        }

        //Подсчитаем индекс следующей стратегии первого игрока
        //в соответствии с максимальным проигрышем второго игрока
        // Это сделано для того,
        // чтобы не обращаться к данной итерации при подсчете номера стратегии первого игрока в следующей итерации
        double max=secondPlayerLossesArray[0];
        int maxLossIndex=0;
        maxLossIndex = 0;
        for(int i=1;i<payoffMatrix.getRowDimension();i++)
        {
            if(max<secondPlayerLossesArray[i])
            {
                max=secondPlayerLossesArray[i];
                maxLossIndex=i;
            }
        }

        nextIndex = maxLossIndex;

        gameValue =(max+min) / 2 / this.iterationNumber;

    }

    /**
     * Получить номер выбранной стратегии первого игрока для данной итерации.
     * @return Номер выбранной стратегии первого игрока для данной итерации.
     */
    public int getFirstPlayerStrategyIndex() {
        return firstPlayerStrategyIndex;
    }

    /**
     * Получить номер выбранной стратегии второго игрока для данной итерации.
     * @return Номер выбранной стратегии второго игрока для данной итерации.
     */
    public int getSecondPlayerStrategyIndex() {
        return secondPlayerStrategyIndex;
    }

    /**
     * Получить цену игры.
     * @return Цена игры.
     */
    public double getGameValue() {
        return gameValue;
    }

    /**
     * Получить массив выигрышей первого игрока.
     * @return Массив выигрышей первого игрока.
     */
    public double[] getFirstPlayerGainsArray() {
        return firstPlayerGainsArray;
    }

    /**
     * Получить массив проигрышей второго игрока.
     * @return Массив проигрышей второго игрока.
     */
    public double[] getSecondPlayerLossesArray() {
        return secondPlayerLossesArray;
    }

    /**
     * Получить номер итерации.
     * @return Номер итерации.
     */
    public int getIterationNumber() {
        return iterationNumber;
    }

    /**
     * Получить следующую итерацию.
     * @return Следующая итерация.
     */
    public Iteration nextIteration() {
        //return next (numberOfIterations+1) iteration
        return new Iteration(iterationNumber + 1, payoffMatrix, nextIndex, firstPlayerGainsArray.clone(),  secondPlayerLossesArray.clone());
    }

    /**
     * Преобразовать итерацию в строку с числовым форматом #.00.
     * @return Итерация в виде строки с числовым форматом #.00.
     */
    @Override
    public String toString() {
        return toString("#.00");
    }

    /**
     * Преобразовать итерацию в строку с указанным числовым форматом.
     * @param decimalFormatString Формат вывода чисел.
     * @return Итерация в виде строки с указанным числовым форматом.
     */
    public String toString(String decimalFormatString) {
        StringBuilder result = new StringBuilder("");
        DecimalFormat decimalFormat = new DecimalFormat(decimalFormatString);
        result.append("A").append(getFirstPlayerStrategyIndex()).append(" ");
        for(int i=0;i< firstPlayerGainsArray.length;i++)
        {
            result.append(decimalFormat.format(firstPlayerGainsArray[i])).append(" ");
        }
        result.append("B").append(getSecondPlayerStrategyIndex()).append(" ");
        for(int i=0;i< secondPlayerLossesArray.length;i++)
        {
            result.append(decimalFormat.format(secondPlayerLossesArray[i])).append(" ");
        }
        result.append(decimalFormat.format(getGameValue()));

        return result.toString();
    }

    /*
    * PRIVATE
    * */

    /**
     * Номер итерации.
     */
    private final int iterationNumber;

    /**
     * Платежная матрица.
     */
    private final Matrix payoffMatrix;
    /**
     * Массив выигрышей первого игрока.
     */
    private final double[] firstPlayerGainsArray;

    /**
     * Массив проигрышей второго игрока.
     */
    private final double[] secondPlayerLossesArray;

    /**
     * Цена игры.
     */
    private final double gameValue;

    /**
     * Номер следующей стратегии первого игрока.(Необходимо для следующей итерации)
     */
    private final int nextIndex;

    /**
     * Номер выбранной стратегии первого игрока на данной итерации.
     */
    private final int firstPlayerStrategyIndex;

    /**
     * Номер выбранной стратегии второго игрока на данной итерации.
     */
    private final int secondPlayerStrategyIndex;
}

