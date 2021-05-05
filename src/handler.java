import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class handler {
    static int startX;
    static int startY;
    static ArrayList<Integer> endPointX;
    static ArrayList<Integer> endPointY;

    public static void main(String[] args) throws Exception {
        endPointY = new ArrayList<>();
        endPointX = new ArrayList<>();

        int[][] map = createMap(args[0]);

        createVisualMap(map);
        System.out.println("\nStarting point: (" + startX + ", " + startY + ")");

        //navigator nvg = new navigator(map,startX,startY,endPointX,endPointY);
    }

    /* Turns the input file's contents into a 2D array */
    public static int[][] createMap(String input) throws Exception
    {
        int[][] map = new int[10][10];

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
        for(int y = 0; y < 10; y++){
            for(int x = 0; x < 10; x++){
                switch (cells[(y*10) + x]) {
                    case "0":
                        map[x][y] = 0;
                        break;
                    case "1":
                        // Save the location of the starting cell
                        startX = x;
                        startY = y;
                        map[x][y] = 1;
                        break;
                    case "2":
                        // Save the location of the endpoints
                        endPointX.add(x);
                        endPointY.add(y);
                        map[x][y] = 2;
                        break;
                    case "3":
                        map[x][y] = 3;
                        break;
                    default:
                        System.out.println("Uh oh.");
                }
            }
        }

        return map;
    }

    /* Creates a visual representation of the 2D map */
    public static void createVisualMap(int[][] map) {
        System.out.println("######################");
        for(int y = 0; y < 10; y++){
            System.out.print("#");
            for(int x = 0; x < 10; x++){
                switch (map[x][y]) {
                    case 0:
                        //cell is empty
                        System.out.print("  ");
                        break;
                    case 1:
                        //starting cell
                        System.out.print("ST");
                        break;
                    case 2:
                        //goal cell
                        System.out.print("EN");
                        break;
                    case 3:
                        //wall
                        System.out.print("[]");
                        break;
                    case 5:
                        //ai path
                        System.out.print("::");
                        break;
                    case 6:
                        //reached endpoint
                        System.out.print(";;");
                        break;
                    default:
                        System.out.println("??");
                }
            }
            System.out.println("#");
        }
        System.out.println("######################");
    }

}
