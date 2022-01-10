package mtcg.Application;
import java.util.ArrayList;

public class Trade {
    private ArrayList<String> tradeData;


    public Trade(ArrayList<String> tradeData) {
        setTradeData(tradeData);
    }


    public ArrayList<String> getTradeData() {
        return tradeData;
    }
    public void setTradeData(ArrayList<String> tradeData) {
        this.tradeData = tradeData;
    }
}
