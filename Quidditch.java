//name: Annlee Li
//date: Jan 20, 2014
//description: quidditch field flyer simulation
// The "Quidditch" class.
import java.awt.*;
import hsa.Console;

public class Quidditch
{
    static Console c;           // The output console

    public static final double L = 60;  //length per line segment

    public static int C = 60;       //number of lines for each circle
    public static int N = 9 * C;
    static final double ZV = 400;

    static double SPEED = 3;
    static double DELTA_SPEED = 1;
    
    static final int TIME_DELAY = 50;

    static final double DELTA_ANGLE = 0.1;      //change in angle per rotation

    static final int SCREEN_C = 150;            //colums and rows
    static final int SCREEN_R = 30;

    static final double SCREEN_X = SCREEN_C * 8;    //convert to rows and columns
    static final double SCREEN_Y = SCREEN_R * 20;

    static final double SHIFT_X = SCREEN_X / 2.0;
    static final double SHIFT_Y = SCREEN_Y / 2.0;

    static final double SCALE_X = +1.0;
    static final double SCALE_Y = -1.0;
    static final double DELTA_SCALE = 0.1;

    //direction keys
    static final char SHIFT_UP = 'w';
    static final char SHIFT_DOWN = 's';
    static final char SHIFT_LEFT = 'a';
    static final char SHIFT_RIGHT = 'd';

    //zoom keys
    static final char ZOOM_IN = '=';
    static final char ZOOM_OUT = '-';
    static final char ZOOM_STOP = '0';
    static final char CHANGE_DIR = 'p';

    static final double INFINITY = 1e+3;

    //pitch values
    static final double PITCH_OFFSET = -100;

    //pole values
    static final double POLE_POS = 3.5;
    static final double POLE_DIS = 0.5;

    static final double POLE_TALL = 0.7;
    static final double POLE_MED = 0.4;
    static final double POLE_SHORT = 0.2;

    static final double HOOP = 0.2;

    //cube values
    static final double CUBE_X_OFFSET = 0;
    static final double CUBE_Y_OFFSET = -100;
    static final double CUBE_SCALE = 3.5;


    public static void main (String[] args)
    {
	c = new Console (SCREEN_R, SCREEN_C);

	Quidditch s = new Quidditch ();

	//initial values
	double x0[] = new double [N];
	double y0[] = new double [N];
	double z0[] = new double [N];

	double x[] = new double [N];
	double y[] = new double [N];
	double z[] = new double [N];

	char key;

	double x_rotate = 0, y_rotate = 0;

	//inital coordinates
	s.setCoordinates (x0, y0, z0);

	//a welcome message
	c.println ("Welcome to my Quidditch Simulator");
	c.println ("Please enjoy your time in the sky");
	c.println ();

	//instructions
	c.println ("Controls: ");
	c.println ("w = up; s = down; a = left; d = right");
	c.println ("+ = speed up; - = slow down; 0 = stop; p = reverse");
	c.println ();

	c.println ();
	c.println ("Press any key to continue....");

	key = c.getChar ();

	while (true)
	{
	    if (c.isCharAvail ())   //when there is a key press
	    {
		key = c.getChar ();

		if (key == SHIFT_LEFT)
		    s.rotate_x (x, x0, y, y0, z, z0, +DELTA_ANGLE);     //turn left

		else if (key == SHIFT_RIGHT)
		    s.rotate_x (x, x0, y, y0, z, z0, -DELTA_ANGLE);     //turn right

		else if (key == SHIFT_UP)
		    s.rotate_y (x, x0, y, y0, z, z0, -DELTA_ANGLE);     //tilt up

		else if (key == SHIFT_DOWN)
		    s.rotate_y (x, x0, y, y0, z, z0, +DELTA_ANGLE);     //tilt down

		if (key == ZOOM_IN)         //increase speed
		    DELTA_SPEED += 1;

		else if (key == ZOOM_OUT)   //decrease speed
		    DELTA_SPEED -= 1;

		else if (key == ZOOM_STOP)  //stop
		    DELTA_SPEED *= 0;

		else if (key == CHANGE_DIR) //direction
		    DELTA_SPEED *= -1;
	    }


	    s.advance (x, x0, y, y0, z, z0);    //advance the world
	    s.perspective (x, y, z, ZV);        //add perspective to world

	    s.drawWorld (x, y, Color.black);    //draw the world

	    c.println ("SPEED = " + DELTA_SPEED);     //displays user speed

	    //time delay
	    try{Thread.sleep (TIME_DELAY);}
	    catch (InterruptedException e){}

	    c.clear ();     //clear screen

	    SPEED += DELTA_SPEED;       //increase speed

	}

    } // main method


