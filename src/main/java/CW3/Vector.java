package CW3;

public class Vector {
    public double[] data;

    public Vector(double[] data) {
        this.data = data.clone();
    }

    public void print() {
        for (double d : data) System.out.print(d + " ");
        System.out.println();
    }

    public boolean isZero() {
        for (double d : data) if (d != 0) return false;
        return true;
    }

    public Vector makeCopy() {
        return new Vector(this.data);
    }

    public Vector times(double scalar) {
        for (int i = 0; i < data.length; i++) {
            this.data[i] *= scalar;
        }
        return this;
    }

    public Vector divideBy(double scalar) {
        for (int i = 0; i < data.length; i++) {
            this.data[i] /= scalar;
        }
        return this;
    }

    public Vector add(Vector that) {
        if (this.data.length != that.data.length) {
            System.err.println("Vector size does not match.");
        } else {
            for (int i = 0; i < this.data.length; i++) {
                this.data[i] += that.data[i];
            }
        }
        return this;
    }

    public Vector subtract(Vector that) {
        if (this.data.length != that.data.length) {
            System.err.println("Vector size does not match.");
        } else {
            for (int i = 0; i < this.data.length; i++) {
                this.data[i] -= that.data[i];
            }
        }
        return this;
    }
}