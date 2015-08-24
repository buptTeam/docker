package basic;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParseToReponse {
	String Status;
	String Mes;
	int Length;
	Object Entity;
	public  ParseToReponse(String status,String mes,Object entity,int len) {
		this.Entity=entity;
		this.Mes=mes;
		this.Status=status;
		this.Length=len;
	}
	
	public static String parse(String status,String mes,Object entity,int len) {
		String mesString=null;
		ParseToReponse parseToReponse=new ParseToReponse(status, mes, entity,len);
		ObjectMapper mapper = new ObjectMapper(); 
		try {
			mesString= mapper.writeValueAsString(parseToReponse);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mesString;
		
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getMes() {
		return Mes;
	}

	public void setMes(String mes) {
		Mes = mes;
	}

	public Object getEntity() {
		return Entity;
	}

	public void setEntity(Object entity) {
		Entity = entity;
	}

	public int getLength() {
		return Length;
	}

	public void setLength(int length) {
		Length = length;
	}
	
}
