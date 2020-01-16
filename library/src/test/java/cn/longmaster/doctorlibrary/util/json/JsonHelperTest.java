package cn.longmaster.doctorlibrary.util.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * @author ABiao_Abiao
 * @date 2019/8/26 20:34
 * @description:
 */
public class JsonHelperTest {
    private String TAG = JsonHelperTest.class.getSimpleName();
    @Test
    public void toJSONObject() {

    }

    @Test
    public void toJSONArray() {
    }

    @Test
    public void toList() {

    }

    @Test
    public void toObject() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject("{\"_onlineState\":0,\"_queryUserID\":1000050,\"_result\":0}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonHelper.toObject(jsonObject,Test1.class);
    }
    public class Test1 {

        /**
         * _onlineState : 0
         * _queryUserID : 1001909
         * _result : 0
         */
        @JsonField("_onlineState")
        private int onlineState;
        @JsonField("_queryUserID")
        private int queryUserID;
        @JsonField("_result")
        private int result;

        public int getOnlineState() {
            return onlineState;
        }

        public void setOnlineState(int onlineState) {
            this.onlineState = onlineState;
        }

        public int getQueryUserID() {
            return queryUserID;
        }

        public void setQueryUserID(int queryUserID) {
            this.queryUserID = queryUserID;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }
    }
}