package IonEngine;

import java.awt.event.*;
import java.util.HashMap;

public class IonPhysicsBP extends IonBehaviourPack {
    private boolean enabled;
    private IonContainer container;

    public IonPhysicsBP(boolean enabled) {
        this.enabled = enabled;
    }
    public IonPhysicsBP() {
        this.enabled = false;
    }

    public void setContainer(IonContainer container) {
        this.container = container;
    }

    public IonContainer getContainer() {
        return container;
    }

    public void on() {
        enabled = true;
        process();
    }
    public void off() {
        enabled = false;
    }

    public void setState(boolean state) {
        enabled = state;
        if (enabled)
            process();
    }


    private double g = 9.8;
    public void setG(double newG) { g = newG; }
    private double bounce = 0.3;
    public void setBounce(double newBounce) { bounce = newBounce; }
    private double friction = 0.7;
    public void setFriction(double newFriction) { friction = newFriction; }
    private double error = 0.1;
    private double maxV = 100;
    private final double tStep = 0.025;
    
    private void process() {
        while (enabled) {
            HashMap<String, IonObject> objectMap = container.getObjectMap();
            try{Thread.sleep(1);} catch (Exception e) {}
            for(IonObject object: objectMap.values())
            {
                if ((boolean)object.getProperty("free", false))
                {
                    double velocityX = (double)object.getProperty("velocityX", 0.0);
                    double velocityY = (double)object.getProperty("velocityY", 0.0);


                    if (velocityY > maxV)
                        velocityY = maxV;
                    else if (velocityY < -maxV)
                        velocityY = -maxV;

                    //System.out.println("Vy = " + velocityY);
                    if (object.getY() + object.getHeight() + 0.1 < container.getHeight() || velocityY > error || velocityY < -error)
                    {
                        velocityY += tStep * g;
                        if (object.touchesBottom() || object.touchesTop())
                            velocityY *= -bounce;
                    }
                    else
                        velocityY = 0;


                    if (velocityX > maxV)
                        velocityX = maxV;
                    else if (velocityX < -maxV)
                        velocityY = -maxV;

                    if (velocityX > error || velocityX < -error)
                    {
                        //System.out.println("Vx = " + velocityX);
                        if (object.touchesLeft() || object.touchesRight())
                            velocityX *= -bounce;
                        if (object.touchesBottom())
                            velocityX *= friction;
                    }
                    else
                        velocityX = 0;

                    if (velocityX != 0 || velocityY != 0)
                        object.moveBy(velocityX * tStep, velocityY * tStep);

                    object.setProperty("velocityY", velocityY);
                    object.setProperty("velocityX", velocityX);
                    
                    
                }
            }
        }
    }

    
    public void objectEvent(IonObject object, String type) {
        if (type == "movedTo" || type == "changedX" || type == "changedY") {
            
            long lastTimeMovedInMillis = (long)object.getProperty("lastTimeMovedInMillis", (long)-1);
            double velocityX = (double)object.getProperty("velocityX", 0.0);
            double velocityY = (double)object.getProperty("velocityY", 0.0);

			if (lastTimeMovedInMillis > 0)
			{
				velocityX = (((double)object.getProperty("lastX", 0) - object.getX()) / (lastTimeMovedInMillis - System.currentTimeMillis()) * 75 + velocityX) / 2;
				velocityY = (((double)object.getProperty("lastY", 0) - object.getY()) / (lastTimeMovedInMillis - System.currentTimeMillis()) * 75 + velocityY) / 2;
			}

            object.setProperty("velocityY", velocityY);
            object.setProperty("velocityX", velocityX);
			object.setProperty("lastX", object.getX());
			object.setProperty("lastY", object.getY());
			object.setProperty("lastTimeMovedInMillis", System.currentTimeMillis());

            //System.out.println(velocityX + " " + velocityY);
        }
    }

    

}
