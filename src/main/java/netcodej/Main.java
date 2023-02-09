package netcodej;

public class Main {
    public static void main(String[] args) {
        var bw = new BitWriter();
        bw.write(5, 0, 32);
        bw.write(4, 0, 32);
        System.out.println(bw.getBitsCount());
        bw.flush();
        System.out.println(bw.getBitsCount());
        var br = new BitReader(bw.getBytes());
        System.out.println(br.readInt(0, 32));
        System.out.println(br.readInt(0, 32));
    }
}