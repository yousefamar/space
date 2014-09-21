package gfx;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

import core.ParadigmShiftGame;
import utils.*;

public class Model {
	//TODO: static HashMap<Class<Entity>, Model> modelMap = new HashMap<Class<Entity>, Model>();
	public static HashMap<String, Model> modelMap = new HashMap<String, Model>();

	public ArrayList<Vec3D> vertices = new ArrayList<Vec3D>();
	public ArrayList<Vec2D> texCoords = new ArrayList<Vec2D>();
	public ArrayList<TexturedPolygon> faces = new ArrayList<TexturedPolygon>();
	

	public static void loadModels() {
		walkDir(new File(ParadigmShiftGame.saveDir + "data", "models"));
	}
	
	public static void walkDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null)
            for (File file : files) {
                if (file.isDirectory())
					walkDir(file);
                else if (file.getName().endsWith(".obj")) {
                	try{
                		FileInputStream fstream = new FileInputStream(file.getPath());
                		DataInputStream in = new DataInputStream(fstream);
                		BufferedReader br = new BufferedReader(new InputStreamReader(in));
                		String line;
                		Model model =  new Model();
                		String fileName = file.getName();
                		String modelName = fileName.substring(0, fileName.lastIndexOf("."));
                		while ((line = br.readLine()) != null) {
                			if (line.length()<2 || line.startsWith("#"))
                				continue;
                			String[] sline = line.split("\\s+");
                			if (sline[0].equals("v"))
                				model.vertices.add(new Vec3D(Integer.parseInt(sline[1]), Integer.parseInt(sline[2]), Integer.parseInt(sline[3])));
                			else if (sline[0].equals("vt"))
                				model.texCoords.add(new Vec2D(Integer.parseInt(sline[1]), Integer.parseInt(sline[2])));
                			else if (sline[0].equals("f")) {
                				TexturedPolygon face = new TexturedPolygon();
                				for (int i = 1; i < sline.length; i++) {
                					String[] nums = sline[i].split("/");
                					face.vertexIDs.add(Integer.parseInt(nums[0]));
                					face.texCoordIDs.add(Integer.parseInt(nums[1]));
                				}
                				model.faces.add(face);
                			}
                			else if (sline[0].equals("o"))
                				modelName = sline[1];
                		}
                		modelMap.put(modelName, model);

                		fstream.close();
                		in.close();
                		br.close();
                	}catch (Exception e){
                		e.printStackTrace();
                	}
                }
            }
	}
}
