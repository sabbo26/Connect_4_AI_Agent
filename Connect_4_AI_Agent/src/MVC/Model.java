package MVC;

enum cell {
    user, agent, empty ;
}

public class Model {

    private int k ;

    private boolean alpha_beta ;

    private cell[][] board ;

    private int num_of_moves ;

    private int user_score ;

    private int agent_score ;

    private final int ROWS = 6 ;

    private final int COLUMNS = 7 ;

    private long num_of_expanded_nodes ;

    private long time_taken ;

    public Model( boolean alpha_beta , int k ){

        board = new cell[ROWS][COLUMNS];

        for (int i = 0 ; i < ROWS ; i++){
            for (int j = 0 ; j < COLUMNS ; j++){
                board[i][j] = cell.empty;
            }
        }

        num_of_moves = 42 ;

        this.alpha_beta = alpha_beta ;

        this.k = k ;

        this.agent_score = 0 ;

        this.user_score = 0 ;

        this.num_of_expanded_nodes = 0 ;

        this.time_taken = 0 ;

    }

    // called by controller to insert user cell in board
    // return true on success, false otherwise

    public boolean set_board (int col){

        boolean success = insert( col , cell.user ) ;

        if ( success ){
            update_score(col);
            num_of_moves--;

        }

        return success ;
    }

    // update score after inserting in board

    private void update_score ( int col ){

        int row = -1 ;

        for (int i = ROWS - 1 ; i >= 0 ; i-- ){
            if ( board[i][col] != cell.empty ){
                row = i ;
                break;
            }
        }

        update_score_horizontally(row,col);

        update_score_vertically(row,col);

        update_score_diagonally(row,col);

    }

    private void update_score_diagonally( int row , int col ){

        cell val = board[row][col];

        int start_i = row ;

        int start_j = col ;

        int end_i = row ;

        int end_j = col ;

        int counter = 0 ;

        while ( start_i - 1 >= 0 && start_j - 1 >= 0  && counter < 3 ){
            if ( board[start_i-1][start_j-1] == val ){
                start_i--;
                start_j--;
                counter++;
            }
            else
                break;
        }

        counter = 0 ;

        while ( end_i + 1 < ROWS && end_j + 1 < COLUMNS  && counter < 3 ){
            if ( board[end_i+1][end_j+1] == val ){
                end_i++;
                end_j++;
                counter++;
            }
            else
                break;
        }

        if ( val == cell.agent )
            agent_score += Math.max( 0 , end_i - start_i - 2 ) ;
        else
            user_score += Math.max( 0 , end_i - start_i - 2 ) ;

        start_i = row ;

        start_j = col ;

        end_i = row ;

        end_j = col ;

        counter = 0 ;

        while ( start_i + 1  < ROWS  && start_j - 1 >= 0  && counter < 3 ){
            if ( board[start_i+1][start_j-1] == val ){
                start_i++;
                start_j--;
                counter++;
            }
            else
                break;
        }

        counter = 0 ;

        while ( end_i - 1 >= 0 && end_j + 1 < COLUMNS  && counter < 3 ){
            if ( board[end_i-1][end_j+1] == val ){
                end_i--;
                end_j++;
                counter++;
            }
            else
                break;
        }

        if ( val == cell.agent )
            agent_score += Math.max( 0 , end_j - start_j - 2 ) ;
        else
            user_score += Math.max( 0 , end_j - start_j - 2 ) ;

    }

    private void update_score_vertically( int row , int col ){

        cell val = board[row][col];

        int start = row ;

        int counter = 0 ;

        while ( start - 1 >= 0 && counter < 3 ){
            if ( board[start-1][col] == val ){
                start--;
                counter++;
            }
            else
                break;
        }

        if ( val == cell.agent )
            agent_score += Math.max( 0 , row - start - 2 ) ;
        else
            user_score += Math.max( 0 , row - start - 2 ) ;
    }

    private void update_score_horizontally( int row , int col ){

        cell val = board[row][col] ;

        int start = col ;

        int end = col ;

        int counter = 0 ;

        while ( start - 1 >= 0 && counter < 3  ){
            if ( board[row][start-1] == val ){
                start--;
                counter++;
            }
            else
                break;
        }

        counter = 0 ;

        while ( end + 1 < COLUMNS && counter < 3  ){
            if ( board[row][end+1] == val ){
                end++;
                counter++;
            }
            else
                break;
        }

        if ( val == cell.agent )
            agent_score += Math.max( 0 , end - start - 2 ) ;
        else
            user_score += Math.max( 0 , end - start - 2 ) ;

    }

    // insert a value 'val' on top of column 'col'
    // return true on success, false otherwise

    private boolean insert ( int col , cell val ){

        if ( board[ROWS-1][col] != cell.empty )
            return false ;

        int index = ROWS - 1 ;

        for ( int i = ROWS - 2 ; i >= 0 ; i-- ){
            if ( board[i][col] == cell.empty )
                index = i ;
            else
                break;
        }

        board[index][col] = val ;

        return true ;

    }

    // delete the value on top of column 'col'

