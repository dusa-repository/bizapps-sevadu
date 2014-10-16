package modelo.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the F0005 database table.
 * 
 */
@Embeddable
public class F0005PK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="DRSY")
	private String drsy;

	@Column(name="DRRT")
	private String drrt;

	@Column(name="DRKY")
	private String drky;

	public F0005PK() {
	}
	public String getDrsy() {
		return this.drsy;
	}
	public void setDrsy(String drsy) {
		this.drsy = drsy;
	}
	public String getDrrt() {
		return this.drrt;
	}
	public void setDrrt(String drrt) {
		this.drrt = drrt;
	}
	public String getDrky() {
		return this.drky;
	}
	public void setDrky(String drky) {
		this.drky = drky;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof F0005PK)) {
			return false;
		}
		F0005PK castOther = (F0005PK)other;
		return 
			this.drsy.equals(castOther.drsy)
			&& this.drrt.equals(castOther.drrt)
			&& this.drky.equals(castOther.drky);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.drsy.hashCode();
		hash = hash * prime + this.drrt.hashCode();
		hash = hash * prime + this.drky.hashCode();
		
		return hash;
	}
}