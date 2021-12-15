package MVC;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class View_game {

    @FXML
    public TextField user_input;
    @FXML
    public Button insertBtn;
    @FXML
    public Label time_taken;
    @FXML
    public Label number_expanded_nodes;
    @FXML
    public Label user_score_label;
    @FXML
    public Label agent_score_label;
    @FXML
    private GridPane grid;
    @FXML
    private TextField depth_input;
    @FXML
    private CheckBox alpha_beta;
    @FXML
    private RadioButton redColor;
    @FXML
    private RadioButton yellowoColor;
    @FXML
    private RadioButton fpUser;
    @FXML
    private RadioButton fpAgent;
    @FXML
    private Button startBtn;
    @FXML
    private Button endBtn;


    final int ROWS = 6;
    final int COLUMNS = 7;

    Controller my_controller ;

    cell[][] board ;

    int user_score ;

    int agent_score ;

    long time ;

    long expanded_nodes;

    int levels ;
    boolean alphaBeta;
    boolean first_play = false;

    Paint user_color;
    Paint agent_color;

    private Circle[][] circles = null;

    @FXML
    public void handleRedRadio(){
        if(redColor.isSelected())  yellowoColor.setSelected(false);
    }

    @FXML
    public void handleYellowoioRadio(){
        if(yellowoColor.isSelected())  redColor.setSelected(false);
    }

    @FXML
    public void handleUserRadio(){
        if(fpUser.isSelected())  fpAgent.setSelected(false);
    }

    @FXML
    public void handleAgentRadio(){
        if(fpAgent.isSelected())  fpUser.setSelected(false);
    }


    @FXML
    public void startBtn(){
        // Error checking
        if(!depth_input.getText().matches("\\d+") ){
            createAlert("Error","Depth must be Integer" , Alert.AlertType.ERROR);
        }

        if(!redColor.isSelected() && !yellowoColor.isSelected()){
            createAlert("Error","You must choose one of the colors" , Alert.AlertType.ERROR);
        }

        if(!fpUser.isSelected() && !fpAgent.isSelected()){
            createAlert("Error","You must choose one of the players" , Alert.AlertType.ERROR);
        }
        // Error checking

        first_play = fpUser.isSelected();

        user_color = redColor.isSelected() ? Color.RED : Color.YELLOW;
        agent_color = redColor.isSelected() ? Color.YELLOW : Color.RED ;

        levels = Integer.parseInt(depth_input.getText());

        alphaBeta = alpha_beta.isSelected();

        int i = 0 , j = 0 ;

        this.circles = new Circle[ROWS][COLUMNS];
        for(Node node : this.grid.getChildren())
        {
                this.circles[5 - i][j] = (Circle) node;
                this.circles[5 - i][j].setFill(Color.WHITE);

                if( j == COLUMNS - 1){
                    j = 0;
                    i++;
                }
                else {
                    j++;
                }
        }

        my_controller = Controller.getInstance();

        my_controller.createModel(levels , alphaBeta);
        my_controller.setView(this);

        if(! first_play){
           agent_play();
           update_view();
        }

        depth_input.setDisable(true);
        redColor.setDisable(true);
        yellowoColor.setDisable(true);
        fpUser.setDisable(true);
        fpAgent.setDisable(true);
        alpha_beta.setDisable(true);
        insertBtn.setDisable(false);
        startBtn.setDisable(true);
        user_score_label.setText("0");
        agent_score_label.setText("0");
        number_expanded_nodes.setText("0");
        time_taken.setText("0");
    }

    public void endBtn(){
        depth_input.setDisable(false);
        redColor.setDisable(false);
        yellowoColor.setDisable(false);
        fpUser.setDisable(false);
        fpAgent.setDisable(false);
        alpha_beta.setDisable(false);
        insertBtn.setDisable(true);
        startBtn.setDisable(false);
    }


    public void process (){
        if(!user_input.getText().matches("\\d+")){
            createAlert("Error","column must be Integer" , Alert.AlertType.ERROR);
        }

        int col = Integer.parseInt(user_input.getText()) ;

        if (col < 1 ||  col > 7){
            createAlert("Error","column must between 1 and 7" , Alert.AlertType.ERROR);
        }


        if ( !my_controller.set_board(col-1) ){
            createAlert("Error","column is full" , Alert.AlertType.ERROR);
        }
        else{
            board = my_controller.get_board();
            user_score = my_controller.get_user_score();
            agent_score = my_controller.get_agent_score();

            update_view(); // view user play

            if(my_controller.get_num_of_moves() == 0) {
               end_game();
            }// finish

            agent_play();

            update_view(); // view agent play

            if ( my_controller.get_num_of_moves() == 0 ){
                end_game();
            }
        }
    }


    public void update_view(){
        for (int i = 0 ; i < ROWS ; i++){
            for (int j = 0 ; j < COLUMNS ; j++){
                if(board[i][j] == cell.user ){
                    circles[i][j].setFill(user_color);
                }
                else if(board[i][j] == cell.agent) {
                    circles[i][j].setFill(agent_color);
                }
            }
        }

        time_taken.setText(time + " ms");
        number_expanded_nodes.setText(Long.toString(expanded_nodes));
        user_score_label.setText(Integer.toString(user_score));
        agent_score_label.setText(Integer.toString(agent_score));

    }

    public void agent_play(){
        my_controller.update_board();
        board = my_controller.get_board();
        user_score = my_controller.get_user_score();
        agent_score = my_controller.get_agent_score();
        time = my_controller.get_time_taken();
        expanded_nodes = my_controller.get_num_of_expanded_nodes();
    }

    public void end_game(){
        if (user_score > agent_score)
            createAlert("Result","I did my best but you won :(" , Alert.AlertType.INFORMATION);

        else if(agent_score > user_score){
            createAlert("Result","I won. Try to play better :D" , Alert.AlertType.INFORMATION);
        }
        else {
            createAlert("Result","Good game. I will beat you next time" , Alert.AlertType.INFORMATION);

        }

        endBtn();
    }

    public void createAlert(String title , String header , Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

}
