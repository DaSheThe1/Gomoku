import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class Gomoku extends JFrame implements ActionListener {
    public static int size=15;//size of board size*size
    public static String pathname="D:\\Java experiment\\";
    public static JFrame f = new JFrame();
    public static JPanel p=new JPanel();//panel for the board
    JPanel menu = new JPanel();//panel for menu
    public static JDialog s;//save dialog
    //menu buttons
    public static JButton undo = new JButton("Undo");//Undo button
    public static JButton restartGame = new JButton("Restart Game");//restart game button
    public static JButton save = new JButton("Save");//save button
    public static JButton load = new JButton("Load");//load button
    public static JButton vs_player = new JButton("Vs Player");//player button
    public static JButton vs_computer = new JButton("Vs Computer");//ai button
    //save components
    public static JButton submit = new JButton("Submit");
    public static JTextField place = new JTextField();
    //the board stuff
    public static BWButton buttons[][]=new BWButton[size][size];//the buttons array
    public static int board[][] = new int[size][size];// 0 = empty 1 = player, 2 = computer
    public static Stack<Move> moves= new Stack<Move>();//stack of all of the moves that gonna be played this game;
    public static int turn = 1;//1 for player 2 for second player/computer
    public static List<Move> computerMoves = new ArrayList<>();
    public static boolean against = true;//true computer false another player

    /**main function starts running the second the program starts running*/
    public static void main(String args[]){
        new Gomoku();
        game();
    }

    /**buttons action listener*/
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        //if restart game pressed
        if(s.equals("Restart Game")){
            restartGame();
        }
        //if undo pressed
        if(s.equals("Undo")){
            undo();
        }
        //if load pressed
        if(s.equals("Load")){
            load();
        }
        //if save pressed
        if(s.equals("Save")){
            save("");
        }
        //if submit pressed
        if(s.equals("Submit")){
            submit();
        }
        //if submit pressed
        if(s.equals("Vs Computer")){
           vsComputer();
        }
        //if submit pressed
        if(s.equals("Vs Player")){
            vsPlayer();
        }
    }

    /**restarting the game and enabling two player mode*/
    public static void vsPlayer(){
        against = false;
        restartGame();
    }

    /**restarting the game and enabling player vs computer mode*/
    public static void vsComputer(){
        against = true;
        restartGame();
    }

    /**restarting the game by clearing the board and the stack*/
    public static void restartGame(){
        for(int i=0;i<computerMoves.size();i++){
            computerMoves.remove(i);
        }
        while(!moves.isEmpty()){
            Move move = new Move();
            move = moves.pop();
            board[move.getRow()][move.getCol()] = 0;
            buttons[move.getRow()][move.getCol()].setIcon(buttons[move.getRow()][move.getCol()].Pink);
            buttons[move.getRow()][move.getCol()].addButton();
        }
        turn = 1;
        for(int i=0;i<6;i++){
            System.out.println("\n");
        }
    }


    /**remove the last move from the moves stack and the board (undoes the latest move) if against computer removes 2 last moves*/
    public static void undo(){
        if(against){
            for(int i=0;i<2;i++){
                if(!moves.isEmpty()){
                    Move move = new Move();
                    move = moves.pop();
                    board[move.getRow()][move.getCol()] = 0;
                    buttons[move.getRow()][move.getCol()].setIcon(buttons[move.getRow()][move.getCol()].Pink);
                    turn = turn%2 + 1;//updates the turn
                    buttons[move.getRow()][move.getCol()].addButton();
                    if(computerMoves.size()>0) {
                        computerMoves.remove(computerMoves.size() - 1);
                    }
                }
            }
        }
        else{
            if(!moves.isEmpty()){
                Move move = new Move();
                move = moves.pop();
                board[move.getRow()][move.getCol()] = 0;
                buttons[move.getRow()][move.getCol()].setIcon(buttons[move.getRow()][move.getCol()].Pink);
                turn = turn%2 + 1;//updates the turn
                buttons[move.getRow()][move.getCol()].addButton();
                if(computerMoves.size()>0) {
                    computerMoves.remove(computerMoves.size() - 1);
                }
            }
        }
    }


    /**opens dialog for saving the game with text field and submit button*/
    public static void save(String string){
        System.out.printf(""+string.length());
        if(string.length()!=0){
            boolean exist = createFile(string);
            System.out.println(exist);
            if(!exist) {
                Stack<Move> movesClone = createStackCopy();
                writeMoves(string,movesClone);
            }
        }
        else{
            s = new JDialog(f,"Save");
            s.setLayout(new BorderLayout());
            s.add(place,BorderLayout.NORTH);
            s.add(submit,BorderLayout.CENTER);
            s.setSize(300, 85);
            s.setLocationRelativeTo(null);
            s.setVisible(true);
        }
    }

    public static void submit(){
        boolean exist = createFile(place.getText());
        System.out.println(exist);
        if(!exist) {
            Stack<Move> movesClone = createStackCopy();
            writeMoves(place.getText(),movesClone);
            s.setVisible(false);
        }
    }

    /**creates a file.txt if not existed already by the given name*/
    public static boolean createFile(String filename){
        try {
            File myObj = new File(pathname + filename + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                JOptionPane.showMessageDialog(null, "File created: " + myObj.getName());
                return false;
            }
            else {
                System.out.println("File already exists.");
                JOptionPane.showMessageDialog(null, "File already exists.");
                return true;
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return true;
    }


    /**write the all the currents moves in the new created file*/
    public static void writeMoves(String filename,Stack<Move> moveStack){
        try {
            FileWriter myWriter = new FileWriter(pathname + filename + ".txt");
            while (!moveStack.isEmpty()) {
                Move move = new Move();
                move = moveStack.pop();
                myWriter.write(move.getRow() + "," + move.getCol() + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    /**creates a copy of the moves stack*/
    public static Stack<Move> createStackCopy(){
        Stack<Move> copy = new Stack<Move>();
        Stack<Move> copyStack = new Stack<Move>();
        while(!moves.isEmpty()){
            Move move = new Move();
            move = moves.pop();
            copy.push(move);
            copyStack.push(move);
        }
        while(!copy.isEmpty()){
            moves.push(copy.pop());
        }
        return copyStack;
    }


    /**if the button is pressed then remove the last move from the moves stack and the board (undoes the latest move)*/
    public static void load(){
        final JFileChooser j = new JFileChooser(pathname);
        int result = j.showOpenDialog(f);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = j.getSelectedFile();
            restartGame();
            System.out.println(selectedFile.getName());
            readMoves(selectedFile.getName());
        }
    }


    /**write all the currents moves in the new created file*/
    public static void readMoves(String filename){
        try {
            File myObj = new File(pathname + filename);
            Scanner myReader = new Scanner(myObj);
            turn = 1;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String [] parts = data.split(",");
                Move move = new Move(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
                if(turn==2){
                    computerMoves.add(move);
                }
                moves.push(move);
                makeATurn(turn,move.getRow(),move.getCol());
                turn = turn%2 + 1;//updates the turn
                System.out.println(data);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    /**creating visual components on the frame using two pannels one for buttons and one for game board*/
    public Gomoku(){
        f.setLayout(new BorderLayout());//makes the JFrame in a BorderLayout so i can put both panels in order one after the other
        f.setSize(1000,1010);//size of board
        f.setResizable(true);//disable the option to change the size of the board
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        menu.setLayout(new FlowLayout());//changes the layout of the menu to flow so it can include all of the necessary buttons
        menu.add(undo);
        menu.add(save);
        menu.add(load);
        menu.add(restartGame);
        menu.add(vs_computer);
        menu.add(vs_player);
        undo.addActionListener(this);
        save.addActionListener(this);
        load.addActionListener(this);
        restartGame.addActionListener(this);
        submit.addActionListener(this);
        vs_computer.addActionListener(this);
        vs_player.addActionListener(this);
        p.setLayout(new GridLayout(size,size,4,4));//the layout of the board 15x15 table
        for(int i=0;i<size;i++){//setting the buttons arry
            for(int j=0;j<size;j++) {
                buttons[i][j] = new BWButton();
                buttons[i][j].setIcon(buttons[i][j].Pink);
                p.add(buttons[i][j]);
            }
        }
        f.add(menu,BorderLayout.PAGE_START);//sets the panel of buttons in the middle
        f.add(p,BorderLayout.CENTER);//sets the panel of buttons in the center
        f.setLocationRelativeTo(null);
        f.setVisible(true);//makes the board changes appear
    }


    /**main function that is running the game*/
    public static void game(){
        //calibrating all the necessary variables
        String winner;//who is the winner
        Move lastMovePc = new Move(0,0);
        boolean ifEnded = false;//if the game ended
        boolean buttonpressed = false;//if button is pressed
        for(int i=0;i<size;i++){//empty the array of the board
            for(int j=0;j<size;j++){
                board[i][j]=0;
            }
        }

        //while loop for the game to run as long as ifEnded is true
        while(true){
            int i=0,j=0;//the row and column of the pressed button
            Move move = new Move(0,0);
            if(!against){
                move = playerMove();
            }
            else{
                if(turn == 1){
                    move = playerMove();
                }
                else {
                    move = computerMove();
                }
            }

            i = move.getRow();
            j = move.getCol();

            //System.out.println("The turn is: " + turn);

            ifEnded = checkIfWinner(turn, i, j);//if there is a winner after the turn is made
            if(ifEnded){
                //who is the winner
                if(turn == 1){
                    winner = "player";
                }
                else{
                    winner = "computer";
                }
                save(getDate());
                //display message with the winner name
                JOptionPane.showMessageDialog(null, "The winner is the: " + winner);
                restartGame();
            }
            else{
                turn = turn%2 + 1;//updates the turn
            }
            if(!isMovesLeft()){
                //display message with a tie
                JOptionPane.showMessageDialog(null, "Its a tie");
            }
        }
    }

    /**returns date as a string*/
    public static String getDate() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd-hh_mm_ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }


    /**calculate and make the best move for the computer*/
    public static Move computerMove(){
        int i=0,j=0;//the row and column of the pressed button
        Move lastmove = new Move();
        if(!moves.isEmpty()){
            lastmove = moves.peek();
        }
        Move bestMove = findBestMove(lastmove);
        makeATurn(turn, bestMove.getRow(), bestMove.getCol());
        //puts the move in the moves stack
        i = bestMove.getRow();
        j = bestMove.getCol();
        computerMoves.add(bestMove);
        moves.push(bestMove);
        return bestMove;
    }

    /**finds what move the player pressed and puts it on the board*/
    public static Move playerMove(){
        int i=0,j=0;//the row and column of the pressed button
        boolean buttonpressed = false;//if button is pressed
        while(!buttonpressed) {//wait until a button is pressed
            for (i = 0; i < size; i++) {
                for (j = 0; j < size; j++) {
                    if (buttons[i][j].getValue() == 1) {
                        buttons[i][j].setValue(2);
                        buttonpressed = true;
                        break;
                    }
                }
                if (buttonpressed) {
                    break;
                }
            }
        }
        makeATurn(turn, i , j);
        //create the new move and puts it in the moves stack
        Move move = new Move(i,j);
        moves.push(move);
        return move;
    }

    /**if moves left on board*/
    public static boolean isMovesLeft(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(board[i][j] == 0){
                    return true;
                }
            }
        }
        return false;
    }


    /**makes the turn on both the boards the visual one and the under one*/
    public static void makeATurn(int turn,int row,int column){
        if(turn==1){//makes the turn for the player
            buttons[row][column].setIcon(buttons[row][column].X);
            board[row][column] = 1;
        }
        else{//makes the turn for the computer
            buttons[row][column].setIcon(buttons[row][column].O);
            board[row][column] = 2;
        }
        buttons[row][column].removeButton();
    }


    /**checks if there is a winner after the new placed peace on the board was placed*/
    public static boolean checkIfWinner(int turn, int i, int j){
        int m=i,n=j;//m and n for shorot and hamudot
        boolean w=true,x=true,y=true,z=true;//w = shoorot , x = hamudon , y = alr , z = arl
        for(int k=0;k<4;k++){ // find the starting location of the checking
            if(m-1>=0){
                m--;
            }
            if(n-1>=0){
                n--;
            }
        }

        int a=i,b=j,c=i,d=j;//creates four points to check halahsonim (ab ac ad dc)
        for(int k=0;k<4;k++){ // find the starting location of the checking
            a--;
            b--;
            c++;
            d++;
        }

        //checks if there is even a reason to check
        //shoorot
        if(j>0 && j<14 && board[i][j-1]!=turn && board[i][j+1]!=turn){
            w=false;
        }
        else if(j>0 && board[i][j-1]!=turn){
            if (j < 11) {
                if (board[i][j] == turn && board[i][j + 1] == turn && board[i][j + 2] == turn && board[i][j + 3] == turn && board[i][j + 4] == turn) {
                    return true;
                }
            }
            w=false;
        }
        else if(j<14 && board[i][j+1]!=turn){
            if (n < 11) {
                if (board[i][n] == turn && board[i][n + 1] == turn && board[i][n + 2] == turn && board[i][n + 3] == turn && board[i][n + 4] == turn) {
                    return true;
                }
            }
            w=false;
        }
        //amudot
        if(i>0 && i<14 && board[i-1][j]!=turn && board[i+1][j]!=turn){
            x=false;
        }
        else if(i>0 && board[i-1][j]!=turn){
            if (i < 11) {
                if (board[i][j] == turn && board[i + 1][j] == turn && board[i + 2][j] == turn && board[i + 3][j] == turn && board[i + 4][j] == turn) {
                    return true;
                }
            }
            x=false;
        }
        else if(i<14 && board[i+1][j]!=turn){
            if (m < 11) {
                if (board[m][j] == turn && board[m + 1][j] == turn && board[m + 2][j] == turn && board[m + 3][j] == turn && board[m + 4][j] == turn) {
                    return true;
                }
            }
            x=false;
        }
        //alahson from left to right
        if(j>0 && j<14 && i>0 && i<14 && board[i-1][j-1]!=turn && board[i+1][j+1]!=turn){
            y=false;
        }
        else if(i>0 && j>0 && board[i-1][j-1]!=turn){
            // from bottom right position(cd)
            if (c>3 && d>3 && c<=14 && d<=14){
                if (board[c][d] == turn && board[c - 1][d - 1] == turn && board[c - 2][d - 2] == turn && board[c - 3][d - 3] == turn && board[c - 4][d - 4] == turn) {
                    return true;
                }
            }
            y=false;
        }
        else if(j<14 && i<14 && board[i+1][j+1]!=turn){
            // from top left position (ab)
            if (a<11 && b<11 && a>=0 && b>=0){
                if (board[a][b] == turn && board[a + 1][b + 1] == turn && board[a + 2][b + 2] == turn && board[a + 3][b + 3] == turn && board[a + 4][b + 4] == turn) {
                    return true;
                }
            }
            y=false;
        }
        //alahson from right to left
        if(j>0 && j<14 && i>0 && i<14 && board[i-1][j+1]!=turn && board[i+1][j-1]!=turn){
            z=false;
        }
        else if(j<14 && i>0 && board[i-1][j+1]!=turn){
            // from bottom left position (cb)
            if (c>3 && b<11 && c<=14 && b>=0) {
                if (board[c][b] == turn && board[c - 1][b + 1] == turn && board[c - 2][b + 2] == turn && board[c - 3][b + 3] == turn && board[c - 4][b + 4] == turn) {
                    return true;
                }
            }
            z=false;
        }
        else if(j>0 && i<14 && board[i+1][j-1]!=turn){
            // from top right position (ad)
            if (a<11 && d>3 && a>=0 && d<=14) {
                if (board[a][d] == turn && board[a + 1][d - 1] == turn && board[a + 2][d - 2] == turn && board[a + 3][d - 3] == turn && board[a + 4][d - 4] == turn) {
                    return true;
                }
            }
            z=false;
        }

        System.out.println("w  "+w+"  x  "+x+"  y  "+y+"  z  "+z+"  a  "+a+"  b  "+b+"  c  "+c+"  d  "+d + "  turn  " + turn);

        for(int l=0;l<5;l++){
            if(!w && !x && !y && !z){
                break;
            }

            // if win in shoorot
            if(w) {
                if (n < 11) {
                    if (board[i][n] == turn && board[i][n + 1] == turn && board[i][n + 2] == turn && board[i][n + 3] == turn && board[i][n + 4] == turn) {
                        return true;
                    }
                }
                else{
                    w=false;
                }
            }
            // if win in amudot
            if(x) {
                if (m < 11) {
                    if (board[m][j] == turn && board[m + 1][j] == turn && board[m + 2][j] == turn && board[m + 3][j] == turn && board[m + 4][j] == turn) {
                        return true;
                    }
                }
                else{
                    x=false;
                }
            }
            // if win in alahson from left to right
            if(y){
                int count = 0;
                // from top left position (ab)
                if (a<11 && b<11 && a>=0 && b>=0){
                    if (board[a][b] == turn && board[a + 1][b + 1] == turn && board[a + 2][b + 2] == turn && board[a + 3][b + 3] == turn && board[a + 4][b + 4] == turn) {
                        return true;
                    }
                }
                else{
                    count++;
                }
                // from bottom right position(cd)
                if (c>3 && d>3 && c<=14 && d<=14){
                    if (board[c][d] == turn && board[c - 1][d - 1] == turn && board[c - 2][d - 2] == turn && board[c - 3][d - 3] == turn && board[c - 4][d - 4] == turn) {
                        return true;
                    }
                }
                else{
                    count++;
                }
                if(count==2){
                    y=false;
                }
            }

            // if win in alahson from right to left
            if(z){
                int count = 0;
                // from top right position (ad)
                if (a<11 && d>3 && a>=0 && d<=14) {
                    if (board[a][d] == turn && board[a + 1][d - 1] == turn && board[a + 2][d - 2] == turn && board[a + 3][d - 3] == turn && board[a + 4][d - 4] == turn) {
                        return true;
                    }
                }
                else{
                    count++;
                }

                // from bottom left position (cb)
                if (c>3 && b<11 && c<=14 && b>=0) {
                    if (board[c][b] == turn && board[c - 1][b + 1] == turn && board[c - 2][b + 2] == turn && board[c - 3][b + 3] == turn && board[c - 4][b + 4] == turn) {
                        return true;
                    }
                }
                else{
                    count++;
                }
                if(count==2){
                    y=false;
                }
            }

            m++;
            n++;
            a++;
            b++;
            c--;
            d--;
        }
        return false;
    }










    //ai stuff under here
    private static String [] winCondition = {"xxxx-","-xxxx", "xx-xx", "x-xxx","xxx-x",
    "-xxx--","--xxx-","-x-xx-","-xx-x-",
    "-xx---","--xx-","-x-x--","--x-x-","-x--x-",
    "-x---","--x--","---x-"};//possible win conditions
    private static int [] winConditionPosition = {4,0,2,1,4,    4,1,2,3,     3,1,2,3,2      ,2,1,2};//positions for countering winning
    /**finds the best possible moves to place for the computer*/
    public static Move findBestMove(Move lastMove){
        String currentwc;
        for(int i = 0; i < winCondition.length;i++){
            currentwc = winCondition[i];
            for(int j=0;j<currentwc.length();j++){
                if(i<=4 && computerMoves.size()>4){
                    for (int w = 0; w < computerMoves.size(); w++) {
                        Move move = checkIfPosition(computerMoves.get(w), currentwc, winConditionPosition[i],j,2);
                        if(move.getRow()!=20 && move.getCol()!=20){
                            System.out.println(currentwc);
                            System.out.println(move.toString());
                            return move;
                        }
                    }
                }
                if(i==9){
                    for(int r=5;r<=8;r++) {
                        for (int w = 0; w < computerMoves.size(); w++) {
                            Move move = checkIfPosition(computerMoves.get(w), winCondition[r], winConditionPosition[r], j, 2);
                            if (move.getRow() != 20 && move.getCol() != 20) {
                                System.out.println(currentwc);
                                System.out.println(move.toString());
                                return move;
                            }
                        }
                    }
                }
                if(i==14){
                    for(int r=9;r<=13;r++) {
                        for (int w = 0; w < computerMoves.size(); w++) {
                            Move move = checkIfPosition(computerMoves.get(w), winCondition[r], winConditionPosition[r], j, 2);
                            if (move.getRow() != 20 && move.getCol() != 20) {
                                System.out.println(currentwc);
                                System.out.println(move.toString());
                                return move;
                            }
                        }
                    }
                }
                Move move = checkIfPosition(lastMove, currentwc, winConditionPosition[i],j,1);
                if(move.getRow()!=20 && move.getCol()!=20){
                    System.out.println(currentwc);
                    System.out.println(move.toString());
                    return move;
                }
            }
        }
        return getPossibleMove(lastMove);
    }

    /**finds the best moves given the current string and the position we are in if there is none it returns move 20 20*/
    public static Move checkIfPosition(Move lastMove,String string,int position, int letter,int who){//who = 1 for counterplay 2 for winning
        int i = lastMove.getRow(), j = lastMove.getCol();
        Move bestMove = new Move(lastMove.getRow(), lastMove.getCol());
        int m=i,n=j;//m and n for shorot and hamudot
        for(int k=0;k<letter;k++){ // find the starting location of the checking
            if(m-1>=0){
                m--;
            }
            if(n-1>=0){
                n--;
            }
        }

        int a=i,b=j,c=i,d=j;//creates four points to check halahsonim (ab ac ad dc)
        for(int k=0;k<letter;k++){ // find the starting location of the checking
            a--;
            b--;
            c++;
            d++;
        }

        //System.out.println(a + " " + b + " " + c + " " + d);

        // if win in shoorot
        if (n < size-string.length()+1) {
            int count = 0;
            for(int e=0;e<string.length();e++){
                if(string.charAt(e)=='-'){
                    if(board[i][n+e] == 0){
                        count++;
                    }
                }
                else{
                    if(board[i][n+e] == who){
                        count++;
                    }
                }
            }
            if(count==string.length()){
                bestMove.setRow(i);
                bestMove.setCol(n+position);
                return bestMove;
            }
        }

        // if win in amudot
        if (m < size-string.length()+1) {
            int count = 0;
            for(int e=0;e<string.length();e++){
                if(string.charAt(e)=='-'){
                    if(board[m+e][j] == 0){
                        count++;
                    }
                }
                else{
                    if(board[m+e][j] == who){
                        count++;
                    }
                }
            }
            if(count==string.length()){
                bestMove.setRow(m+position);
                bestMove.setCol(j);
                return bestMove;
            }
        }

        // if win in alahson from left to right
        // from top left position (ab)
        if (a<size-string.length()+1 && b<size-string.length()+1 && a>=0 && b>=0) {
            int count = 0;
            for(int e=0;e<string.length();e++){
                if(string.charAt(e)=='-'){
                    if(board[a+e][b+e] == 0){
                        count++;
                    }
                }
                else{
                    if(board[a+e][b+e] == who){
                        count++;
                    }
                }
            }
            if(count==string.length()){
                bestMove.setRow(a+position);
                bestMove.setCol(b+position);
                return bestMove;
            }
        }
        // from bottom right position(cd)
        if (c>string.length()-2 && d>string.length()-2 && c<=14 && d<=14) {
            int count = 0;
            for(int e=0;e<string.length();e++){
                if(string.charAt(e)=='-'){
                    if(board[c-e][d-e] == 0){
                        count++;
                    }
                }
                else{
                    if(board[c-e][d-e] == who){
                        count++;
                    }
                }
            }
            if(count==string.length()){
                bestMove.setRow(c-position);
                bestMove.setCol(d-position);
                return bestMove;
            }
        }

        // if win in alahson from right to left
        // from top right position (ad)
        if (a<size-string.length()+1 && d>string.length()-2 && a>=0 && d<=14) {
            int count = 0;
            for(int e=0;e<string.length();e++){
                if(string.charAt(e)=='-'){
                    if(board[a+e][d-e] == 0){
                        count++;
                    }
                }
                else{
                    if(board[a+e][d-e] == who){
                        count++;
                    }
                }
            }
            if(count==string.length()){
                bestMove.setRow(a+position);
                bestMove.setCol(d-position);
                return bestMove;
            }
        }
        // from bottom left position (cb)
        if (c>string.length()-2 && b<size-string.length()+1 && c<=14 && b>=0) {
            int count = 0;
            for(int e=0;e<string.length();e++){
                if(string.charAt(e)=='-'){
                    if(board[c-e][b+e] == 0){
                        count++;
                    }
                }
                else{
                    if(board[c-e][b+e] == who){
                        count++;
                    }
                }
            }
            if(count==string.length()){
                bestMove.setRow(c-position);
                bestMove.setCol(b+position);
                return bestMove;
            }
        }

        Move move = new Move(20,20);
        return move;
    }



    /**if we didnt find good move by conditions we looking for the closest move possible*/
    public static Move getPossibleMove(Move lastMove){
        Move move = new Move(0,0);
        int i = lastMove.getRow(), j = lastMove.getCol();

        int a=i-1,b=j-1,c=i+1,d=j+1;//creates four points to check halahsonim (ab ac ad dc)

        move = calculateMove(move,a,b,c,d,3);
        if(move.getCol()!=0 && move.getRow()!=0){
            //System.out.println("1");
            return move;
        }
        move = calculateMove(move,a-1,b-1,c+1,d+1,4);
        if(!move.equals(move)){
            //System.out.println("2");
            return move;
        }
        move = calculateMove(move,a-2, b-2,c+2,d+2,5);
        if(!move.equals(move)){
            //System.out.println("3");
            return move;
        }


        for (int q = 0; q < size; q++) {
            for (int w = 0; w < size; w++) {
                if(board[q][w]==0){
                    move.setRow(q);
                    move.setCol(w);
                    return move;
                }
            }
        }
        return move;
    }

    public static Move calculateMove(Move move,int a,int b,int c,int d,int num){
        for (int w = 0; w < num; w++) {
            if(a>=0 && b+w>=0 && a<=14 && b+w<=14) {
                if (board[a][b + w] == 0) {
                    move.setRow(a);
                    move.setCol(b + w);
                    return move;
                }
            }
            if(a>=0 && b>=0 && a<=14 && b+w<=14) {
                if (board[a][b + w] == 0) {
                    move.setRow(a);
                    move.setCol(b + w);
                    return move;
                }
            }
            if(c>=0 && d-w>=0 && c<=14 && d<=14) {
                if (board[c][d - w] == 0) {
                    move.setRow(c);
                    move.setCol(d - w);
                    return move;
                }
            }
            if(c-w>=0 && d>=0 && c<=14 && d<=14) {
                if (board[c-w][d] == 0) {
                    move.setRow(c-w);
                    move.setCol(d);
                    return move;
                }
            }
        }
        return move;
    }
}

