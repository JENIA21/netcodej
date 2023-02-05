package netcodej;

public class Main {
    public static void main(String[] args) {
        var bw = new BitWriter();
        bw.write(83.3f, 0f, 100f, 0.1f);
        System.out.println(bw.getBitsCount());
        bw.write(12, 1, 14);
        System.out.println(bw.getBitsCount());

        bw.flush();
        System.out.println(bw.getBitsCount());

        var a = bw.getBytes();
        var br = new BitReader(a);
        var b = br.readFloat(0f, 100f, 0.1f);
        var c = br.readInt(1, 14);
        System.out.println(b + " " + c);
    }
}