    public static void setCoordinates (double x0[], double y0[], double z0[])
    {
	//field
	for (int i = 0 ; i <= C - 1 ; ++i)
	{
	    x0 [i] = 2 * L * Math.cos (2 * Math.PI * i / C);
	    y0 [i] = -L;
	    z0 [i] = PITCH_OFFSET + 4 * L * Math.sin (2 * Math.PI * i / C);
	}

	//centre circle
	for (int i = 0 ; i <= C - 1 ; ++i)
	{
	    x0 [C + i] = 0.75 * L * Math.cos (2 * Math.PI * i / C);
	    y0 [C + i] = -L;
	    z0 [C + i] = PITCH_OFFSET + 0.75 * L * Math.sin (2 * Math.PI * i / C);
	}

	//near tall hoop
	for (int i = 0 ; i <= C - 1 ; ++i)
	{
	    x0 [2 * C + i] = HOOP * L * Math.cos (2 * Math.PI * i / C);
	    y0 [2 * C + i] = HOOP * L + POLE_TALL * L + HOOP * L * Math.sin (2 * Math.PI * i / C);
	    z0 [2 * C + i] = PITCH_OFFSET + POLE_POS * L;
	}

	//far tall hoop
	for (int i = 0 ; i <= C - 1 ; ++i)
	{
	    x0 [3 * C + i] = HOOP * L * Math.cos (2 * Math.PI * i / C);
	    y0 [3 * C + i] = HOOP * L + POLE_TALL * L + HOOP * L * Math.sin (2 * Math.PI * i / C);
	    z0 [3 * C + i] = PITCH_OFFSET - POLE_POS * L;
	}

	//near left hoop
	for (int i = 0 ; i <= C - 1 ; ++i)
	{
	    x0 [4 * C + i] = HOOP * L * Math.cos (2 * Math.PI * i / C) + POLE_DIS * L;
	    y0 [4 * C + i] = HOOP * L + POLE_SHORT * L + HOOP * L * Math.sin (2 * Math.PI * i / C);
	    z0 [4 * C + i] = PITCH_OFFSET + POLE_POS * L;
	}

	//near right hoop
	for (int i = 0 ; i <= C - 1 ; ++i)
	{
	    x0 [5 * C + i] = HOOP * L * Math.cos (2 * Math.PI * i / C) - POLE_DIS * L;
	    y0 [5 * C + i] = HOOP * L + POLE_MED * L + HOOP * L * Math.sin (2 * Math.PI * i / C);
	    z0 [5 * C + i] = PITCH_OFFSET + POLE_POS * L;
	}
	//far left hoop
	for (int i = 0 ; i <= C - 1 ; ++i)
	{
	    x0 [6 * C + i] = HOOP * L * Math.cos (2 * Math.PI * i / C) - POLE_DIS * L;
	    y0 [6 * C + i] = HOOP * L + POLE_SHORT * L + HOOP * L * Math.sin (2 * Math.PI * i / C);
	    z0 [6 * C + i] = PITCH_OFFSET - POLE_POS * L;
	}

	//far right hoop
	for (int i = 0 ; i <= C - 1 ; ++i)
	{
	    x0 [7 * C + i] = HOOP * L * Math.cos (2 * Math.PI * i / C) + POLE_DIS * L;
	    y0 [7 * C + i] = HOOP * L + POLE_MED * L + HOOP * L * Math.sin (2 * Math.PI * i / C);
	    z0 [7 * C + i] = PITCH_OFFSET - POLE_POS * L;
	}

	//midline
	x0 [8 * C + 0] = -2 * L;
	y0 [8 * C + 0] = -L;
	z0 [8 * C + 0] = PITCH_OFFSET + 0;

	x0 [8 * C + 1] = +2 * L;
	y0 [8 * C + 1] = -L;
	z0 [8 * C + 1] = PITCH_OFFSET + 0;

	//near tall pole
	x0 [8 * C + 4] = -0;       //positiion
	y0 [8 * C + 4] = -L;       //length
	z0 [8 * C + 4] = PITCH_OFFSET + POLE_POS * L; //farness

	x0 [8 * C + 5] = +0;
	y0 [8 * C + 5] = +POLE_TALL * L;
	z0 [8 * C + 5] = PITCH_OFFSET + POLE_POS * L;

	//far tall pole
	x0 [8 * C + 6] = +0;
	y0 [8 * C + 6] = -L;
	z0 [8 * C + 6] = PITCH_OFFSET - POLE_POS * L;

	x0 [8 * C + 7] = +0;
	y0 [8 * C + 7] = +POLE_TALL * L;
	z0 [8 * C + 7] = PITCH_OFFSET - POLE_POS * L;

	//near left pole
	x0 [8 * C + 8] = +POLE_DIS * L;
	y0 [8 * C + 8] = -L;
	z0 [8 * C + 8] = PITCH_OFFSET + POLE_POS * L;

	x0 [8 * C + 9] = +POLE_DIS * L;
	y0 [8 * C + 9] = +POLE_SHORT * L;
	z0 [8 * C + 9] = PITCH_OFFSET + POLE_POS * L;

	//near right pole
	x0 [8 * C + 10] = -POLE_DIS * L;
	y0 [8 * C + 10] = -L;
	z0 [8 * C + 10] = PITCH_OFFSET + POLE_POS * L;

	x0 [8 * C + 11] = -POLE_DIS * L;
	y0 [8 * C + 11] = +POLE_MED * L;
	z0 [8 * C + 11] = PITCH_OFFSET + POLE_POS * L;

	//far left pole
	x0 [8 * C + 14] = -POLE_DIS * L;
	y0 [8 * C + 14] = -L;
	z0 [8 * C + 14] = PITCH_OFFSET - POLE_POS * L;

	x0 [8 * C + 15] = -POLE_DIS * L;
	y0 [8 * C + 15] = +POLE_SHORT * L;
	z0 [8 * C + 15] = PITCH_OFFSET - POLE_POS * L;

	//far right pole
	x0 [8 * C + 12] = +POLE_DIS * L;
	y0 [8 * C + 12] = -L;
	z0 [8 * C + 12] = PITCH_OFFSET - POLE_POS * L;

	x0 [8 * C + 13] = +POLE_DIS * L;
	y0 [8 * C + 13] = +POLE_MED * L;
	z0 [8 * C + 13] = PITCH_OFFSET - POLE_POS * L;

	//bounding cube
	x0 [8 * C + 20 + 0] = CUBE_X_OFFSET + CUBE_SCALE * L;
	y0 [8 * C + 20 + 0] = +CUBE_SCALE * L;
	z0 [8 * C + 20 + 0] = CUBE_Y_OFFSET + 2 * CUBE_SCALE * L;

	x0 [8 * C + 20 + 1] = CUBE_X_OFFSET - CUBE_SCALE * L;
	y0 [8 * C + 20 + 1] = -CUBE_SCALE * L;
	z0 [8 * C + 20 + 1] = CUBE_Y_OFFSET - 2 * CUBE_SCALE * L;

	x0 [8 * C + 20 + 2] = CUBE_X_OFFSET - CUBE_SCALE * L;
	y0 [8 * C + 20 + 2] = +CUBE_SCALE * L;
	z0 [8 * C + 20 + 2] = CUBE_Y_OFFSET + 2 * CUBE_SCALE * L;

	x0 [8 * C + 20 + 3] = CUBE_X_OFFSET + CUBE_SCALE * L;
	y0 [8 * C + 20 + 3] = -CUBE_SCALE * L;
	z0 [8 * C + 20 + 3] = CUBE_Y_OFFSET - 2 * CUBE_SCALE * L;

	x0 [8 * C + 20 + 4] = CUBE_X_OFFSET - CUBE_SCALE * L;
	y0 [8 * C + 20 + 4] = -CUBE_SCALE * L;
	z0 [8 * C + 20 + 4] = CUBE_Y_OFFSET + 2 * CUBE_SCALE * L;

	x0 [8 * C + 20 + 5] = CUBE_X_OFFSET + CUBE_SCALE * L;
	y0 [8 * C + 20 + 5] = +CUBE_SCALE * L;
	z0 [8 * C + 20 + 5] = CUBE_Y_OFFSET - 2 * CUBE_SCALE * L;

	x0 [8 * C + 20 + 6] = CUBE_X_OFFSET + CUBE_SCALE * L;
	y0 [8 * C + 20 + 6] = -CUBE_SCALE * L;
	z0 [8 * C + 20 + 6] = CUBE_Y_OFFSET + 2 * CUBE_SCALE * L;

	x0 [8 * C + 20 + 7] = CUBE_X_OFFSET - CUBE_SCALE * L;
	y0 [8 * C + 20 + 7] = +CUBE_SCALE * L;
	z0 [8 * C + 20 + 7] = CUBE_Y_OFFSET - 2 * CUBE_SCALE * L;

    }


