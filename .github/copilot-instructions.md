# AI Agent Instructions for OOP Project

## Project Overview
This is a Java Object-Oriented Programming project focusing on basic OOP concepts and console input/output operations. The project is structured with standalone Java classes demonstrating fundamental programming concepts.

## Project Structure
- `Main.java`: Entry point for user input handling using Scanner
- `Car.java`: Class template for demonstrating OOP concepts

## Code Patterns and Conventions

### Input Handling
- User input is managed through `java.util.Scanner` in the `Main` class
- Standard pattern for input:
  ```java
  Scanner scanner = new Scanner(System.in);
  System.out.print("Prompt: ");
  // Use appropriate scanner method based on input type
  // nextInt() for integers
  // nextLine() for strings
  ```

### Class Structure
- Each class is defined in its own file with matching filename
- Public classes follow Java naming conventions (PascalCase)
- Main method signature: `public static void main(String[] args)`

## Development Workflow
1. Classes can be run directly if they contain a `main` method
2. `Main.java` is the primary entry point for the application

## Common Tasks
- **Adding User Input**: Use Scanner class with appropriate input method
- **Creating New Classes**: 
  1. Create new .java file matching class name
  2. Define public class with matching name
  3. Add necessary methods and attributes

## Important Note
- Scanner's `nextInt()` and `nextLine()` combination requires careful handling due to line feed issues
- Current `Main.java` has potential input handling issue that needs addressing

## Future Considerations
- Class relationships and inheritance structures may be added
- Input validation and error handling patterns to be established