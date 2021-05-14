package IonEngine;




public class IonFeatures {
    public void setTimeout(long time, IonCallback callback) {
        try {
            Thread.sleep(time);
        }
        catch(Exception e) {}
        finally {
            callback.call();
        }
    }
}