import java.io.IOException;

import nl.q42.jue.FullLight;
import nl.q42.jue.Group;
import nl.q42.jue.HueBridge;
import nl.q42.jue.Light;
import nl.q42.jue.StateUpdate;
import nl.q42.jue.exceptions.ApiException;

import com.leapmotion.leap.*;


public class main
{

	public static void main(String[] args) throws IOException, ApiException
	{
		
		
		HueBridge bridge = new HueBridge("192.168.2.5");
		try
		{
//			try {
//			    Thread.sleep(7500);
//			} catch(InterruptedException ex) {
//			    Thread.currentThread().interrupt();
//			}
//			bridge.link("NeilLikesHue", "LeapHUE");
			bridge.authenticate("NeilLikesHue");
	
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApiException e)
		{
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FullLight[] fullLights = new FullLight[3];
		int i = 0;
		for(Light light: bridge.getLights())
		{
			fullLights[i] = bridge.getLight(light);
			i++;
		}
		
		

		

		
		
	
		System.out.println("DONE");
		
		MyListener listener = new MyListener(bridge);
		Controller controller = new Controller();
		
		controller.addListener(listener);
		
		System.out.println("Press Enter to Quit...");
		try
		{	
			System.in.read();

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		controller.removeListener(listener);
		
		
		
		
		
	}

}
