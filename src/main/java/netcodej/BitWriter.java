package netcodej;

import java.util.ArrayList;
import java.util.List;

public class BitWriter {
    private final int defaultCapacity = 1500;
    private int capacity;
    private byte[] bytes;
    private byte buffer = 0;
    private final float defaultPrecision = 0.000001f;
    private long bitsCount = 0;
    private int bytePoint = 0;
    public BitWriter(){
        bytes = new byte[defaultCapacity];
        capacity = defaultCapacity;
    }
    public BitWriter(int capacity){
        bytes = new byte[capacity];
        this.capacity = capacity;
    }
    private void writeBits(int bitCount, int value) {
        int mask = (1 << bitCount) - 1;
        value &= mask;
        while (bitCount > 0) {
            long count = Math.min(8 - bitsCount % 8, bitCount);
            int submask = ((1 << count) - 1) << (bitCount - count);
            int numbers = value & submask;
            if (8 - bitsCount % 8 - bitCount >= 0)
                numbers <<= 8 - bitsCount % 8 - bitCount;
            else
                numbers >>= -(8 - bitsCount % 8 - bitCount);
            buffer += numbers;
            bitCount -= count;
            bitsCount += count;
            if (bitsCount % 8 == 0) {
                bytes[bytePoint++] = buffer;
                buffer = 0;
            }
            if(bytePoint == capacity)
                doubleCapacity();
        }
    }

    public void write(float value, float min, float max, float precision) {
        write(value, new LimitFloat(min, max, precision));
    }

    public void write(float value, LimitFloat limit) {
        float normalizedValue = clamp((value - limit.min) / limit.Delta, 0, 1);
        int integerValue = (int) Math.floor(normalizedValue * limit.MaxIntegerValue + 0.5f);
        writeBits(limit.BitCount, integerValue);
    }

    public void writeValueIfChanged(float before, float after, LimitFloat limit) {
        var diff = after - before;
        if (Math.abs(diff) >= limit.Precision) {
            writeBits(1, 1);
            write(after, limit);
        }
        else
            writeBits(1, 0);
    }

    public void writeDiffIfChanged(float before, float after, LimitFloat limit, LimitFloat diffLimit) {
        var diff = after - before;
        if (Math.abs(diff) >= limit.Precision) {
            if (diffLimit.min < diff && diff < diffLimit.max) {
                write(diff, diffLimit);
            } else {
                write(after, limit);
            }
        }
    }

    public void write(int value, int min, int max) {
        write(value, new LimitInt(min, max));
    }

    public void write(int value, LimitInt limit) {
        value -= limit.min;
        writeBits(limit.BitCount, value);
    }

    public void writeValueIfChanged(int before, int after, LimitInt limit) {
        if (before != after) {
            writeBits(1, 1);
            write(after, limit);
        }
        else
            writeBits(1, 0);
    }

    public void writeDiffIfChanged(int before, int after, LimitInt limit, LimitInt diffLimit) {
        var diff = after - before;
        if (Math.abs(diff) > 0) {
            if (diffLimit.min < diff && diff < diffLimit.max) {
                write(diff, diffLimit);
            } else {
                write(after, limit);
            }
        }
    }

    public void flush() {
        if(bitsCount % 8 != 0){
            bytes[bytePoint++] = buffer;
            bitsCount = bytePoint * 8L;
            buffer = 0;
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    public long getBitsCount() {
        return bitsCount;
    }

    private static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        }
        return Math.min(value, max);
    }
    private void doubleCapacity(){
        capacity *= 2;
        byte[] newArray = new byte[capacity];
        System.arraycopy(bytes, 0, newArray, 0, bytes.length);
        bytes = newArray;
    }
}