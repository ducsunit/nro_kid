package nro.models.task;

/**
 *
 * @author DucSunIT
 * @copyright 💖 GirlkuN 💖
 *
 */
public class SideTaskTemplate {

    public int id;
    public String name;
    public int[][] count;

    public SideTaskTemplate() {
        this.count = new int[5][2];
    }

}
