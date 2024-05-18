package contructors;

public class Person {
	private String name;
	private String surname;
	private int age;
	
	Person() {
	  this.age = 7;
	}
	
	Person(String name) {
		this.name = name;
	}

	public Person(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}
	
	public Person(Person person) {
		this.name = person.getName();
		this.surname = person.getSurname();
		this.age = person.getAge();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public static void main(String[] args) {
		Person firstPerson = new Person("John", "Smith");
		
		System.out.println(firstPerson.getName());
		System.out.println(firstPerson.getSurname());
		
		Person secondPerson = new Person(firstPerson);
		System.out.println(secondPerson.getName());
		System.out.println(secondPerson.getSurname());
	}
}
