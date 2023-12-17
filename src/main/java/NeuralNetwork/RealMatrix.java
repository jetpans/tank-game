package NeuralNetwork;

public class RealMatrix
{
double[][] matrix;

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public RealMatrix(double[][] matrix) {
        this.matrix=new double[matrix.length][matrix[0].length];
        for(int i=0;i< matrix.length;i++)
        {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, matrix[i].length);
        }
    }

    public RealMatrix copy()
    {
        return (new RealMatrix(this.matrix));
    }

    public double getEntry(int i, int j)
    {
        return this.matrix[i][j];
    }

    public void setEntry(int i, int j, double value)
    {
        this.matrix[i][j]=value;
    }

    public int getRowDimension()
    {
        return this.matrix.length;
    }

    public int getColumnDimension()
    {
        return  this.matrix[0].length;
    }
    public double[] getColumn(int column) {
        int nRows = this.getRowDimension();
        double[] out = new double[nRows];

        for(int i = 0; i < nRows; ++i) {
            out[i] = this.getEntry(i, column);
        }

        return out;
    }

    public RealMatrix add(RealMatrix mat2)
    {
        RealMatrix sum=new RealMatrix(mat2.getMatrix());
        for(int i=0;i< this.matrix.length;i++)
        {
            for(int j=0;j<this.matrix[i].length;j++)
            {
                sum.setEntry(i,j,this.matrix[i][j]+sum.getEntry(i,j));
            }
        }
        return sum;
    }

    public RealMatrix scalarMultiply(double v)
    {
        RealMatrix pi=new RealMatrix(this.matrix);
        for(int i=0;i< this.matrix.length;i++)
        {
            for(int j=0;j<this.matrix[i].length;j++)
            {
                pi.setEntry(i,j,v*pi.getEntry(i,j));
            }
        }
        return pi;
    }

    public RealMatrix multiply(RealMatrix m)
    {
        int nRows = this.getRowDimension();
        int nCols = m.getColumnDimension();
        int nSum = this.getColumnDimension();
        double[][] outData = new double[nRows][nCols];
        double[] mCol = new double[nSum];
        double[][] mData = m.matrix;

        for(int col = 0; col < nCols; ++col) {
            int row;
            for(row = 0; row < nSum; ++row) {
                mCol[row] = mData[row][col];
            }

            for(row = 0; row < nRows; ++row) {
                double[] dataRow = this.matrix[row];
                double sum = 0.0;

                for(int i = 0; i < nSum; ++i) {
                    sum += dataRow[i] * mCol[i];
                }

                outData[row][col] = sum;
            }
        }
        return new RealMatrix(outData);
    }
}
