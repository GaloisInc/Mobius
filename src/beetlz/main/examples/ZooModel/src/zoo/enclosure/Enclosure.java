package zoo.enclosure;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import zoo.animal.Animal;

//Use this class for testing compound types.
public class Enclosure {
  public short animalCount = 0;
  public long animalCapacity = 0;
  
  public boolean accomodateAnimal(List<Animal> an){return true;}
  public SortedSet < Animal> removeAnimal(List<Animal> an){return null;}
  
  public void moveAnimal(String reason, zoo.enclosure.Cage c){}
  
  public void nameAnimal(java.lang.String name){}
  
  public void addAnimals(Map<Animal, String> animals){ }  
}
