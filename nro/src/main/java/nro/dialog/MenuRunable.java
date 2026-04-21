package nro.dialog;

import lombok.Getter;
import lombok.Setter;

/**
 * @author DucSunIT
 */
@Setter
@Getter
public abstract class MenuRunable implements Runnable {
    private int indexSelected;
}
