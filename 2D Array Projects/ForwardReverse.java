public class Main {
    public static void main(String[] args) {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        System.out.println("Forward Array");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + ", ");
            }
            System.out.println();
        }

        System.out.println("\nReverse Array");
        for (int x = matrix.length - 1; x >= 0; x--) {
            for (int z = matrix[x].length - 1; z >= 0; z--){
                System.out.print(matrix[x][z] + ", ");
            }
            System.out.println();
        }
    }
}
