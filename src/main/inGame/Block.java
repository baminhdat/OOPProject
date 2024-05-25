package main.inGame;

class Block extends Entity{
    boolean passable;

    Block(int id,String s,boolean passable){
        this.id = id;
        this.name = s;
        this.passable=passable;
    }
}
