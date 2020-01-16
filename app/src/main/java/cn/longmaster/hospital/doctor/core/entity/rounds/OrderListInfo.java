package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/5/16.
 */

public class OrderListInfo implements Serializable {
    @JsonField("data")
    private List<OrderListItemInfo> orderListItems;

    public List<OrderListItemInfo> getOrderListItems() {
        return orderListItems;
    }

    public void setOrderListItems(List<OrderListItemInfo> orderListItems) {
        this.orderListItems = orderListItems;
    }

    @Override
    public String toString() {
        return "OrderListInfo{" +
                "orderListItems=" + orderListItems +
                '}';
    }
}
