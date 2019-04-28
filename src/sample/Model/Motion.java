package sample.Model;

public class Motion {

    public enum Action{
        NONE,
        ROTATE_LEFT,
        ROTATE_RIGHT,
        ROTATE_BACK,
        SPEED_UP,
        SPEED_DOWN,
        MOVE_CONSTANT,
        STEP
    }

    public static Action[] getSuitableActions(Action previousAction){
        Action[] suitableActions;
        if ( (previousAction== Action.SPEED_UP) | (previousAction== Action.MOVE_CONSTANT)){
            suitableActions    = new Action[2];
            suitableActions[0] = Action.MOVE_CONSTANT;
            suitableActions[1] = Action.SPEED_DOWN;
        }
        else if ((previousAction== Action.ROTATE_LEFT) | (previousAction== Action.ROTATE_RIGHT) | (previousAction== Action.ROTATE_BACK)){
            suitableActions    = new Action[3];
            suitableActions[0] = Action.NONE;
            suitableActions[1] = Action.SPEED_UP;
            suitableActions[2] = Action.STEP;
        }
        else{
            suitableActions = new Action[6];
            suitableActions[0] = Action.NONE;
            suitableActions[1] = Action.ROTATE_LEFT;
            suitableActions[2] = Action.ROTATE_RIGHT;
            suitableActions[3] = Action.ROTATE_BACK;
            suitableActions[4] = Action.SPEED_UP;
            suitableActions[5] = Action.STEP;
        }
        return suitableActions;
    }

    public static int getActionPathCost(Action action){
        if(action == Action.NONE)
            return 0;
        else
            return 1;
    }

    public static int getActionTimeCost(Action action){
        switch (action){
            case NONE:          return 1;
            case ROTATE_LEFT:   return 1;
            case ROTATE_RIGHT:  return 1;
            case MOVE_CONSTANT: return 1;
            default:            return 2;
        }
    }
    public static int getPenaltyCost(Action previousAction, Action action){
        switch (action){
            case NONE: return 1;
            case STEP:
                switch (previousAction){
                    case STEP: return 2;
                    case SPEED_DOWN: return 1;
                }
                break;
            case SPEED_UP:
                switch (previousAction){
                    case STEP: return 2;
                    case SPEED_DOWN: return 1;
                }
                break;
        }
        return 0;
    }

    public static Action getRotateActionByStatusChange(PointInfo.Status previousStatus, PointInfo.Status status){
        return Action.ROTATE_LEFT;
    }


    

}
