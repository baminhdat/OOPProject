package main.inGame;

public class Enemy extends Entity {
    boolean isEnemy;
    Enemy(){

    }
    Enemy(int id){
        this.id = id;
    }
    public Enemy(int id, String name){
        super(id,name);
        isEnemy = true;
    }
}
