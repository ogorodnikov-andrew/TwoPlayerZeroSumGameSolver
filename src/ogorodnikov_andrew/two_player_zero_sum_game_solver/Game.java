package ogorodnikov_andrew.two_player_zero_sum_game_solver;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by Andrew Ogorodnikov
 * Date: 08.02.14 19:42
 */
public class Game {

    /**
     * Конструктор игры по указанной платежной матрице.
     * @param matrix Платежная матрица.
     */
    public Game(Matrix matrix) {
        this.initialPayoffMatrix = matrix;
        this.payoffMatrix = matrix;
    }

    /**
     * Получить результат игры итеративным методом.
     * @param numberOfIterations Количество итераций.
     * @return Результат игры.
     */
    public Result getIterationMethodResult(int numberOfIterations) {

        this.numberOfIterations = numberOfIterations;
        iterations = new Iteration[numberOfIterations];

        // Получить седловую точку
        SaddlePoint sp=getSaddlePoint();

        //Если у матрицы есть отрицательные элемента, добавим ко всем элементам модуль наименьшего.
        addValue();

        if(sp!=null)//if a saddle point found
        {
            //get the result according to the saddle point
            return new Result(sp, payoffMatrix.getRowDimension(), payoffMatrix.getColumnDimension());
        }
        else // if there is no saddle point
        {
            //search for domination
            int min = Math.min(payoffMatrix.getRowDimension(), payoffMatrix.getRowDimension());
            for(int i = 0; i < min-1; i++)
            {
                searchRowDomination();
                searchColumnDomination();
            }

            double[] firstPersonGains = new double[payoffMatrix.getColumnDimension()];
            double[] secondPersonLosses = new double[payoffMatrix.getRowDimension()];
            //get the start index (index of max of min gains)
            int startIndex = (int) getLowerBound().getX();

            //obtain the 1st iteration
            iterations[0] = new Iteration(1,payoffMatrix, startIndex,firstPersonGains,secondPersonLosses);
            //obtain the rest of the iterations
            for(int i=0;i< numberOfIterations -1;i++)
            {
                iterations[i+1]=iterations[i].nextIteration();
            }

            //create vectors for results
            ArrayList<Double> p = new ArrayList<Double>();
            ArrayList<Double> q = new ArrayList<Double>();
            for(int i=0;i<payoffMatrix.getRowDimension();i++)
            {
                p.add(0d);
            }
            for(int j=0;j<payoffMatrix.getColumnDimension();j++)
            {
                q.add(0d);
            }

            //calculate how many times players chose each choice
            for(int k=0;k< numberOfIterations;k++)
            {
                //first player
                for(int i=0;i<payoffMatrix.getRowDimension();i++)
                {    if(iterations[k].getFirstPlayerStrategyIndex()==i)
                {
                    //p[i]++
                    p.set(i,Double.parseDouble(p.get(i).toString())+1);
                }
                }

                //second player
                for(int j=0;j<payoffMatrix.getColumnDimension();j++)
                {
                    if(iterations[k].getSecondPlayerStrategyIndex()==j)
                    {
                        //q[j]++
                        q.set(j,Double.parseDouble(q.get(j).toString())+1);
                    }
                }
            }

            //get the probability of each choice by dividing it by the number of iterations
            //first player
            for(int i=0;i<payoffMatrix.getRowDimension();i++)
            {
                //p[i]/=numberOfIterations;
                p.set(i,Double.parseDouble(p.get(i).toString())/ numberOfIterations);

            }
            //second plauer
            for(int j=0;j<payoffMatrix.getColumnDimension();j++)
            {
                //q[j]/=numberOfIterations;
                q.set(j,Double.parseDouble(q.get(j).toString())/ numberOfIterations);
            }

            //if some rows were deleted insert a 0 probability at their indexes
            for(int index : removedRowIndexes)
            {
                p.add(index,0d);
            }
            //if some columns were deleted insert a 0 probability at their indexes
            for(int index : removedColumnIndexes)
            {
                q.add(index,0d);
            }
            //construct a result object and return it
            return new Result(p,q,iterations[numberOfIterations -1].getGameValue());

        }
    }

    /**
     * Получить TreeSet с удаленными строками.
     * @return TreeSet с удаленными строками.
     */
    public TreeSet<Integer> getRemovedRowIndexes() {
        return removedRowIndexes;
    }

    /**
     * Получить TreeSet с удаленными столбцами.
     * @return TreeSet с удаленными столбцами.
     */
    public TreeSet<Integer> getRemovedColumnIndexes() {
        return removedColumnIndexes;
    }

