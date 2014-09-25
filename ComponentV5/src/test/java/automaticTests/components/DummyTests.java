package automaticTests.components;

import org.testng.annotations.Test;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.core.PetriNet;

import static org.mockito.Mockito.*;

public class DummyTests {
	//mock creation
	private PetriNet mockedPN = mock(PetriNet.class);

	public class DummyClass extends Dummy {
		public DummyClass(String tName){
			super(DummyTests.this.mockedPN, tName);
		}
		@Override
		protected void execute() {
			return;
		}
	}
	
	@Test
	public void fireTransitionAfterExecutionTest(){
		DummyClass dummyObj = new DummyClass("SomeTransition");
		
		try {
			dummyObj.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		verify(mockedPN).fire("SomeTransition");
	}
}
