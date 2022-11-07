import java.util.List;

public class Result {
    String pharmaName;
    List<String> addresses;
    float price;

    public Result(String pharmaName, List<String> addresses, float price) {
        this.pharmaName = pharmaName;
        this.addresses = addresses;
        this.price = price;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Name: ").append(this.pharmaName).append("\n");
        str.append("Addresses:" + "\n");
        for (String s: this.addresses) {
            str.append(s).append("\n");
        }
        str.append("Price: ").append(this.price);
        return str.toString();
    }
}
