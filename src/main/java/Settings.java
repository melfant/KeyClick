import javafx.scene.input.KeyCode;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;


public class Settings implements Serializable{
    private volatile static Settings settings;

    private static final KeyCode defaultKeyCode = KeyCode.SPACE;
    private static final Integer defaultTimeInSeconds = 5;
    private static final Integer defaultPeriodsNumber = 3;

    private KeyCode keyCode;
    private Integer timeInSeconds;
    private Integer periodsNumber;

    private final PropertyChangeSupport timeInSecondsChangeSupport;
    private final PropertyChangeSupport keyCodeChangeSupport;
    private final PropertyChangeSupport periodsNumberChangeSupport;

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

    public void setPeriodsNumber(Integer periodsNumber){
        Integer oldPeriodsCount = this.periodsNumber;
        this.periodsNumber = periodsNumber;
        periodsNumberChangeSupport.firePropertyChange("periodsNumber", oldPeriodsCount, periodsNumber);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        timeInSecondsChangeSupport.addPropertyChangeListener(listener);
        keyCodeChangeSupport.addPropertyChangeListener(listener);
        periodsNumberChangeSupport.addPropertyChangeListener(listener);
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }
    public Integer getTimeInSeconds() {
        return timeInSeconds;
    }
    public Integer getPeriodsNumber(){
        return  periodsNumber;
    }

    private Settings(){
        this.keyCode = defaultKeyCode;
        this.timeInSeconds = defaultTimeInSeconds;
        this.periodsNumber = defaultPeriodsNumber;

        this.timeInSecondsChangeSupport = new PropertyChangeSupport(this);
        this.keyCodeChangeSupport = new PropertyChangeSupport(this);
        this.periodsNumberChangeSupport= new PropertyChangeSupport(this);
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
