//components: chart of results, restarting,gameover board,game board,sanke,food,moving,hit the war/body, eating,score board 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;// windowed components
import java.util.ArrayList;//import array to keep scores in each game
import java.util.Random;//import the "random" to randomly assign the food 

public class SnakeGame extends JPanel implements ActionListener, KeyListener 
{//creat snakegmaae to creat windows 
    static final int W = 800; //width of the window
    static final int H = 800; //length of the window
    static final int UnitSize = 25; // every suqres have 25 sidelength 
    static final int UA = UnitSize * UnitSize;//Unit Area 
    int[] x = new int[W * H / UA];// set the X coordinates (# of squares in x-axis)
    int[] y = new int[W * H / UA];// set the y coordinates (# of squares in y-axis)  

    int body = 4; //set the initial body length
    int foodX = 0;//set the x coordinate of food
    int foodY = 0;//set the y coordinate of food 
    int score = 0;//set the initial score
    char dir = 'R';//set the initial direction of the snake 
    int speed = 100;
    boolean over = false;//set the game attitude (processing or finished) 

    Timer timer = new Timer(speed, this);// creat the new timer ( the speed of the snake 100ms/shift) 
    Random rand = new Random();//creat the new random to assign the x and y axis of food

    static ArrayList<Integer> scores = new ArrayList<Integer>();//creat a new array to keep score 
   
    public SnakeGame() 
    { // starts a new window and starts a game 
        setPreferredSize(new Dimension(W, H));//set the size of the window 
        setBackground(Color.black);//set the background color of the window 
        setFocusable(true);//set a foucus to ensure every keyboard inputs only impact the game 
        addKeyListener(this);//get the keyboard inputs
        start();//start the game 
    }

    public void start() 
    {//error at first time 
        body = 4;  
        score = 0;
        dir = 'R';
        over = false; 
        //recover the attitude 

        for (int i = 0; i < body; i++) 
        {
            x[i] = 100 - i * UnitSize;
            y[i] = 100;
        }//recover the location of each part of bodys 

        newFood();//recover the spawning of foods 
        timer = new Timer(speed,this);// creat a new timer 
        timer.start();//start moving 
    }

    public void newFood() 
    {
        foodX = rand.nextInt(W / UnitSize) * UnitSize;//put the food on the each squares x coordinate   
        foodY = rand.nextInt(H / UnitSize) * UnitSize;//put the food on the each squares y coordinate 
    }

    public void actionPerformed(ActionEvent e) 
    {
        if (over == false) // if the atiitude is "processing", to activate functions below 
        {
            move();//moving 
            eat();//eating (plus scores) 
            checkHit();//check wether the snake hits the food 
        }
        repaint();//redrow the board (error at first)*****************
    }

    public void move() 
    {
        for (int i = body; i > 0; i--) 
        {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        } 
        // delet the one square of body once it moves one step 

        if (dir == 'R')
        {
            x[0] += UnitSize;
        }// when direction == right, move to right(x++)
        if (dir == 'L')
        {
            x[0] -= UnitSize;
        }// when direction == left, move to left(x--)
        if (dir == 'U') 
        {
            y[0] -= UnitSize;
        }// when direction == up, move to right(y++)

        if (dir == 'D') 
        {
            y[0] += UnitSize;
        }// when direction == up, move to right(y--)
        
    }
    public void eat() 
    {
        if (x[0] == foodX && y[0] == foodY) 
        //if the y of body == x of body 
        {
            body++ ;//body # + 1 
            score++;//score # + 1 
            newFood();//creat new food 
        }
    }

    public void checkHit() 
    {
        if (x[0] < 0 || x[0] >= W || y[0] < 0 || y[0] >= H) 
        {
            end();
            //when the snake hit the wall , activate the end process 
        } 

        for (int i = 1; i < body; i++) 
        {
            if (x[0] == x[i] && y[0] == y[i]) 
            {
               end();
            }//when the snake hit the body , activate the end process
        }
    }

    public void end() 
    {
        over = true;//set the end attitude == ture 
        timer.stop();//stop moving 
        scores.add(score);//keep the score in the score array to draw the graph 
    }

    public void paintComponent(Graphics g) //dispay the game 
    {
        super.paintComponent(g);//delet the original board 

        if (over == true) //if the the game over 
        {
           drawGameOver(g);
        }// drow gameover page
        else drawGame(g);
        // drow the game page 
    }

