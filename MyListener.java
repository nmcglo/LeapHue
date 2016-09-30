import java.io.IOException;
import java.util.Date;

import nl.q42.jue.FullLight;
import nl.q42.jue.Group;
import nl.q42.jue.HueBridge;
import nl.q42.jue.StateUpdate;
import nl.q42.jue.exceptions.ApiException;

import com.leapmotion.leap.*;


public class MyListener extends Listener
{
	HueBridge bridge;
	Group lightGroup;
	boolean needNewState = true;
	long lastStateTime;
	float lastLeftStatePos;
	float lastRightStatePos;
	Date initDate;
	
	public MyListener(HueBridge bridge)
	{
		super();
		this.bridge = bridge;
		lightGroup = bridge.getAllGroup();
	}
	
	public void onInit(Controller controller) {
	        System.out.println("Initialized");
	        new StateUpdate().setSat(255);
	    }

    public void onConnect(Controller controller) {
	        System.out.println("Connected");
	        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
	        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
	        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
	        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	    }

    public void onDisconnect(Controller controller) {
	        //Note: not dispatched when running in a debugger.
	        System.out.println("Disconnected");
	    }

	public void onExit(Controller controller) {
	        System.out.println("Exited");
	    }
	
	
	

	/* (non-Javadoc)
	 * @see com.leapmotion.leap.Listener#onFrame(com.leapmotion.leap.Controller)
	 */
	@Override
	
	public void onFrame(Controller controller)
	{
		// TODO Auto-generated method stub
		super.onFrame(controller);
		
		Frame frame = controller.frame();
		Hand right = frame.hands().rightmost();
		Hand left = frame.hands().leftmost();
		
		//Initialize the hands for real
		if (frame.hands().count() > 0)
			{
				right = frame.hands().rightmost();
				
				if (frame.hands().count() == 2)
				{
					left = frame.hands().leftmost();
				}
				
			}
		
		//calculate the height of right hand from the plane of the leap motion
		Vector leftYVector = new Vector(0,left.palmPosition().getY(),0);
		float leftAngle = leftYVector.angleTo(Vector.up());
		double leftHeight = leftYVector.getY() * Math.cos(leftAngle);
		if (leftHeight > 455)
			leftHeight = 455;
		if (leftHeight < 200)
			leftHeight = 200;
		int leftModHeight = (int) leftHeight - 200;

		
		Vector rightYVector = new Vector(0,right.palmPosition().getY(),0);
		float rightAngle = rightYVector.angleTo(Vector.up());
		double rightHeight = rightYVector.getY() * Math.cos(rightAngle);
		if (rightHeight < 200)
			rightHeight = 0;
		if (rightHeight > 450)
			rightHeight = 650;
		if (rightHeight > 200 && rightHeight < 650)
			rightHeight = (rightHeight - 200) * 2.3;
		
		
		int rightModHeight = (int) rightHeight;
		rightModHeight = rightModHeight * 100;
		
		
		
		initDate = new Date();
		long deltaTime = initDate.getTime() - lastStateTime;
		
	
		if(frame.hands().count() == 1)
		{
			if(((deltaTime > 1000) && (left.palmVelocity().getY() < 5) && (Math.abs(leftModHeight - lastLeftStatePos) > 10)) || (deltaTime > 1000 && Math.abs(leftModHeight - lastLeftStatePos) > 10))
			{
				needNewState = true;
			}
			if(needNewState == true)
			{
				System.out.println("Changing to Brightness: " + leftModHeight);
				StateUpdate update = new StateUpdate().setBrightness((int) leftModHeight);
				needNewState = false;
				lastStateTime = new Date().getTime();
				lastLeftStatePos = leftModHeight;
				try
				{
					bridge.setGroupState(lightGroup, update);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ApiException e)
				{
					// TODO Auto-generated catch block
				}
			}
		}
		
		if(frame.hands().count() == 2)
		{
		
			if(((deltaTime > 1000) && ((left.palmVelocity().getY() < 5) || (right.palmVelocity().getY() < 5)) && ((Math.abs(leftModHeight - lastLeftStatePos) > 10) || (Math.abs(rightModHeight - lastRightStatePos) > 2000 ) ) ) || (deltaTime > 1000 && ((Math.abs(leftModHeight - lastLeftStatePos) > 10) || (Math.abs(rightModHeight - lastRightStatePos) > 2000 )      )))
			{
				needNewState = true;
			}
			
			
			
			
			
			if(needNewState == true)
			{
				System.out.println("Changing to Brightness: " + leftModHeight);
				System.out.println("Changing to Hue: " + rightModHeight);
				StateUpdate update = new StateUpdate().setBrightness((int) leftModHeight);
				StateUpdate update2 = new StateUpdate().setHue((int) rightModHeight);
				needNewState = false;
				lastStateTime = new Date().getTime();
				lastLeftStatePos = leftModHeight;
				lastRightStatePos = rightModHeight;
				try
				{
					bridge.setGroupState(lightGroup, update);
					bridge.setGroupState(lightGroup, update2);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ApiException e)
				{
					// TODO Auto-generated catch block
				}
			}
		}
		

		
//        if (frame.hands().count() > 0)
//        {
//        	System.out.println("Frame id: " + frame.id()
//                    + ", timestamp: " + frame.timestamp()
//                    + ", hands: " + frame.hands().count()
//                    + ", fingers: " + frame.fingers().count()
//                    + ", tools: " + frame.tools().count()
//                    + ", gestures " + frame.gestures().count());
//        }
	}
	
}

	


