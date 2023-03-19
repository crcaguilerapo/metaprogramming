package co.cristian;

@Annotation1(type ="map", deep = 1)
public class PersonDto {
    String name;
    String lastName;

    public PersonDto(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }
}
