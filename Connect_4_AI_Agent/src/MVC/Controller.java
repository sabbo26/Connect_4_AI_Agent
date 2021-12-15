package MVC;

public class Controller {

    private Model my_model ;

    private View_game my_view ;

    private static Controller controller ;

    private Controller(){
    }

    public static Controller getInstance(){
        if(controller == null){
          return   controller = new Controller();
        }
        return controller;
    }

    public void createModel(int levels , boolean alpha_beta){
        this.my_model = new Model(alpha_beta , levels);
    }

    public void setView(View_game my_view){
        this.my_view = my_view;
    }

    public boolean set_board ( int col ){
        return my_model.set_board(col);
    }

    public void update_board(  ){
        my_model.update_board() ;
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

    public long get_num_of_expanded_nodes() {
        return my_model.get_num_of_expanded_nodes();
    }

    public long get_time_taken () {
        return my_model.get_time_taken() ;
    }

    public int get_num_of_moves(){
        return my_model.get_num_of_moves();
    }


}