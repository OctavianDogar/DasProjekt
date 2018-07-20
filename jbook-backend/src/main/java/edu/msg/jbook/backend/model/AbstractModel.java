package edu.msg.jbook.backend.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractModel implements Serializable {

	private static final long serialVersionUID = -1627326135959296585L;
	private String uuid;

	@PrePersist
	public void ensureUUid() {
		if (getUuid() == null) {
			setUuid(UUID.randomUUID().toString());
		}
	}
	
	@Override
	public boolean equals(Object obj){
		if(this==obj)
			return true;
		if(obj==null)
			return false;
		AbstractModel other = (AbstractModel) obj;
		if(getUuid() == null){
			if(other.getUuid()!=null)
				return false;
		}else{
			if(!getUuid().equals(other.getUuid())){
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return getUuid().hashCode();
	}

	@Column(name="UUID",nullable=false,length=36,unique=true)
	public String getUuid() {
		if(this.uuid == null) {
			this.uuid = UUID.randomUUID().toString();
		}

		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
