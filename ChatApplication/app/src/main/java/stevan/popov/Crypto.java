package stevan.popov;


public class Crypto {
    public native String crypt(String message);

    static {
        System.loadLibrary("crypt");
    }
}