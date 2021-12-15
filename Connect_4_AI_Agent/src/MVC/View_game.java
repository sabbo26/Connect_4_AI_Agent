package MVC;

public class View_game {

    Controller my_controller ;

    cell[][] board ;

    int user_score ;

    int agent_score ;


    public void process (){
        int k = 0 ;
        if ( ! my_controller.set_board(k)){
            // error
        }
        else {
            my_controller.get_board();
            my_controller.get_user_score();
            // view user play
            boolean w = my_controller.update_board();
            my_controller.get_board();
            my_controller.get_agent_score();
            // view agent play

            if ( w ){

            }
        }
    }


}
