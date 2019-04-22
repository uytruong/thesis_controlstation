package sample.Model;

public class Motion {

    public enum Action{
        NONE(0),
        ROTATE_LEFT(1),
        ROTATE_RIGHT(2),
        ROTATE_BACK(3),
        SPEED_UP(4),
        SPEED_DOWN(5),
        MOVE_CONSTANT(6),
        STEP(7);

        private final int value;

        Action(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static Action[] getSuitableActions(Action previousAction){
        Action[] suitableActions;
        if ( (previousAction== Action.SPEED_UP) | (previousAction== Action.MOVE_CONSTANT)){
            suitableActions = new Action[2];
            suitableActions[0] = Action.SPEED_DOWN;
            suitableActions[1] = Action.MOVE_CONSTANT;
        }
        else{
            suitableActions = new Action[8];
            suitableActions[0] = Action.NONE;
            suitableActions[1] = Action.ROTATE_LEFT;
            suitableActions[2] = Action.ROTATE_RIGHT;
            suitableActions[3] = Action.ROTATE_BACK;
            suitableActions[4] = Action.SPEED_UP;
            suitableActions[5] = Action.SPEED_DOWN;
            suitableActions[6] = Action.MOVE_CONSTANT;
            suitableActions[7] = Action.STEP;
        }
        return suitableActions;
    }

    public static int getActionPathCost(Action action){
        switch (action){
            case SPEED_UP:   return 2;
            case SPEED_DOWN: return 2;
            case STEP:       return 2;
            case ROTATE_BACK:return 2;
            default:         return 1;
        }
    }

    public static int getActionTimeCost(Action action){
        switch (action){
            case SPEED_UP:   return 2;
            case SPEED_DOWN: return 2;
            case STEP:       return 2;
            case ROTATE_BACK:return 2;
            default:         return 1;
        }
    }

    

}
