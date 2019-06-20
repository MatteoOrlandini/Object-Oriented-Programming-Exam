package exam;

import org.json.simple.JSONObject;

public class FilterParameters {
	private String fieldName;
	private String operator;
	private Object value;
	
	public FilterParameters(String fieldName, String operator, Object value) {
		super();
		this.fieldName = fieldName;
		this.operator = operator;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * This method is used by PharmacyService.filter to read the body of the JSON and update the parameters.
	 * @param obj JSON body
	 */
	public void readFields(Object body) {
		if (body instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject) body;
			fieldName = (String) jsonObj.get("fieldName");
			operator = (String) jsonObj.get("operator");
			value = jsonObj.get("value");
		}
	}	

}
