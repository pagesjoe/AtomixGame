package eg.edu.guc.atomix.engine;


import java.util.Collection;

public interface BoardInterface {
	public boolean move(Object atom, char direction);

	public boolean gameover();

	public Collection<Atom> getAtoms();
}
