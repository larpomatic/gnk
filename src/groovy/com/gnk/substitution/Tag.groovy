package com.gnk.substitution

class Tag {
	String value
	String type
	Integer weight	
	String status

    public Tag() {}

	public Tag(String value, String type, Integer weight, String status) {
		super();
		this.value = value;
		this.type = type;
		this.weight = weight;
		this.status = status;

	}

    @Override
    public java.lang.String toString() {
        return "Tag{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", weight=" + weight +
                ", status='" + status + '\'' +
                '}';
    }
}
