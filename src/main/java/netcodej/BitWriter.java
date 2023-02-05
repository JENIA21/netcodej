package netcodej;

import java.util.ArrayList;
import java.util.List;

public class BitWriter {
    private List<Byte> bytes = new ArrayList<>();
    private byte buffer = 0;
    private float defaultPrecision = 0.000001f;
    private long bitsCount = 0;
    private void writeBits(int bitCount, int value){
        int mask = (1 << bitCount) - 1;
        value &= mask;
        while(bitCount > 0){
            long count = Math.min(8 - bitsCount % 8, bitCount);
            int submask = ((1 << count) - 1) << (bitCount - count);
            int numbers = value & submask;
            if(8 - bitsCount % 8 - bitCount >= 0)
                numbers <<= 8 - bitsCount % 8 - bitCount;
            else
                numbers >>= -(8 - bitsCount % 8 - bitCount);
            buffer += numbers;
            bitCount -= count;
            bitsCount += count;
            if(bitsCount % 8 == 0){
                bytes.add(buffer);
                buffer = 0;
            }
        }
    }
    public void write(float value, float min, float max, float precision){
        write(value, new LimitFloat(min, max, precision));
    }
    public void write(float value, LimitFloat limit){
        float normalizedValue = clamp((value - limit.min) / limit.Delta, 0, 1);
        int integerValue = (int)Math.floor(normalizedValue * limit.MaxIntegerValue + 0.5f);
        writeBits(limit.BitCount, integerValue);
    }
    public void writeValueIfChanged(float before, float after, LimitFloat limit) {
        if(before != after)
            write(after, limit);
    }
    public void writeDiffIfChanged(float before, float after, LimitFloat limit, LimitFloat diffLimit){
        var diff = after - before;
        if (Math.abs(diff) >= limit.Precision)
        {
            if (diffLimit.min < diff && diff < diffLimit.max)
            {
                write(diff, diffLimit);
            }
            else
            {
                write(after, limit);
            }
        }
    }
    public void write(int value, int min, int max){
        write(value, new LimitInt(min, max));
    }
    public void write(int value, LimitInt limit){
        value -= limit.min;
        writeBits(limit.BitCount, value);
    }
    public void writeValueIfChanged(int before, int after, LimitInt limit) {
        if(before != after)
            write(after, limit);
    }
    public void writeDiffIfChanged(int before, int after, LimitInt limit, LimitInt diffLimit){
        var diff = after - before;
        if (Math.abs(diff) > 0)
        {
            if (diffLimit.min < diff && diff < diffLimit.max)
            {
                write(diff, diffLimit);
            }
            else
            {
                write(after, limit);
            }
        }
    }
    public void flush(){
        bytes.add(buffer);
        bitsCount = bytes.stream().count() * 8;
        buffer = 0;
    }
    public Object[] getBytes(){
        return bytes.toArray();
    }
    public long getBitsCount(){
        return bitsCount;
    }
    private static float clamp(float value, float min, float max) {
        if (value < min)
        {
            return min;
        }
        return Math.min(value, max);
    }

}
