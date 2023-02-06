package netcodej;

public class LimitFloat {
    public float min;
    public float max;
    public float Precision;
    public float Delta;
    public int MaxIntegerValue;
    public int BitCount;

    public LimitFloat(float min, float max, float precision) {

        this.min = min;
        this.max = max;
        Precision = precision;
        Delta = max - min;
        float values = Delta / precision;
        MaxIntegerValue = (int) Math.ceil(values);
        BitCount = bitsRequired(MaxIntegerValue);
    }

    private static int bitsRequired(int range) {
        return range == 0 ? 1 : (int) (Math.log(range) / Math.log(2)) + 1;
    }
}
