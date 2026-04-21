package nro.models.consignment;

import nro.models.item.Item;
import lombok.Getter;
import lombok.Setter;

/**
 * @author DucSunIT
 */
@Setter
@Getter
public class ConsignmentItem extends Item {
    private int consignID;
    private long consignorID;
    private long priceGold;
    private long priceGem;
    private byte tab;
    private boolean sold;
    private boolean upTop;
    private long timeConsign;
    private short itemExchangeID; // ID của vật phẩm dùng để giao dịch
    private int exchangeQuantity; // Số lượng vật phẩm dùng để giao dịch

}
