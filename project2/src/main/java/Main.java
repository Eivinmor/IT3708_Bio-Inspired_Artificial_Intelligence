import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Map map = DataReader.readMapData("p01");
        map.getDepotCoords();
        map.getcustomerCoords();
    }
}


