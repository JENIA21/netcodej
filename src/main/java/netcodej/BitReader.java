package netcodej;

import java.util.ArrayList;
import java.util.List;

public class BitReader {
    private List<Byte> bytes = new ArrayList<>();
    private int point = 0;

    public BitReader(Object[] data) {
        for (var item : data)
            bytes.add((Byte) item);
    }

    public int readBits(int count) {
        int re = 0;
        while (count > 0) {
            int mask = 1 << (7 - (point % 8));
            re <<= 1;
            re += (bytes.get(point / 8) & mask) == 0 ? 0 : 1;
            count -= 1;
            point += 1;
        }
        return re;
    }

    public float readFloat(float min, float max, float precision) {
        return readFloat(new LimitFloat(min, max, precision));
    }

    public float readFloat(LimitFloat limit) {
        int integerValue = readBits(limit.BitCount);
        float normalizedValue = integerValue / (float) limit.MaxIntegerValue;

        return normalizedValue * limit.Delta + limit.min;
    }

    public int readInt(int min, int max) {
        return readInt(new LimitInt(min, max));
    }

    public int readInt(LimitInt limit) {
        int integerValue = readBits(limit.BitCount);
        return integerValue + limit.min;
    }
    public Result<Float> readFloatIfChanged(float min, float max, float precision) {
        return readFloatIfChanged(new LimitFloat(min, max, precision));
    }
    public Result<Float> readFloatIfChanged(LimitFloat limit) {
        int flag = readBits(1);
        if(flag == 1)
            return Result.ok(readFloat(limit));
        return Result.fail("Значение не изменилось");
    }
    public Result<Integer> readIntIfChanged(int min, int max) {
        return readIntIfChanged(new LimitInt(min, max));
    }
    public Result<Integer> readIntIfChanged(LimitInt limit) {
        int flag = readBits(1);
        if(flag == 1)
            return Result.ok(readInt(limit));
        return Result.fail("Значение не изменилось");
    }
}