    public void drawGame(Graphics g) 
    {
        g.setColor(Color.white);// set the sentence color 
        g.setFont(new Font("Arial", Font.BOLD, 20));//set the form of text
        g.drawString("Score: " + score, 20, 30);//score board
        g.drawString("control:w = up,A = left, D = right, s = down",300, 30);//tips 

        g.setColor(Color.yellow);//set the color of food
        g.fillRect(foodX, foodY, UnitSize, UnitSize);//drow the food 

        for (int i = 0; i < body; i++) 
        {
            if (i == 0) 
            {
               g.setColor(Color.green);
            }//set the color of body head

            else g.setColor(new Color(0, 180, 0)); 
            //difficulty 2 

            g.fillRect(x[i], y[i], UnitSize, UnitSize);
            //drow the bodies
        }

    }

    public void drawGameOver(Graphics g) 
    {
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over", 300, 70);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Your final score is: " + score, 320, 110);
        g.drawString("Press SPACE[-] to restart in default mode", 300, 145);//tips
        g.drawString("Press 'F' to restart in hard difficulty", 300, 170);
        g.drawString("Press 'G' to restart in easy difficulty", 300, 195);

        drawChart(g, 175, 225, 460, 340);//gameover page(drawing a chart to keep scores)
    }

    public void drawChart(Graphics g, int cx, int cy, int cw, int ch) //////////////////////////////////////////////hard part///////////////////////////////// 
    {//set the two points ( upleft and down right ) 
        g.setColor(Color.white);//set the chart color
        g.drawRect(cx, cy, cw, ch);//draw the chart

        
        g.drawString("Score History", cx-125, cy + 25);//put the tittle above the chart 

        if (scores.size() == 0)
           return;//if there's no values, end this method


        int max = 1;
        for (int s : scores) {
            if (s > max) 
            {
               max = s;
            }
        }//find the max value 

        int oldX = 0, oldY = 0;// save the location of the last point 

        for (int i = 0; i < scores.size(); i++) 
        {//draw ou all points in the array 
            int px;//calculate the x-coordinate of the new point 

            if (scores.size() == 1) //if only 1 elements in the array 
            {
               px = cx + cw / 2;//drow the point in the center of the chart 
            }
            else 
            {
               int totalPoints = scores.size();  // find the # of the games through finding the size of the array(element #)         
               int gap = cw / (totalPoints - 1); //find the distance between points          
               px = cx + i * gap;// find the x of the new point (add 1 distance)                         
            }

            int scoreNow = scores.get(i);  //get the current score       
            int chartBottom = ch+cy; //get the bottom of the scores          
            int height = scoreNow * ch / max; //get the height of this point 
            int py = chartBottom - height;// set the y coordinate of the point 

            g.fillOval(px - 4, py - 4, 8, 8);//set the points in the chart 

            if (i > 0) // drow lines between 
            {
                g.drawLine(oldX, oldY, px, py); //drow lines between two points 
            }

            
            g.drawString("" + scores.get(i), px + 5, py - 5);//drow the number besides the points 

            oldX = px;// set the new point as the old point 
            oldY = py;// set the new point as the old point
        }
    }
//////////////////////////////////////////////////////////////////////////Chart/////////////////////////////////////
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();//set k as the pressed key 

        if (over && k == KeyEvent.VK_SPACE) 
        {   
            speed = 100;
            start();//restart the game 
            return;
        }
        if (over && k == KeyEvent.VK_F) 
        {
            speed = 45;
            start();//restart the game (hard) 
            return;
        }
        if (over && k == KeyEvent.VK_G) 
        {
            speed = 155;
            start();//restart the game (easy)
            return;
        }
 


        if (k == KeyEvent.VK_D && dir != 'L') 
           dir = 'R';//D = Left
        if (k == KeyEvent.VK_A && dir != 'R') 
           dir = 'L';//L = right
        if (k == KeyEvent.VK_W && dir != 'D') 
           dir = 'U';//W = up 
        if (k == KeyEvent.VK_S && dir != 'U') 
           dir = 'D';//S = down 
           //baned the opposite direction 
    }

    public void keyReleased(KeyEvent e) {}//null
    public void keyTyped(KeyEvent e) {}//null
//************************************************************************* keyboard listener 
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");//set the window name
        frame.add(new SnakeGame());//creat the new window
        frame.pack();//set the window to aprropriate size 
        frame.setLocationRelativeTo(null); //set the window in the center of the screen 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//close the window 
        frame.setVisible(true);//display the window 
    }
}