    /**
     * Получить указанную итерацию.
     * @param iterationNumber Номер итерации.
     * @return Итерация по указанному номеру.
     */
    public Iteration getIteration(int iterationNumber) {
        if(iterationNumber>=numberOfIterations || iterationNumber<0)
        {
            throw new IndexOutOfBoundsException("Iteration number must be a positive number less than the number of iterations");
        }
        return iterations[iterationNumber];
    }

    /**
     * Получить количество итераций.
     * @return Количество итераций.
     */
    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    /**
     * Получить прибавленное число.
     * @return Прибоавленное число.
     */
    public double getAddedValue() {
        return addedValue;
    }

    /**
     * Добавить значение к платежной матрицы. Это делается для того, чтобы избежать счета отрицательных чисел.
     * В конце данное число вычитается из цены игры.
     */
    private void addValue() {
        double min = payoffMatrix.get(0,0);
        for(int i=0;i<payoffMatrix.getRowDimension();i++){
            for(int j=0;j<payoffMatrix.getColumnDimension();j++) {
                if(payoffMatrix.get(i,j)<min) {
                    min = payoffMatrix.get(i,j);
                }
            }
        }
        if(min<0) {
            payoffMatrix.addConstValue(Math.abs(min));
            addedValue =  Math.abs(min);
        }
        addedValue = 0;
    }

    /**
     * Поиск доминирования среди строк.
     */
    private void searchRowDomination() {
        ArrayList<Boolean> isRowRemoved = new ArrayList<Boolean>();
        for(int i=0;i< payoffMatrix.getRowDimension();i++)
        {
            isRowRemoved.add(false);
            for(int k=0;k< payoffMatrix.getRowDimension();k++)
            {
                if(isRowRemoved.get(i))
                {
                    break;
                }
                for(int j=0;j< payoffMatrix.getColumnDimension();j++)
                {
                    if(i==k)
                    {
                        break;
                    }
                    if(payoffMatrix.get(i,j)> payoffMatrix.get(k,j))
                    {
                        isRowRemoved.set(i,false);
                        break;
                    }
                    else
                    {
                        if(payoffMatrix.get(i,j)< payoffMatrix.get(k,j))
                        {
                            isRowRemoved.set(i,true);
                        }
                    }
                }
            }
        }
        for(int i=0;i<payoffMatrix.getRowDimension();i++)
        {
            if(isRowRemoved.get(i))
            {
                if(removedRowIndexes.isEmpty())
                {
                    removedRowIndexes.add(i);
                }
                else
                {
                    int real_index = i;
                    for(int index : removedRowIndexes)
                    {
                        if(index <= real_index)
                        {
                            real_index++;
                        }
                    }
                    removedRowIndexes.add(real_index);
                }
                payoffMatrix.removeRow(i);
                isRowRemoved.remove(i);
                i--;
            }
        }
    }

    /**
     * Поиск доминирования среди столбцов.
     */
    private void searchColumnDomination() {
        ArrayList<Boolean> isColumnRemoved = new ArrayList<Boolean>();
        for(int j=0;j< payoffMatrix.getColumnDimension();j++)
        {
            isColumnRemoved.add(false);
            for(int k=0;k< payoffMatrix.getColumnDimension();k++)
            {
                if(isColumnRemoved.get(j))
                {
                    break;
                }
                for(int i=0;i< payoffMatrix.getRowDimension();i++)
                {
                    if(j==k)
                    {
                        break;
                    }
                    if(payoffMatrix.get(i,j)< payoffMatrix.get(i,k))
                    {
                        isColumnRemoved.set(j,false);
                        break;
                    }
                    else
                    {
                        if(payoffMatrix.get(i,j)> payoffMatrix.get(i,k))
                        {
                            isColumnRemoved.set(j,true);
                        }
                    }
                }
            }
        }
        for(int j=0;j<payoffMatrix.getColumnDimension();j++)
        {
            if(isColumnRemoved.get(j))
            {
                if(removedColumnIndexes.isEmpty())
                {
                    removedColumnIndexes.add(j);
                }
                else
                {
                    int real_index = j;
                    for(int index : removedColumnIndexes)
                    {
                        if(index <= real_index)
                        {
                            real_index++;
                        }
                    }
                    removedColumnIndexes.add(real_index);
                }
                payoffMatrix.removeColumn(j);
                isColumnRemoved.remove(j);
                j--;
            }
        }
    }

