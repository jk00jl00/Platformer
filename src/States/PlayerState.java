package States;

public class PlayerState{

    private static PlayerStateStack playerState = new PlayerStateStack();

    public static CreatureState getCurrent(){
        return playerState.getCurrent();
    }

    public static void push(CreatureState state){
        playerState.push(state);
    }
    public static void pop(){
        playerState.pop();
    }



    private static class PlayerStateStack{
        PlayerStateStackItem first;

        PlayerStateStack(){
        }

        void push(CreatureState state){
            PlayerStateStackItem temp = new PlayerStateStackItem(state);
            temp.next = first;
            first = temp;
        }
        void pop(){
            first = first.next;
        }
        CreatureState getCurrent(){
            return first.item;
        }

        private class PlayerStateStackItem{
            PlayerStateStackItem next;
            CreatureState item;
            PlayerStateStackItem(CreatureState state){
                this.item = state;
            }
        }
    }
}
