package com.yaksha.assignment;

// Animal class demonstrating public and private modifiers
class Animal {

	// Public method that can be accessed outside the class
	public void speak() {
		System.out.println("The animal makes a sound.");
	}

	// Private method that can only be accessed within this class
	private void run() {
		System.out.println("The animal runs.");
	}

	// Public method to invoke private method inside the class
	public void performAction() {
		run(); // Invoking the private method inside the class
	}
}

// Dog class - Inherits from Animal
class Dog extends Animal {

	// Overriding the public speak method
	@Override
	public void speak() {
		System.out.println("The dog barks.");
	}

	// Method to access private method of the parent class (Animal)
	public void dogAction() {
		performAction(); // Calling the public method that internally calls the private method
	}
}

public class PublicPrivateModifierAssignment {
	public static void main(String[] args) {
		Dog dog = new Dog(); // Creating a Dog object
		dog.speak(); // Should print "The dog barks." (public method overridden)
		dog.dogAction(); // Should print "The animal runs." (accessing the private method via public
							// method)
	}
}
