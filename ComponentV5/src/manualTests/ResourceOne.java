package manualTests;
import com.lac.petrinet.components.Dummy;


public class ResourceOne extends Dummy {

	@Override
	protected void execute() {
		System.out.println("joder!");
	}
}
