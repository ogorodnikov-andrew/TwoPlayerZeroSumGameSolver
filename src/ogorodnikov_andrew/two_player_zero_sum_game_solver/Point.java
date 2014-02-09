package ogorodnikov_andrew.two_player_zero_sum_game_solver;


public class Point {

    /*
    * PUBLIC
    * */

    /**
     * Конструктор по умолчанию. Создает точку (0,0).
     */
    public Point()
    {
        this.x=0;
        this.y=0;
    }

    /**
     * Конструктор (x,y) точки.
     * @param x x-координата.
     * @param y y-координата.
     */
    public Point(double x,double y)
    {
        this.x=x;
        this.y=y;
    }

    /**
     * Получить x-координату.
     * @return x-координата.
     */
    public double getX()
    {
        return x;
    }

    /**
     * Получить y-координату.
     * @return y-координата.
     */
    public double getY()
    {
        return y;
    }

    /*
    * PRIVATE
    * */

     /**
     * x координата.
     */
    private final double x;
    /**
     * y координата.
     */
    private final double y;
}
