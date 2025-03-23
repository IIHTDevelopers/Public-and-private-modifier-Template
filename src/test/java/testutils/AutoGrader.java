package testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class AutoGrader {

	// Test if the code demonstrates proper usage of public and private modifiers
	public boolean testPublicPrivateModifiers(String filePath) throws IOException {
		System.out.println("Starting testPublicPrivateModifiers with file: " + filePath);

		File participantFile = new File(filePath); // Path to participant's file
		if (!participantFile.exists()) {
			System.out.println("File does not exist at path: " + filePath);
			return false;
		}

		FileInputStream fileInputStream = new FileInputStream(participantFile);
		JavaParser javaParser = new JavaParser();
		CompilationUnit cu;
		try {
			cu = javaParser.parse(fileInputStream).getResult()
					.orElseThrow(() -> new IOException("Failed to parse the Java file"));
		} catch (IOException e) {
			System.out.println("Error parsing the file: " + e.getMessage());
			throw e;
		}

		System.out.println("Parsed the Java file successfully.");

		// Use AtomicBoolean to allow modifications inside lambda expressions
		AtomicBoolean animalClassFound = new AtomicBoolean(false);
		AtomicBoolean dogClassFound = new AtomicBoolean(false);
		AtomicBoolean publicSpeakMethodFound = new AtomicBoolean(false);
		AtomicBoolean privateRunMethodFound = new AtomicBoolean(false);
		AtomicBoolean publicPerformActionMethodFound = new AtomicBoolean(false);
		AtomicBoolean privateMethodAccessedCorrectly = new AtomicBoolean(false);
		AtomicBoolean dogMethodFound = new AtomicBoolean(false);

		// Check for class implementation and method accessibility
		System.out.println("------ Class and Method Check ------");
		for (TypeDeclaration<?> typeDecl : cu.findAll(TypeDeclaration.class)) {
			if (typeDecl instanceof ClassOrInterfaceDeclaration) {
				ClassOrInterfaceDeclaration classDecl = (ClassOrInterfaceDeclaration) typeDecl;

				if (classDecl.getNameAsString().equals("Animal")) {
					System.out.println("Class 'Animal' found.");
					animalClassFound.set(true);
					// Check for public method 'speak' and private method 'run'
					classDecl.getMethods().forEach(method -> {
						if (method.getNameAsString().equals("speak") && method.isPublic()) {
							publicSpeakMethodFound.set(true);
							System.out.println("Public method 'speak' found.");
						}
						if (method.getNameAsString().equals("run") && method.isPrivate()) {
							privateRunMethodFound.set(true);
							System.out.println("Private method 'run' found.");
						}
						if (method.getNameAsString().equals("performAction") && method.isPublic()) {
							publicPerformActionMethodFound.set(true);
							System.out.println("Public method 'performAction' found.");
						}
					});
				}

				if (classDecl.getNameAsString().equals("Dog")) {
					System.out.println("Class 'Dog' found.");
					dogClassFound.set(true);
					// Check for dog-specific methods
					classDecl.getMethods().forEach(method -> {
						if (method.getNameAsString().equals("dogAction") && method.isPublic()) {
							dogMethodFound.set(true);
							System.out.println("Public method 'dogAction' found.");
						}
					});
				}
			}
		}

		// Ensure all classes and methods exist
		if (!animalClassFound.get() || !dogClassFound.get()) {
			System.out.println("Error: Class 'Animal' or 'Dog' not found.");
			return false;
		}

		if (!publicSpeakMethodFound.get()) {
			System.out.println("Error: 'speak' method is not public in 'Animal' class.");
			return false;
		}

		if (!privateRunMethodFound.get()) {
			System.out.println("Error: 'run' method is not private in 'Animal' class.");
			return false;
		}

		if (!publicPerformActionMethodFound.get()) {
			System.out.println("Error: 'performAction' method is not public in 'Animal' class.");
			return false;
		}

		if (!dogMethodFound.get()) {
			System.out.println("Error: 'dogAction' method is not public in 'Dog' class.");
			return false;
		}

		// Check if private method is accessed correctly via public method
		System.out.println("------ Private Method Access Check ------");
		for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
			if (method.getNameAsString().equals("main")) {
				if (method.getBody().isPresent()) {
					method.getBody().get().findAll(MethodCallExpr.class).forEach(callExpr -> {
						if (callExpr.getNameAsString().equals("dogAction")) {
							privateMethodAccessedCorrectly.set(true);
							System.out.println(
									"Private method 'run' is accessed correctly via 'performAction' in 'Dog' class.");
						}
					});
				}
			}
		}

		if (!privateMethodAccessedCorrectly.get()) {
			System.out.println("Error: Private method 'run' not accessed correctly.");
			return false;
		}

		// If all checks pass
		System.out.println("Test passed: Public and private modifiers are correctly implemented.");
		return true;
	}
}
