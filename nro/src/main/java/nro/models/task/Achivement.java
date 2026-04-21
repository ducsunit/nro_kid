package nro.models.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nro.consts.ConstAchive;

/**
 * @author DucSunIT
 */

@Setter
@Getter
@NoArgsConstructor
public class Achivement {

    private int id;

    private String name;

    private String detail;

    private int money;


    public int count;

    private int maxCount;

    private boolean isFinish;

    private boolean isReceive;

    public boolean isDone() {
        return this.count >= this.maxCount;
    }

    public boolean isDone(int divisor) {
        return this.count / divisor >= this.maxCount / divisor;
    }

    public int getCount() {
        if (this.id == ConstAchive.GIA_NHAP_THAN_CAP || this.id == ConstAchive.SUC_MANH_GIOI_VUONG_THAN) {
            return this.count / 1000000;
        } else if (this.id == ConstAchive.HOAT_DONG_CHAM_CHI) {
            return count / 60;
        }
        return this.count;
    }

    public int getMaxCount() {
        if (this.id == ConstAchive.GIA_NHAP_THAN_CAP || this.id == ConstAchive.SUC_MANH_GIOI_VUONG_THAN) {
            return this.maxCount / 1000000;
        } else if (this.id == ConstAchive.HOAT_DONG_CHAM_CHI) {
            return maxCount / 60;
        }
        return this.maxCount;
    }
}
