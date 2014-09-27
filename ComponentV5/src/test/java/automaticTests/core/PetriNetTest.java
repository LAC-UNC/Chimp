package automaticTests.core;

import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.testng.annotations.Test;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.netcommunicator.InformedTransition;
import com.lac.petrinet.netcommunicator.ProcessorHandler;

public class PetriNetTest {
	
	private InformedTransition itmocked = mock(InformedTransition.class);
	
	@SuppressWarnings("unchecked")
	public HashMap<String, InformedTransition> getInformedFromPetriNet(PetriNet obj) {
		Field field;
		try {
			field = PetriNet.class.getDeclaredField("informedTransitions");
			field.setAccessible(true);
			return (HashMap<String, InformedTransition>) field.get(obj);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Test
	public void addInformedTransitionToPetriNet() {
		PetriNet p = new PetriNet();
		
		assertFalse(this.getInformedFromPetriNet(p).containsValue(itmocked));
		assertFalse(this.getInformedFromPetriNet(p).containsKey("someInformed"));
		p.addInformed("someInformed", itmocked);
		assertTrue(this.getInformedFromPetriNet(p).containsValue(itmocked));
		assertTrue(this.getInformedFromPetriNet(p).containsKey("someInformed"));
	}
}
