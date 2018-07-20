package edu.msg.jbook.backend.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity extends AbstractEntity<Long>{

	private static final long serialVersionUID = 1L;
	private Long id;

	public BaseEntity(){
		this(null);
	}

	public BaseEntity(Long id){
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
