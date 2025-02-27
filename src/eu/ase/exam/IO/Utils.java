package eu.ase.exam.IO;

import eu.ase.exam.Animal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Utils {
	private static List<Animal> list = new ArrayList<>();
	
	public static void addAnimal(Animal a) {
		Utils.list.add(a);
	}
	
	public static void writeBinary(String fileName) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
			for(Animal a : list) {
				oos.writeObject(a);
				oos.flush();
			}
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Animal> readBinary(String fileName){
		Animal a = null;
		List<Animal> listA = new ArrayList<>();
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
			while((a = (Animal) ois.readObject())!=null) {
				listA.add(a);
			}
			ois.close();
		}catch (IOException e) {
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Utils.list = listA;
		return Utils.list;
	}
	
	public static List<Animal> filterList(String sequence){
		Stream<Animal> stream = Utils.list.stream();
		Utils.list = stream.filter(animal -> animal.getName().contains(sequence)).toList();
		return Utils.list;
	}
}
