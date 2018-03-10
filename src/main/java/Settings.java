import javafx.scene.input.KeyCode;


public class Settings {
    private volatile static Settings settings;

    private static final KeyCode defaultKeyCode = KeyCode.SPACE;
    private static final Integer defaultTimeInSeconds = 30;

    private KeyCode keyCode;
    private Integer timeInSeconds;

    public void setKeyCode(KeyCode keyCode) {
        this.keyCode = keyCode;
    }

    public void setTimeInSeconds(Integer timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }
    public Integer getTimeInSeconds() {
        return timeInSeconds;
    }

    private Settings(){
        this.keyCode = defaultKeyCode;
        this.timeInSeconds = defaultTimeInSeconds;
    }

    public static Settings getInstance(){
        if (settings == null){
            synchronized (Settings.class){
                if(settings == null){
                    settings = new Settings();
                }
            }
        }
        return settings;
    }

}
