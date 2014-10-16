package modelo.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the F0004 database table.
 * 
 */
@Embeddable
public class F0004PK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="DTSY")
	private String dtsy;

	@Column(name="DTRT")
	private String dtrt;

	public F0004PK() {
	}
	public String getDtsy() {
		return this.dtsy;
	}
	public void setDtsy(String dtsy) {
		this.dtsy = dtsy;
	}
	public String getDtrt() {
		return this.dtrt;
	}
	public void setDtrt(String dtrt) {
		this.dtrt = dtrt;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof F0004PK)) {
			return false;
		}
		F0004PK castOther = (F0004PK)other;
		return 
			this.dtsy.equals(castOther.dtsy)
			&& this.dtrt.equals(castOther.dtrt);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.dtsy.hashCode();
		hash = hash * prime + this.dtrt.hashCode();
		
		return hash;
	}
}