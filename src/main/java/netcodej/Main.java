package netcodej;

public class Main {
    public static void main(String[] args) {
        int a = 1;
        int b = 1;
        int c = 2;
        var bw = new BitWriter();
        bw.writeValueIfChanged(a, b, new LimitInt(0, 8));
        bw.writeValueIfChanged(a, c, new LimitInt(0, 8));
        bw.flush();
        var br = new BitReader(bw.getBytes());
        var ab = br.readIntIfChanged(0, 8);
        var ac = br.readIntIfChanged(0, 8);
        System.out.println(ab.isError);
        System.out.println(ac.isError);
        System.out.println(ac.value);
    }
}