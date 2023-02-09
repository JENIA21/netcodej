package netcodej;

public class Main {
    public static void main(String[] args) {
        var bw = new BitWriter(2);
        bw.write(3, 0, 128);
        bw.write(3, 0, 128);
        bw.write(3, 0, 128);
        System.out.println(bw.getBitsCount());
        var br = new BitReader(bw.getBytes());
        System.out.println(br.readInt(0, 128));
        System.out.println(br.readInt(0, 128));
        System.out.println(br.readInt(0, 128));
    }
}