    //rotate around x axis
    public static void rotate_x (double x[], double x0[], double y[], double y0[], double z[], double z0[], double x_rotate)
    {
	for (int i = 0 ; i <= N - 1 ; ++i)
	{
	    x [i] = x0 [i];
	    y [i] = y0 [i];
	    z [i] = z0 [i];

	    x0 [i] = x [i] * Math.cos (x_rotate) - (z [i]) * Math.sin (x_rotate);
	    y0 [i] = y [i];
	    z0 [i] = x [i] * Math.sin (x_rotate) + (z [i]) * Math.cos (x_rotate);
	}
    }


    //rotate around y axis
    public static void rotate_y (double x[], double x0[], double y[], double y0[], double z[], double z0[], double y_rotate)
    {
	for (int i = 0 ; i <= N - 1 ; ++i)
	{
	    x [i] = x0 [i];
	    y [i] = y0 [i];
	    z [i] = z0 [i];

	    x0 [i] = x [i];
	    y0 [i] = y [i] * Math.cos (y_rotate) - (z [i]) * Math.sin (y_rotate);
	    z0 [i] = y [i] * Math.sin (y_rotate) + (z [i]) * Math.cos (y_rotate);
	}
    }


    //moves the world
    public static void advance (double x[], double x0[], double y[], double y0[], double z[], double z0[])
    {
	for (int i = 0 ; i <= N - 1 ; ++i)
	{
	    x [i] = x0 [i];
	    y [i] = y0 [i];
	    z [i] = z0 [i] + SPEED;
	}
    }


