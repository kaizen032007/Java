public class Main {
    public static void main(String[] args) {
        String[][] matrix = new String[3][3];

        System.out.println("Students who got remarkable scores in their perspective subjects");

        matrix[0][0] = "Student A:";
        matrix[0][1] = "Math";
        matrix[0][2] = "90.9";
        
        matrix[1][0] = "Student B:";
        matrix[1][1] = "Science";
        matrix[1][2] = "95.6";

        matrix[2][0] = "Student C:";
        matrix[2][1] = "ESP";
        matrix[2][2] = "93.4";

        for (int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
