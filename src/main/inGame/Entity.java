package main.inGame;

public abstract class Entity {
    public int id;
    public String name;
    public Entity(){

    }
    public Entity(int id, String name) {
        this.id=id;
        this.name=name;
    }
}
