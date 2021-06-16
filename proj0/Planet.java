public class Planet {
    /** Its current x position. */
    double xxPos;
    /** Its current y position. */
    double yyPos;
    /** Its current velocity in the x direction. */
    double xxVel;
    /** Its current velocity in the y direction. */
    double yyVel;
    /** Its mass. */
    double mass;
    /** The name of the file that corresponds to the image that depicts the planet */
    String imgFileName;
    /** The gravitational constant. */
    static final double G = 6.67e-11;

    /** The first constructor. */
    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    /**
     * The second constructor should take in a Planet object and initialize
     * an identical Planet object.
     */
    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    /**
     * This method will take in a single planet and return a double equal to
     * the distance between the supplied planet and the planet that is doing
     * the calculation.
     */
    public double calcDistance(Planet p) {
        double dx = p.xxPos - xxPos;
        double dy = p.yyPos - yyPos;
        double rSquare = dx * dx + dy * dy;
        double r = Math.sqrt(rSquare);
        return r;
    }

    /**
     * This method takes in a planet, and returns a double
     * describing the force exerted on this planet by the given planet.
     */
    public double calcForceExertedBy(Planet p) {
        double r = calcDistance(p);
        double F = G * p.mass * mass / (r * r);
        return F;
    }

    /** Return the force exerted in the X direction. */
    public double calcForceExertedByX(Planet p) {
        double F = calcForceExertedBy(p);
        double r = calcDistance(p);
        double dx = p.xxPos - xxPos;
        double fx = dx * F / r;
        return fx;
    }

    /** Return the force exerted in the Y direction. */
    public double calcForceExertedByY(Planet p) {
        double F = calcForceExertedBy(p);
        double r = calcDistance(p);
        double dy = p.yyPos - yyPos;
        double fy = dy * F / r;
        return fy;
    }

    /**
     * Takes in an array of planets and calculates the net X force exerted by
     * all planets in that array upon the current planet.
     */
    public double calcNetForceExertedByX(Planet[] allPlanets) {
        double netXForce = 0;

        for (Planet p : allPlanets) {
            if (this.equals(p)) {
                continue;
            }

            netXForce += calcForceExertedByX(p);
        }

        return netXForce;
    }

    /**
     * Takes in an array of planets and calculates the net Y force exerted by
     * all planets in that array upon the current planet.
     */
    public double calcNetForceExertedByY(Planet[] allPlanets) {
        double netYForce = 0;

        for (Planet p : allPlanets) {
            if (this.equals(p)) {
                continue;
            }

            netYForce += calcForceExertedByY(p);
        }

        return netYForce;
    }

    /**
     * Determines how much the forces exerted on the planet will cause that
     * planet to accelerate, and the resulting change in the planet's velocity
     * and position in a small period of time dt.
     */
    public void update(double dt, double fX, double fY) {
        double aX = fX / mass;
        double aY = fY / mass;
        xxVel = xxVel + aX * dt;
        yyVel = yyVel + aY * dt;
        xxPos = xxPos + xxVel * dt;
        yyPos = yyPos + yyVel * dt;
    }

    /** Draw the planet's image at the planet's position. */
    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }
}
