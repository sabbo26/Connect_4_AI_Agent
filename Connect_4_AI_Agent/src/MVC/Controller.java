package MVC;

public class Controller {

    Model my_model ;

    View_game my_view ;

    public Controller(Model my_model, View_game my_view) {
        this.my_model = my_model;
        this.my_view = my_view;
    }

    public boolean set_board ( int col ){
        return my_model.set_board(col);
    }

    public boolean update_board(  ){
        return my_model.update_board() ;
    }

    public cell[][] get_board(){
        return my_model.get_board() ;
    }

    public int get_user_score (){
        return  my_model.get_user_score() ;
    }

    public int get_agent_score (){
        return my_model.get_agent_score() ;
    }

    public int get_num_of_expanded_nodes() {
        return my_model.get_num_of_expanded_nodes();
    }

    public long get_time_taken () {
        return my_model.get_time_taken() ;
    }


}
