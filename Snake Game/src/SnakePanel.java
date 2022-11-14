import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;


public class SnakePanel extends JPanel implements ActionListener{
    //Fixed size of the canvas and unit

    static  final int width = 1200;
    static  final int height = 600;
    static  final int unit_size = 50;

    // Random variable for random food spawns

    Random random;

    // Coordinates of food

    int foodX;
    int foodY;
    int foodeaten =0;

    int body =3;

    boolean game_flag = false;
    char dir ='R';



    //Number of units
    static final int size =(width*height)/(unit_size*unit_size);

    final int x_snake[] = new int[size];
    final int y_snake[] = new int[size];

    // To decide the refresh rate of the snake
    Timer timer;
    static final int DELAY = 1000;

    SnakePanel() {

        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.addKeyListener(new Mykey());
        this.setFocusable(true);
        random = new Random();

        Game_Start();

    }

    public void Game_Start(){
        game_flag = true;
        timer = new Timer(DELAY,this);
        timer.start();
        spawnfood();
    }



    public void paintComponent(Graphics graphic){
        super.paintComponent((graphic));
        draw(graphic);

    }
    public void draw (Graphics graphic){
        //Setting the graphics for the food block
        if(game_flag){
            graphic.setColor(Color.red);
            graphic.fillOval(foodX,foodY,unit_size,unit_size);

            //Setting the graphics for the snake
            for(int i=0;i<body;i++){
                if(i==0){
                    graphic.setColor(Color.yellow);
                    graphic.fillRect(x_snake[i],y_snake[i],unit_size,unit_size);
                }
                else{
                    graphic.setColor(Color.green);
                    graphic.fillRect(x_snake[i],y_snake[i],unit_size,unit_size);
                }
            }

            graphic.setColor(Color.red);
            graphic.setFont(new Font("Comic sans", Font.BOLD,40));
            FontMetrics font_me = getFontMetrics(graphic.getFont());
            graphic.drawString("Score:"+foodeaten,(width-font_me.stringWidth("Score:"+foodeaten))/2,graphic.getFont().getSize());

        }
        else{
            gameOver(graphic);

        }
    }

    public void move(){

        // Updating the whole apart from its head

        for(int i=body;i>0;i--) {
            x_snake[i] = x_snake[i - 1];
            y_snake[i] = y_snake[i - 1];

        }

            switch(dir){
                case 'U':
                    y_snake[0] = y_snake[0] - unit_size;
                    break;
                case 'L':
                    x_snake[0] = x_snake[0] - unit_size;
                    break;
                case 'D':
                    y_snake[0] = y_snake[0] + unit_size;
                    break;
                case 'R':
                    x_snake[0] = x_snake[0] + unit_size;
                    break;

            }

    }

    public void gameOver(Graphics graphic){
        // To display the score
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics font_me = getFontMetrics(graphic.getFont());
        graphic.drawString("Score:"+foodeaten,(width-font_me.stringWidth("Score:"+foodeaten))/2,graphic.getFont().getSize());


        //To display the game over text
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,80));
        FontMetrics font_me1 = getFontMetrics(graphic.getFont());
        graphic.drawString("GAME OVER!",(width-font_me1.stringWidth("GAME OVER!"))/2,height/2);

        // Prompt to replay
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics font_me2 = getFontMetrics(graphic.getFont());
        graphic.drawString("Press R to replay",(width-font_me2.stringWidth("Press R to replay"))/2,height/2-150);

    }

    public void spawnfood(){

        foodX = random.nextInt((int)(width/unit_size)) * unit_size;
        foodY = random.nextInt((int)(height/unit_size)) * unit_size;
    }



    public void checkhit(){
        // Check if the snake has hit itself or the wall

        for(int i=body;i>0;i--){
            if(x_snake[0] == x_snake[i] && y_snake[0] == y_snake[i]){
                game_flag = false;
            }
        }
        if(x_snake[0]<0){ // Snake hits left end
            game_flag = false;
        }
        else if(x_snake[0]>width){
            game_flag = false;
        }
        else if(y_snake[0]<0){
            game_flag = false;
        }
        else if (y_snake[0]>height) {
            game_flag = false;
        }

        if(game_flag ==false){
            timer.stop();
        }

    }

    public void eat(){
        if(x_snake[0] == foodX && y_snake[0]== foodY){
            body++;  // Increase the snake length
            foodeaten++; // Increase the score
            spawnfood();
        }
    }



   public class Mykey extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(dir!='R'){
                        dir ='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(dir!='L'){
                        dir ='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(dir!='D'){
                        dir ='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(dir!='U'){
                        dir ='D';
                    }
                    break;

                case KeyEvent.VK_R:
                    if(!game_flag){
                        foodeaten = 0;
                        body =3;
                        dir ='R';
                        Arrays.fill(x_snake,0);
                        Arrays.fill(y_snake,0);
                        Game_Start();
                    }
                    break;
            }

        }
   }

    // To make sure we use this function instead of any other func with the same name in parent class (JPanel)
   @Override
   public void actionPerformed(ActionEvent arg0){

       if (game_flag) {
           move();
           eat();
           checkhit();

       }
       repaint();

    }
}
