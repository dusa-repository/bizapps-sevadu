package modelo.maestros;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import modelo.pk.F0004PK;


/**
 * The persistent class for the F0004 database table.
 * 
 */
@Entity
public class F0004 implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private F0004PK id;

	@Column(name="DTCDL")
	private Double dtcdl;

	@Column(name="DTCNUM")
	private String dtcnum;

	@Column(name="DTDL01")
	private String dtdl01;

	@Column(name="DTJOBN")
	private String dtjobn;

	@Column(name="DTLN2")
	private String dtln2;

	@Column(name="DTMRCT")
	private String dtmrct;

	@Column(name="DTMRTY")
	private String dtmrty;

	@Column(name="DTPID")
	private String dtpid;

	@Column(name="DTUCD1")
	private String dtucd1;

	@Column(name="DTUPMJ")
	private BigDecimal dtupmj;

	@Column(name="DTUPMT")
	private Double dtupmt;

	@Column(name="DTUSEQ")
	private Double dtuseq;

	@Column(name="DTUSER")
	private String dtuser;

	public F0004() {
	}

	public F0004PK getId() {
		return this.id;
	}

	public void setId(F0004PK id) {
		this.id = id;
	}

	public Double getDtcdl() {
		return this.dtcdl;
	}

	public void setDtcdl(Double dtcdl) {
		this.dtcdl = dtcdl;
	}

	public String getDtcnum() {
		return this.dtcnum;
	}

	public void setDtcnum(String dtcnum) {
		this.dtcnum = dtcnum;
	}

	public String getDtdl01() {
		return this.dtdl01;
	}

	public void setDtdl01(String dtdl01) {
		this.dtdl01 = dtdl01;
	}

	public String getDtjobn() {
		return this.dtjobn;
	}

	public void setDtjobn(String dtjobn) {
		this.dtjobn = dtjobn;
	}

	public String getDtln2() {
		return this.dtln2;
	}

	public void setDtln2(String dtln2) {
		this.dtln2 = dtln2;
	}

	public String getDtmrct() {
		return this.dtmrct;
	}

	public void setDtmrct(String dtmrct) {
		this.dtmrct = dtmrct;
	}

	public String getDtmrty() {
		return this.dtmrty;
	}

	public void setDtmrty(String dtmrty) {
		this.dtmrty = dtmrty;
	}

	public String getDtpid() {
		return this.dtpid;
	}

	public void setDtpid(String dtpid) {
		this.dtpid = dtpid;
	}

	public String getDtucd1() {
		return this.dtucd1;
	}

	public void setDtucd1(String dtucd1) {
		this.dtucd1 = dtucd1;
	}

	public BigDecimal getDtupmj() {
		return this.dtupmj;
	}

	public void setDtupmj(BigDecimal dtupmj) {
		this.dtupmj = dtupmj;
	}

	public Double getDtupmt() {
		return this.dtupmt;
	}

	public void setDtupmt(Double dtupmt) {
		this.dtupmt = dtupmt;
	}

	public Double getDtuseq() {
		return this.dtuseq;
	}

	public void setDtuseq(Double dtuseq) {
		this.dtuseq = dtuseq;
	}

	public String getDtuser() {
		return this.dtuser;
	}

	public void setDtuser(String dtuser) {
		this.dtuser = dtuser;
	}

}