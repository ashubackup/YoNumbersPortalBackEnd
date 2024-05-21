package com.vision.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Categories  implements Comparable<Categories> {

    private String id;
	private String name;
	private String description;
	private String thumbnailUrl;
	private String type;
	private String name_id;
	private String name_en;
	private String name_fr;
	private String name_de;
	private String name_es;
	@Override
	public int compareTo(Categories o) {
		return this.getName().compareTo(o.getName());
	}
	
	
	
}
