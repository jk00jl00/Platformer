package States.PlayerStates;

public class PlayerStateStack {

    private static PlayerStateStackController playerState = new PlayerStateStackController();

    public static PlayerState getCurrent(){
        try {
            return playerState.getCurrent();
        } catch (NullPointerException e) {
            return null;
            
        }
    }

    public static void push(PlayerState state){
        playerState.push(state);
    }
    public static void pop(){
        playerState.pop();
    }



    private static class PlayerStateStackController {
        PlayerStateStackItem first;

        PlayerStateStackController(){
        }

        void push(PlayerState state){
            PlayerStateStackItem temp = new PlayerStateStackItem(state);
            temp.next = first;
            first = temp;
        }
        void pop(){
            first = first.next;
        }
        PlayerState getCurrent(){
            return first.item;
        }

        private class PlayerStateStackItem{
            PlayerStateStackItem next;
            PlayerState item;
            PlayerStateStackItem(PlayerState state){
                this.item = state;
            }
        }
    }
}
