package designpattern.single;

public class SoulSinger {

    private final static SoulSinger INSTANCE = new SoulSinger();

    private SoulSinger() {
    }

    public static SoulSinger getInstance(){
        return INSTANCE;
    }

    public void sing(){
        System.out.println("singing out");
    }
}
