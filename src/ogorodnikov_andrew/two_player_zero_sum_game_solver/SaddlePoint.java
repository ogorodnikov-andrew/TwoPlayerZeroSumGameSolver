package ogorodnikov_andrew.two_player_zero_sum_game_solver;

/**
 * Класс седловой точки.
 */
public final class SaddlePoint extends Point {

    /*
    * PUBLIC
    * */

    /**
     * Конструктор по умолчанию.
     */
    public SaddlePoint(){
        super();
        value=0;
    }

    /**
     * Конструктор по координатам и значению.
     * @param row Строка.
     * @param column Столбец.
     * @param value Значение.
     */
    public SaddlePoint(int row, int column, double value) {
        super(row,column);
        this.value=value;
    }

    /**
     * Получить значение седловой точки.
     * @param value Значение седловой точки.
     */
    public void setValue(double value) {
        this.value=value;
    }

    /**
     * Задать значение седловой точки.
     * @return Значение седловой точки.
     */
    public double getValue() {
        return value;
    }

    /*
    * PRIVATE
    * */

     /**
     * Значение седловой точки.
     */
    private double value;






}
