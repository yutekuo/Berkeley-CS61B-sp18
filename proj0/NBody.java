public class NBody {
    /**
     * Given a file name as a String, it should return a double
     * corresponding to the radius of the universe in that file.
     */
    public static double readRadius(String file) {
        In in = new In(file);
        int numberOfPlanets = in.readInt();
        double radius = in.readDouble();
        return radius;
    }

    /**
     * Given a file name, it should return an array of Bodys corresponding to
     * the bodies in the file.
     */
    public static Planet[] readPlanets(String file) {
        In in = new In(file);
        int numberOfPlanets = in.readInt();
        Planet[] arrayOfPlanets = new Planet[numberOfPlanets];

        double radius = in.readDouble();
        for (int i = 0; i < numberOfPlanets; i++) {
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String img = in.readString();
            arrayOfPlanets[i] = new Planet(xP, yP, xV, yV, m, img);
        }

        return arrayOfPlanets;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        Planet[] planets = readPlanets(filename);
        double universeRadius = readRadius(filename);

        /** Set the scale so that it matches the radius of the universe. */
		StdDraw.setScale(-universeRadius, universeRadius);

        /** Draw the image starfield.jpg as the background. */
        StdDraw.picture(0, 0, "images/starfield.jpg");

        /** Draw all of the planets. */
        for (Planet p : planets) {
            p.draw();
        }

        /**
         * When double buffering is enabled by calling enableDoubleBuffering(),
         * all drawing takes place on the offscreen canvas. The offscreen canvas is
         * not displayed. Only when you call show() does your drawing get copied from
         * the offscreen canvas to the onscreen canvas, where it is displayed in the
         * standard drawing window.
         *
         * @source https://sp18.datastructur.es/materials/proj/proj0/proj0#creating-an-animation
         */
        StdDraw.enableDoubleBuffering();

        double time = 0;
        while (time <= T) {
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];
            for (int i = 0; i < planets.length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
                planets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (Planet p : planets) {
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            time += dt;
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", universeRadius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                  planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
