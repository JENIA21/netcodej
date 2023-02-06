package netcodej;

public class LimitInt {
    public int min;
    public int max;
    public int Delta;
    public int BitCount;

    public LimitInt(int min, int max) {

        this.min = min;
        this.max = max;
        Delta = max - min;
        BitCount = bitsRequired(Delta);
    }

    private static int bitsRequired(int range) {
        return range == 0 ? 1 : (int) (Math.log(range) / Math.log(2)) + 1;
    }
}