    public static void perspective (double x[], double y[], double z[], double ZV)
    {

	for (int i = 0 ; i <= N - 1 ; ++i)
	{
	    if (z [i] < ZV)
	    {
		x [i] = x [i] / (1 - z [i] / ZV);
		y [i] = y [i] / (1 - z [i] / ZV);
	    }
	    else
	    {
		x [i] = x [i] * INFINITY;       //draws off screen
		y [i] = y [i] * INFINITY;
	    }
	}
    }


    //converts to screen coordinates
    public static void shift_scale (double X[], double Y[], double x[], double y[])
    {
	for (int i = 0 ; i <= N - 1 ; ++i)
	{
	    X [i] = x [i] * SCALE_X + SHIFT_X;
	    Y [i] = y [i] * SCALE_Y + SHIFT_Y;
	}
    }


    public static void drawWorld (double x[], double y[], Color Cl)
    {
	double X[] = new double [N];
	double Y[] = new double [N];

	shift_scale (X, Y, x, y);   //shifts and scales entire world

	c.setColor (Cl);    //colour

	//drawing circles
	for (int i = 0 ; i <= 7 ; ++i)
	{
	    for (int j = 0 ; j <= C - 1 ; ++j)
	    {
		c.drawLine ((int) Math.round (X [i * C + j]),
			(int) Math.round (Y [i * C + j]),
			(int) Math.round (X [(i * C + j + 1) % C + i * C]),
			(int) Math.round (Y [(i * C + j + 1) % C + i * C]));
	    }
	}

	//midline
	c.drawLine ((int) Math.round (X [8 * C + 0]),
		(int) Math.round (Y [8 * C + 0]),
		(int) Math.round (X [8 * C + 1]),
		(int) Math.round (Y [8 * C + 1]));

	//near tall pole
	c.drawLine ((int) Math.round (X [8 * C + 4]),
		(int) Math.round (Y [8 * C + 4]),
		(int) Math.round (X [8 * C + 5]),
		(int) Math.round (Y [8 * C + 5]));

	//far tall pole
	c.drawLine ((int) Math.round (X [8 * C + 6]),
		(int) Math.round (Y [8 * C + 6]),
		(int) Math.round (X [8 * C + 7]),
		(int) Math.round (Y [8 * C + 7]));

	//near right pole
	c.drawLine ((int) Math.round (X [8 * C + 8]),
		(int) Math.round (Y [8 * C + 8]),
		(int) Math.round (X [8 * C + 9]),
		(int) Math.round (Y [8 * C + 9]));

	//near left pole
	c.drawLine ((int) Math.round (X [8 * C + 10]),
		(int) Math.round (Y [8 * C + 10]),
		(int) Math.round (X [8 * C + 11]),
		(int) Math.round (Y [8 * C + 11]));

	//far right pole
	c.drawLine ((int) Math.round (X [8 * C + 12]),
		(int) Math.round (Y [8 * C + 12]),
		(int) Math.round (X [8 * C + 13]),
		(int) Math.round (Y [8 * C + 13]));

	//far left pole
	c.drawLine ((int) Math.round (X [8 * C + 14]),
		(int) Math.round (Y [8 * C + 14]),
		(int) Math.round (X [8 * C + 15]),
		(int) Math.round (Y [8 * C + 15]));

	//bounding cube
	c.drawLine ((int) Math.round (X [8 * C + 20 + 0]),
		(int) Math.round (Y [8 * C + 20 + 0]),
		(int) Math.round (X [8 * C + 20 + 2]),
		(int) Math.round (Y [8 * C + 20 + 2]));
	c.drawLine ((int) Math.round (X [8 * C + 20 + 0]),
		(int) Math.round (Y [8 * C + 20 + 0]),
		(int) Math.round (X [8 * C + 20 + 5]),
		(int) Math.round (Y [8 * C + 20 + 5]));
	c.drawLine ((int) Math.round (X [8 * C + 20 + 0]),
		(int) Math.round (Y [8 * C + 20 + 0]),
		(int) Math.round (X [8 * C + 20 + 6]),
		(int) Math.round (Y [8 * C + 20 + 6]));
	c.drawLine ((int) Math.round (X [8 * C + 20 + 1]),
		(int) Math.round (Y [8 * C + 20 + 1]),
		(int) Math.round (X [8 * C + 20 + 3]),
		(int) Math.round (Y [8 * C + 20 + 3]));
	c.drawLine ((int) Math.round (X [8 * C + 20 + 1]),
		(int) Math.round (Y [8 * C + 20 + 1]),
		(int) Math.round (X [8 * C + 20 + 4]),
		(int) Math.round (Y [8 * C + 20 + 4]));
	c.drawLine ((int) Math.round (X [8 * C + 20 + 1]),
		(int) Math.round (Y [8 * C + 20 + 1]),
		(int) Math.round (X [8 * C + 20 + 7]),
		(int) Math.round (Y [8 * C + 20 + 7]));
	c.drawLine ((int) Math.round (X [8 * C + 20 + 2]),
		(int) Math.round (Y [8 * C + 20 + 2]),
		(int) Math.round (X [8 * C + 20 + 4]),
		(int) Math.round (Y [8 * C + 20 + 4]));
	c.drawLine ((int) Math.round (X [8 * C + 20 + 2]),
		(int) Math.round (Y [8 * C + 20 + 2]),
		(int) Math.round (X [8 * C + 20 + 7]),
		(int) Math.round (Y [8 * C + 20 + 7]));
	c.drawLine ((int) Math.round (X [8 * C + 20 + 3]),
		(int) Math.round (Y [8 * C + 20 + 3]),
		(int) Math.round (X [8 * C + 20 + 5]),
		(int) Math.round (Y [8 * C + 20 + 5]));
	c.drawLine ((int) Math.round (X [8 * C + 20 + 3]),
		(int) Math.round (Y [8 * C + 20 + 3]),
		(int) Math.round (X [8 * C + 20 + 6]),
		(int) Math.round (Y [8 * C + 20 + 6]));
	c.drawLine ((int) Math.round (X [8 * C + 20 + 4]),
		(int) Math.round (Y [8 * C + 20 + 4]),
		(int) Math.round (X [8 * C + 20 + 6]),
		(int) Math.round (Y [8 * C + 20 + 6]));
	c.drawLine ((int) Math.round (X [8 * C + 20 + 5]),
		(int) Math.round (Y [8 * C + 20 + 5]),
		(int) Math.round (X [8 * C + 20 + 7]),
		(int) Math.round (Y [8 * C + 20 + 7]));

    }
} // Cube class