    private void delete (int col ){

        for ( int i = ROWS - 1 ; i >= 0 ; i-- ){
            if ( board[i][col] != cell.empty ){
                board[i][col] = cell.empty ;
                return;
            }
        }

    }

    // used
    private int eval_func(cell[][] board){

        int score =  100 * ( agent_score - user_score ) ;

        int old_user = user_score;

        int old_agent = agent_score;

        int candidate_user = 0,candidate_agent = 0;

        //minimizer
        for(int i = 0 ; i < COLUMNS ; i++){
            if(insert(i,cell.user)){
                update_score(i);
                candidate_user+=  user_score - old_user;
                delete(i);
                user_score = old_user ;
            }
        }

        //maximizer
        for(int i = 0 ; i < COLUMNS ; i++){
            if(insert(i,cell.agent)){
                update_score(i);
                candidate_agent+= agent_score - old_agent;
                delete(i);
                agent_score = old_agent;
            }
        }
        score += (candidate_agent - candidate_user);
        return score;
    }



    // not used
    private int eval_func_2 ( cell[][] board ){
        int score =0;
        int empty_cols[] = new int[7];


        //check vertically
        for(int i = 0 ; i < 7 ; i++){
            int col_val = 0;
            int row = 5;
            boolean full = true;
            cell current = board[row][i];
            while(row >=0){
                if(board[row][i] == cell.empty){
                    full = false;
                    empty_cols[i] += row+1;
                    break;
                }
                else{
                    if(board[row][i] == current)
                        col_val++;
                    else{
                        current = board[row][i];
                        col_val=1;
                    }
                }
                row--;
            }
            //set score
            if(!full){
                if(current == cell.agent)
                    score += Math.min(col_val*10,4*10);
                else
                    score -= Math.min(col_val*10,4*10);
            }
        }


        // check horizontally
        for(int i = 0 ; i < 6 ; i++){
            int row_val = 0;
            cell current = board[i][0];
            boolean empty = true;
            for(int j = 0 ; j < 7 ; j++){
                if(board[i][j] == cell.empty)
                    row_val++;
                else if(board[i][j] == current){
                    empty = false;
                    row_val+= 5;
                }
                else{
                    if(current == cell.empty){
                        current = board[i][j];
                        row_val+= 5;
                        continue;
                    }
                    else{
                        if(current == cell.agent)
                            score += row_val*10;
                        else
                            score -= row_val*10;
                        current = board[i][j];
                        row_val= current==cell.empty ? 1:5;
                    }
                }
            }
            //set score
            if(!empty){
                if(current == cell.agent)
                    score += row_val*10;
                else
                    score -= row_val*10;
            }
        }

        //check diagonally going down \
        int M=6,N=7,K=4;
        boolean flag = true;
        for(int i=M-1;i>=K-1;--i)
        {   flag = true;
            for(int j=0;(j<=N-K &&flag);++j)
            {
                if(i < 5)
                    flag = false;
                int diag_val = 0;
                boolean empty = true;
                cell current = board[i][j];
                for(int k=0;k<8;++k)
                {
                    if( (i-k) >= 0 && (i-k) <6 &&  (j+k) >= 0 && (j+k) <7 ){
                        int r = i-k,c=j+k;
                        if(board[r][c] == cell.empty)
                            diag_val++;
                        else if(board[r][c] == current){
                            empty = false;
                            diag_val+= 5;
                        }
                        else{
                            if(current == cell.empty){
                                current = board[r][c];
                                diag_val+= 5;
                                continue;
                            }
                            else{
                                if(current == cell.agent)
                                    score += diag_val*10;
                                else
                                    score -= diag_val*10;
                                current = board[i][j];
                                diag_val= current==cell.empty ? 1:5;
                            }
                        }
                    }

                }
                //set score
                if(!empty){
                    if(current == cell.agent)
                        score += diag_val*10;
                    else
                        score -= diag_val*10;
                }
            }
        }



        //check diagonally going up /

        flag = true;
        for(int i=0;i<=M-K;++i)
        {   flag = true;
            for(int j = 0; (j <= N - K && flag); ++j)
            {
                if(i > 0)
                    flag = false;
                int diag_val = 0;
                boolean empty = true;
                cell current = board[i][j];
                for(int k=0;k<8;++k)
                {
                    if( (i+k) >= 0 && (i+k) <6 &&  (j+k) >= 0 && (j+k) <7 ){
                        int r = i+k,c=j+k;
                        if(board[r][c] == cell.empty)
                            diag_val++;
                        else if(board[r][c] == current){
                            empty = false;
                            diag_val+= 5;
                        }
                        else{
                            if(current == cell.empty){
                                current = board[r][c];
                                diag_val+= 5;
                                continue;
                            }
                            else{
                                if(current == cell.agent)
                                    score += diag_val*10;
                                else
                                    score -= diag_val*10;
                                current = board[i][j];
                                diag_val= current==cell.empty ? 1:5;
                            }
                        }
                    }

                }
                //set score
                if(!empty){
                    if(current == cell.agent)
                        score += diag_val*10;
                    else
                        score -= diag_val*10;
                }
            }
        }


        return score ;
    }

    // run minimax for each successor of current state and choose state with max minimax value


