import javafx.scene.input.KeyCode;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;


public class Settings implements Serializable{
    private volatile static Settings settings;

    private static final KeyCode defaultKeyCode = KeyCode.SPACE;
    private static final Integer defaultTimeInSeconds = 30;

    private KeyCode keyCode;
    private Integer timeInSeconds;

    private final PropertyChangeSupport timeInSecondsChangeSupport;
    private final PropertyChangeSupport keyCodeChangeSupport;

    public void setKeyCode(KeyCode keyCode) {
        KeyCode oldKeyCode = this.keyCode;
        this.keyCode = keyCode;
        keyCodeChangeSupport.firePropertyChange("keyCode", oldKeyCode, keyCode);
    }

    public void setTimeInSeconds(Integer timeInSeconds) {
        Integer oldTimeInSeconds = this.timeInSeconds;
        this.timeInSeconds = timeInSeconds;
        timeInSecondsChangeSupport.firePropertyChange("timeInSeconds", oldTimeInSeconds, timeInSeconds);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        timeInSecondsChangeSupport.addPropertyChangeListener(listener);
        keyCodeChangeSupport.addPropertyChangeListener(listener);
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

        this.timeInSecondsChangeSupport = new PropertyChangeSupport(this);
        this.keyCodeChangeSupport = new PropertyChangeSupport(this);
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