    /**
     * Получить седловую точку, если она есть.
     * @return Седловая точка. Если седловой точки нет, то null.
     */
    public SaddlePoint getSaddlePoint() {
        //get game value bounds

        //lower bound
        //x-coordinate is a max_a index
        //y-coordinate is a max_a_value
        Point a = getLowerBound();

        //upper bound
        //x-coordinate is a min_b index
        //y-coordinate is a min_b value
        Point b = getUpperBound();

        if(a.getY()==b.getY())//if lower bound == upper bound (max_a value == min_b value)
        {
            //there is a saddle point. return it
            // a.getX is an x-coordinate(row-coordinate) of a saddle point
            // b.getX is an y-coordinate(column-coordinate) of a saddle point
            //a.getY == b.getY is saddle point's value
            return new SaddlePoint((int)a.getX(),(int)b.getX(),a.getY());
        }
        else//if lower bound != upper bound
        {
            // no saddle point
            return null;
        }
    }

    /**
     * Получить нижнюю границу цены игры.
     * @return Нижняя граница цены игры.
     */
    public Point getLowerBound() {
        //lower bound value
        double max_a;
        //lower bound index
        int max_a_index;

        //an array for first player's minimum gains
        double[] a = new double[payoffMatrix.getRowDimension()];

        //set first player's minimum gain to first element in a row
        for(int i=0;i< payoffMatrix.getRowDimension();i++)
        {
            a[i]= payoffMatrix.get(i,0);
        }
        //find minimum gains in each row
        for(int i=0;i< payoffMatrix.getRowDimension();i++)
        {
            for(int j=1;j< payoffMatrix.getColumnDimension();j++)
            {
                if(payoffMatrix.get(i,j)<a[i])
                {
                    //replace if current element is less than the min element
                    a[i]= payoffMatrix.get(i,j);
                }
            }
        }
        max_a=a[0];
        max_a_index=0;
        //find the maximum element of an array of minimum gains
        for(int i=1;i< payoffMatrix.getRowDimension();i++)
        {
            if(a[i]>max_a)
            {
                max_a=a[i];
                max_a_index=i;
            }
        }
        Point A = new Point(max_a_index,max_a);
        //x-coordinate is an integer index
        //y-coordinate is a double value
        return A;
    }

    /**
     * Получить верхнюю границу цены игры.
     * @return Верхняя граница цены игры.
     */
    public Point getUpperBound() {
        //algorithm is similar to getLowerBound()
        double min_b;
        int min_b_index;

        double b[] = new double[payoffMatrix.getColumnDimension()];

        for(int j=0;j< payoffMatrix.getColumnDimension();j++)
        {
            b[j]= payoffMatrix.get(0,j);
        }

        for(int j=0;j< payoffMatrix.getColumnDimension();j++)
        {
            for(int i=1;i< payoffMatrix.getRowDimension();i++)
            {
                if(payoffMatrix.get(i,j)>b[j])
                {
                    b[j]= payoffMatrix.get(i,j);
                }
            }
        }
        min_b=b[0];
        min_b_index=0;
        for(int j=1;j< payoffMatrix.getColumnDimension();j++)
        {
            if(b[j]<min_b)
            {
                min_b=b[j];
                min_b_index=j;
            }
        }
        Point B = new Point(min_b_index,min_b);
        return B;
    }

    /**
     * Получить исходную матрицу.
     * @return Исходная матрица.
     */
    public Matrix getInitialPayoffMatrix() {
        return initialPayoffMatrix;
    }

    /*
    * PRIVATE
    * */

    /**
     * Платежная матрица.
     */
    private final Matrix payoffMatrix;

    /**
     * Исходная платежная матрица. Без учета удаленных строк и столбцов в ходе поиска доминирования.
     */
    private final Matrix initialPayoffMatrix;

    /**
     * TreeSet в котором хранятся номера удаленных строк.
     */
    private final TreeSet<Integer> removedRowIndexes = new TreeSet<Integer>();

    /**
     * TreeSet в котором хранятся номера удаленных столбцов.
     */
    private final TreeSet<Integer> removedColumnIndexes = new TreeSet<Integer>();

    /**
     * Добавляемая к платежной матрице величина. Это делается для того, чтобы избежать счета отрицательных чисел.
     * В конце данное число вычитается из цены игры.
     */
    private double addedValue=0;

    /**
     * Количество итераций.
     */
    private int numberOfIterations;

    /**
     * Массив итераций.
     */
    private Iteration[] iterations;
}