    private void minimax_with_pruning(){

        num_of_expanded_nodes = 1 ; //root

        int max = Integer.MIN_VALUE ;

        int max_j = -1 ;

        int old_user_score = user_score;
        int old_agent_score = agent_score;

        for ( int j = 0 ; j < COLUMNS ; j++ ){

            if (  insert( j , cell.agent) ){

                num_of_expanded_nodes++;

                update_score(j);

                int x = min_with_pruning( Integer.MIN_VALUE , Integer.MAX_VALUE , 0 )  ;

                if ( x > max ){
                    max = x ;
                    max_j = j ;
                }

                delete(j);

                agent_score = old_agent_score;
                user_score = old_user_score;

            }
        }

        insert( max_j , cell.agent) ;

        update_score( max_j );

    }


    private int max_with_pruning(  int alpha , int beta , int depth  ){

        if ( depth == k )
            return eval_func(board);

        int old_user_score = user_score;
        int old_agent_score = agent_score;

        int v = Integer.MIN_VALUE ;

        for ( int j = 0 ; j < COLUMNS ; j++ ){

            if (  insert( j , cell.agent) ){

                num_of_expanded_nodes++;

                update_score(j);

                v = Math.max( v ,  min_with_pruning( alpha , beta , depth+1 ) ) ;

                if ( v >= beta ){
                    delete(j);
                    agent_score = old_agent_score;
                    user_score = old_user_score;
                    return v ;
                }

                alpha = Math.max( alpha , v  );

                delete(j);

                agent_score = old_agent_score;
                user_score = old_user_score;

            }
        }

        return v ;

    }

    private int min_with_pruning( int alpha , int beta , int depth ){

        if ( depth == k )
            return eval_func(board);

        int old_user_score = user_score;
        int old_agent_score = agent_score;

        int v = Integer.MAX_VALUE ;

        for ( int j = 0 ; j < COLUMNS ; j++ ){

            if (  insert( j , cell.user) ){

                num_of_expanded_nodes++;

                update_score(j);

                v = Math.min( v ,  max_with_pruning( alpha , beta , depth + 1 ) ) ;

                if ( v <= alpha ){
                    delete(j);
                    agent_score = old_agent_score;
                    user_score = old_user_score;
                    return v ;
                }

                beta = Math.min( beta , v  );

                delete(j);

                agent_score = old_agent_score;
                user_score = old_user_score;
            }
        }

        return v ;

    }


    // run minimax without pruning for each successor of current state and choose state with max minimax value
    private void minimax_without_pruning(){

        num_of_expanded_nodes = 1 ; // root

        int max = Integer.MIN_VALUE ;

        int max_j = -1 ;

        int old_user_score = user_score;
        int old_agent_score = agent_score;

        for ( int j = 0 ; j < COLUMNS ; j++ ){

            if (  insert( j , cell.agent) ){

                num_of_expanded_nodes++;

                update_score(j);

                int x = min_without_pruning(0 ) ;

                if ( x > max ){
                    max = x ;
                    max_j = j ;
                }

                delete(j);

                agent_score = old_agent_score;
                user_score = old_user_score;

            }
        }

        insert( max_j , cell.agent) ;

        update_score( max_j );
    }

    private int min_without_pruning(int depth){

        if ( depth == k )
            return eval_func(board);

        int old_user_score = user_score;
        int old_agent_score = agent_score;

        int v = Integer.MAX_VALUE ;

        for ( int j = 0 ; j < COLUMNS ; j++ ){

            if (  insert( j , cell.user) ){

                num_of_expanded_nodes++;

                update_score(j);

                v = Math.min( v ,  max_without_pruning(depth + 1 ) ) ;

                delete(j);

                user_score = old_user_score;
                agent_score = old_agent_score;
            }
        }

        return v;
    }

    private int max_without_pruning(int depth){

        if ( depth == k )
            return eval_func(board);

        int old_user_score = user_score;
        int old_agent_score = agent_score;

        int v = Integer.MIN_VALUE ;

        for ( int j = 0 ; j < COLUMNS ; j++ ){

            if (  insert( j , cell.user) ){

                num_of_expanded_nodes++;

                update_score(j);

                v = Math.max( v ,  min_without_pruning(depth + 1 ) ) ;

                delete(j);

                user_score = old_user_score;
                agent_score = old_agent_score;
            }
        }

        return v;
    }

    // run minimax and modify the board with agent's play
    // return true on finishing, false otherwise

    public void update_board( ){

        time_taken = System.currentTimeMillis();

        if (alpha_beta)
            minimax_with_pruning();
        else
            minimax_without_pruning();

        time_taken = System.currentTimeMillis() - time_taken ;

        num_of_moves-- ;
    }

    public cell[][] get_board(){
        return this.board ;
    }

    public int get_user_score (){
        return this.user_score ;
    }

    public int get_agent_score (){
        return this.agent_score;
    }

    public long get_num_of_expanded_nodes() {
        return num_of_expanded_nodes;
    }

    public long get_time_taken () {
        return this.time_taken ;
    }

    public int get_num_of_moves(){return num_of_moves;}
}