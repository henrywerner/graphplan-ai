import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class handler {
    static byte startX;
    static byte startY;
    static ArrayList<Byte> endPointX;
    static ArrayList<Byte> endPointY;

    public static void main(String[] args) throws Exception {
        endPointY = new ArrayList<>();
        endPointX = new ArrayList<>();

        byte[][] map = createMap(args[0]);

        createVisualMap(map);
        System.out.println("\nStarting point: (" + startX + ", " + startY + ")");

        //navigator nvg = new navigator(map,startX,startY,endPointX,endPointY);
    }

    /* Turns the input file's contents into a 2D array */
    public static byte[][] createMap(String input) throws Exception
    {
        byte[][] map = new byte[10][10];

        File mapFile = new File(input); //search for file name
        Scanner scnr = new Scanner(mapFile);
        String contents = null;
        String[] cells = new String[100];

        //assumes map file contains all information on a single line
        if (scnr.hasNextLine())
            contents = scnr.nextLine();

        //split string along spaces
        if (contents != null)
            cells = contents.split("\\s+");

        //construct matrix
        for(byte y = 0; y < 10; y++){
            for(byte x = 0; x < 10; x++){
                switch (cells[(y * 10) + x]) {
                    case "0" -> map[x][y] = 0;
                    case "1" -> {
                        // Save the location of the starting cell
                        startX = x;
                        startY = y;
                        map[x][y] = 1;
                    }
                    case "2" -> {
                        // Save the location of the endpoints
                        endPointX.add(x);
                        endPointY.add(y);
                        map[x][y] = 2;
                    }
                    case "3" -> map[x][y] = 3;
                    default -> System.out.println("Uh oh.");
                }
            }
        }

        return map;
    }

    /* Creates a visual representation of the 2D map */
    public static void createVisualMap(byte[][] map) {
        System.out.println("######################");
        for(byte y = 0; y < 10; y++){
            System.out.print("#");
            for(byte x = 0; x < 10; x++){
                switch (map[x][y]) {
                    case 0 -> System.out.print("  "); //cell is empty
                    case 1 -> System.out.print("ST"); //starting cell
                    case 2 -> System.out.print("EN"); //goal cell
                    case 3 -> System.out.print("[]"); //wall
                    case 5 -> System.out.print("::"); //ai path
                    case 6 -> System.out.print(";;"); //reached endpoint
                    default -> System.out.println("??");
                }
            }
            System.out.println("#");
        }
        System.out.println("######################");
    }

}
