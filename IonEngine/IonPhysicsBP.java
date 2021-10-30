package IonEngine;

import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
//import java.awt.event.*;
import java.util.HashMap;

public class IonPhysicsBP extends IonBehaviourPack{
    private boolean enabled;
    private IonContainer container;

    public IonPhysicsBP(boolean enabled) {
        this.enabled = enabled;
    }
    public IonPhysicsBP() {
        this.enabled = true;
    }

    public void setContainer(IonContainer container) {
        this.container = container;
    }

    public IonContainer getContainer() {
        return container;
    }


    public void on() {
        enabled = true;
        run();
    }
    public void off() {
        enabled = false;
    }

    public void setState(boolean state) {
        enabled = state;
        if (enabled)
            run();
    }


    private double g = 9.8;
    public IonPhysicsBP setG(double newG) { g = newG; return this;}
    private double bounce = 0.4;
    public void setBounce(double newBounce) { bounce = newBounce; }
    private double friction = 0.9;
    public void setFriction(double newFriction) { friction = newFriction; }
    private double error = 0.06;
    private double maxV = 1000;
    private final double tStep = 0.025;
    private double movementForce = 50;
    

    public void run() {
        while (enabled) {
            HashMap<String, IonObject> objectMap = container.getObjectMap();
            try{Thread.sleep(1);} catch (Exception e) {}
            for(IonObject object: objectMap.values())
            {
                if ((boolean)object.getProperty("free", false))
                {
                    double velocityX = (double)object.getProperty("velocityX", 0.0);
                    double velocityY = (double)object.getProperty("velocityY", 0.0);

                    //if object touches the top and has no Y velocity, starts falling
                    if (object.touchesTop() && velocityY > -error)
                    {
                        velocityY = tStep * g;
                    }
                    //if has a Y velocity or not touches the bottom, falls
                    else if (!object.touchesBottom(0.5) || velocityY > error || velocityY < -error)
                    {
                        velocityY += tStep * g;
                        //top/bottom bouncing
                        if (object.touchesBottom() || object.touchesTop())
                        {
                            velocityY *= -bounce;
                        }
                        
                    }
                    else
                        velocityY = 0;

                    //if has X velocity, moves left/right
                    if (velocityX > error || velocityX < -error)
                    {
                        if (object.touchesBottom(0.5))
                        velocityX *= friction;
                        //left/right bouncing
                        if (object.touchesLeft() || object.touchesRight())
                            velocityX *= -bounce;
                    }
                    else
                        velocityX = 0;

                    //limit max velocity
                    if (velocityX > maxV || Double.isNaN(velocityX))
                        velocityX = maxV;
                    else if (velocityX < -maxV)
                        velocityY = -maxV;

                    if (velocityY > maxV || Double.isNaN(velocityY))
                        velocityY = maxV;
                    else if (velocityY < -maxV)
                        velocityY = -maxV;

                    //if has any velocity, move
                    if (velocityX != 0 || velocityY != 0)
                        object.moveBy(velocityX * tStep, velocityY * tStep);
                    

                    //object collision - IN DEVELOPMENT!

                    // ArrayList<IonObject> collidedObjects = new ArrayList<IonObject>(); //for object collided in total
                    // if (velocityX > 0)
                    //     for (int y = (int)object.getY(); y < (int)object.getY() + object.getHeight(); y++)
                    //     {
                    //         ArrayList<IonObject> collidedWith = container.getObjectsByCoordinates((int)object.getX() + object.getWidth(), y, "free", false);
                    //         for (IonObject collidedObject: collidedWith)
                    //         {
                    //             if (collidedObject != object)
                    //             {
                    //                 collidedObjects.add(collidedObject);
                                    
                    //             }

                    //         }
                    //     }
                    // else if (velocityX < 0)
                    //     for (int y = (int)object.getY(); y < (int)object.getY() + object.getHeight(); y++)
                    //     {
                    //         ArrayList<IonObject> collidedWith = container.getObjectsByCoordinates((int)object.getX(), y, "free", false);
                    //         for (IonObject collidedObject: collidedWith)
                    //         {
                    //             if (collidedObject != object)
                    //                 collidedObjects.add(collidedObject);
                    //         }
                    //     }

                    // if (velocityY > 0)
                    //     for (int x = (int)object.getX(); x < (int)object.getX() + object.getWidth(); x++)
                    //     {
                    //         ArrayList<IonObject> collidedWith = container.getObjectsByCoordinates(x, (int)object.getY() + object.getHeight(), "free", false);
                    //         for (IonObject collidedObject: collidedWith)
                    //         {
                    //             if (collidedObject != object)
                    //                 collidedObjects.add(collidedObject);
                    //         }
                    //     }
                    // else if (velocityY < 0)
                    //     for (int x = (int)object.getX(); x < (int)object.getX() + object.getWidth(); x++)
                    //     {
                    //         ArrayList<IonObject> collidedWith = container.getObjectsByCoordinates(x, (int)object.getY(), "free", false);
                    //         for (IonObject collidedObject: collidedWith)
                    //         {
                    //             if (collidedObject != object)
                    //                 collidedObjects.add(collidedObject);
                    //         }
                    //     }


                    // if (collidedObjects.size() > 0)
                    //     System.out.println(collidedObjects.get(0).getName());
                    
                    //save new velocities
                    object.setProperty("velocityY", velocityY);
                    object.setProperty("velocityX", velocityX);
                }
            }
        }
    }

    
    public void objectEvent(IonObject object, String type) {
        if (type == "movedTo" || type == "changedX" || type == "changedY") {
            //fetch properties
            long lastTimeMovedInMillis = (long)object.getProperty("lastTimeMovedInMillis", (long)-1);
            double velocityX = (double)object.getProperty("velocityX", 0.0);
            double velocityY = (double)object.getProperty("velocityY", 0.0);

            double mass = (double)object.getProperty("mass", 1.0);

            //if in movement, calculate new velocities
			if (lastTimeMovedInMillis > 0)  
			{  
                velocityX = ((((double)object.getProperty("lastX", 0) - object.getX()) * movementForce) / ((lastTimeMovedInMillis - System.currentTimeMillis()) * mass) + velocityX) / 2;
                velocityY = ((((double)object.getProperty("lastY", 0) - object.getY()) * movementForce) / ((lastTimeMovedInMillis - System.currentTimeMillis()) * mass) + velocityY) / 2;
			}
            //limit the max velocity
            if (velocityX > maxV || Double.isNaN(velocityX))
                velocityX = maxV;
            else if (velocityX < -maxV)
                velocityY = -maxV;

            if (velocityY > maxV || Double.isNaN(velocityY))
                velocityY = maxV;
            else if (velocityY < -maxV)
                velocityY = -maxV;
            //save new properties
            object.setProperty("velocityY", velocityY);
            object.setProperty("velocityX", velocityX);
			object.setProperty("lastX", object.getX());
			object.setProperty("lastY", object.getY());
			object.setProperty("lastTimeMovedInMillis", System.currentTimeMillis());
        }
        else if (type == "released")
        {
            //if released, stop the movement
            object.setProperty("lastTimeMovedInMillis", (long)-1);
        }
    }

    

}
