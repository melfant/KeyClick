import javafx.scene.input.KeyCode;


public class Settings {
    private KeyCode keyCode;
    private Integer timeInSeconds;

    public KeyCode getKeyCode() {
        return keyCode;
    }
    public Integer getTimeInSeconds() {
        return timeInSeconds;
    }

    public Settings(KeyCode keyCode, Integer timeInSeconds){
        this.keyCode = keyCode;
        this.timeInSeconds = timeInSeconds;
    }
}
