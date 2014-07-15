package edu.utexas.cycic;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class XMLReader {
	static String test = "<interleave><element name=\"in_commods\"><oneOrMore><element name=\"val\">" +  
			"<data type=\"token\" /></element></oneOrMore></element><element name=\"capacity\">"+
			"<data type=\"double\" /></element><optional><element name=\"max_inv_size\"><data type=\"double\" />"+
			"</element></optional></interleave>";
	static String jsonTest = "{"+
		"\"doc\" : \"A minimum implementation sink facility that accepts specified amounts of commodities from other agents\","+
		"\"vars\" : {" +
		"\"capacity\" : {" +
        "\"doc\" : \"capacity the sink facility can accept at each time step\"," +
        "\"index\" : 1, \"tooltip\" : \"sink capacity\", \"type\" : \"double\"}," +
        "\"in_commods\" : {\"doc\" : \"commodities that the sink facility accepts\", \"index\" : 0," +
        "\"schematype\" : \"token\", \"tooltip\" : \"input commodities\", \"type\" : [ \"std::vector\", \"std::string\" ]" +
      	"}, \"inventory\" : {\"capacity\" : \"max_inv_size\", \"index\" : 3, \"type\" : \"cyclus::toolkit::ResourceBuff\"},"+
      	"\"max_inv_size\" : {\"default\" : 1.000000000000000e+299, \"doc\" : \"total maximum inventory size of sink facility\","+
        "\"index\" : 2, \"tooltip\" : \"sink maximum inventory size\", \"type\" : \"double\"}}}";
			
	static ArrayList<Object> readSchema(String xmlSchema){
		ArrayList<Object> schema = new ArrayList<Object>();
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlSchema));
			Document doc = dBuilder.parse(is);
			
			NodeList top = doc.getChildNodes();
			for(int i = 0; i < top.getLength(); i++){
				schema = nodeListener(top.item(i), schema);
			}
			//System.out.println(schema);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return schema;
	}
	
	static void annotationReader(String jsonSchema){
		Reader schema = new StringReader(jsonSchema);
		JsonReader jsonReader = Json.createReader(schema);
		JsonObject jsonObject = jsonReader.readObject();
		jsonReader.close();
		JsonObject vars = jsonObject.getJsonObject("vars");
		ArrayList<Object> xmlschema = readSchema(test);
		for(int i = 0; i < xmlschema.size(); i++){
			combiner((ArrayList<Object>)xmlschema.get(i), vars);		
		}
		System.out.println(xmlschema);
	}
	
	static void combiner(ArrayList<Object> dataArray, JsonObject json){
		if(dataArray.get(0) instanceof ArrayList){
			combiner((ArrayList<Object>)dataArray.get(0), json);
		} else if(dataArray.get(1) instanceof ArrayList){
			while(dataArray.size() < 9){
				dataArray.add(null);
			}
			JsonObject json_pass = json.getJsonObject((String)dataArray.get(0));
			combiner((ArrayList<Object>)dataArray.get(1), json);
			dataArray.set(3, json_pass.get("units"));
			dataArray.set(4, json_pass.get("range"));
			dataArray.set(5, json_pass.get("default"));
			if(json_pass.get("default") != null){
				
			}
			dataArray.set(6, json_pass.get("userlevel"));
			dataArray.set(7, json_pass.get("tooltip"));
			dataArray.set(8, json_pass.get("doc"));
		} else {
			while(dataArray.size() < 9){
				dataArray.add(null);
			}
			JsonObject json_pass = json.getJsonObject((String)dataArray.get(0));
			try{
				//dataArray.set(2, json_pass.get("type"));
				dataArray.set(3, json_pass.get("units"));
				dataArray.set(4, json_pass.get("range"));
				dataArray.set(5, json_pass.get("default"));
				dataArray.set(6, json_pass.get("userlevel"));
				dataArray.set(7, json_pass.get("tooltip"));
				dataArray.set(8, json_pass.get("doc"));
			} catch (Exception ex) {
				
			}	
		}
		return;
	}
	static ArrayList<Object> nodeListener(Node node, ArrayList<Object> array){
		NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++){
			if(nodes.item(i).getNodeName() == "oneOrMore" || nodes.item(i).getNodeName() == "zeroOrMore"){
				ArrayList<Object> newArray = new ArrayList<Object>();
				newArray = nodeListener(nodes.item(i), newArray);
				array.add(newArray);
				array.add(nodes.item(i).getNodeName());
			}
			if(nodes.item(i).getNodeName() == "element"){
				ArrayList<Object> newArray = new ArrayList<Object>();
				for(int j = 0; j < nodes.item(i).getAttributes().getLength(); j++){
					if (nodes.item(i).getAttributes().item(j).getNodeName() == "name"){
						newArray.add(nodes.item(i).getAttributes().item(j).getNodeValue());
					}
				}
				array.add(nodeListener(nodes.item(i), newArray));
			}
			if(nodes.item(i).getNodeName() == "optional"){
				Node newNode = nodes.item(i).getChildNodes().item(0);
				ArrayList<Object> newArray = new ArrayList<Object>();
				for(int j = 0; j < newNode.getAttributes().getLength(); j++){
					if (newNode.getAttributes().item(j).getNodeName() == "name"){
						newArray.add(newNode.getAttributes().item(j).getNodeValue());
					}
				}
				array.add(nodeListener(newNode, newArray));
			}
			if(nodes.item(i).getNodeName() == "data"){
				for(int j = 0; j < nodes.item(i).getAttributes().getLength(); j++){
					if(nodes.item(i).getAttributes().item(j).getNodeName() == "type"){
						array.add(1, nodes.item(i).getAttributes().item(j).getNodeValue());
					}
				}
			}
		}
		return array;
	}
}
