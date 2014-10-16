package modelo.maestros;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import modelo.pk.F0005PK;



/**
 * The persistent class for the F0005 database table.
 * 
 */
@Entity
public class F0005 implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private F0005PK id;

	@Column(name="DRDL01")
	private String drdl01;

	@Column(name="DRDL02")
	private String drdl02;

	//
	@Column(name="DRHRDC")
	private String drhrdc;

	@Column(name="DRJOBN")
	private String drjobn;

	@Column(name="DRPID")
	private String drpid;

	//
	@Column(name="DRSPHD")
	private String drsphd;

	@Column(name="DRUDCO")
	private String drudco;

	//
	@Column(name="DRUPMJ")
	private BigDecimal drupmj;

	@Column(name="DRUPMT")
	private Double drupmt;

	//
	@Column(name="DRUSER")
	private String druser;

	public F0005() {
	}

	public F0005PK getId() {
		return this.id;
	}

	public void setId(F0005PK id) {
		this.id = id;
	}

	public String getDrdl01() {
		return this.drdl01;
	}

	public void setDrdl01(String drdl01) {
		this.drdl01 = drdl01;
	}

	public String getDrdl02() {
		return this.drdl02;
	}

	public void setDrdl02(String drdl02) {
		this.drdl02 = drdl02;
	}

	public String getDrhrdc() {
		return this.drhrdc;
	}

	public void setDrhrdc(String drhrdc) {
		this.drhrdc = drhrdc;
	}

	public String getDrjobn() {
		return this.drjobn;
	}

	public void setDrjobn(String drjobn) {
		this.drjobn = drjobn;
	}

	public String getDrpid() {
		return this.drpid;
	}

	public void setDrpid(String drpid) {
		this.drpid = drpid;
	}

	public String getDrsphd() {
		return this.drsphd;
	}

	public void setDrsphd(String drsphd) {
		this.drsphd = drsphd;
	}

	public String getDrudco() {
		return this.drudco;
	}

	public void setDrudco(String drudco) {
		this.drudco = drudco;
	}

	public BigDecimal getDrupmj() {
		return this.drupmj;
	}

	public void setDrupmj(BigDecimal drupmj) {
		this.drupmj = drupmj;
	}

	public Double getDrupmt() {
		return this.drupmt;
	}

	public void setDrupmt(Double drupmt) {
		this.drupmt = drupmt;
	}

	public String getDruser() {
		return this.druser;
	}

	public void setDruser(String druser) {
		this.druser = druser;
	}

}