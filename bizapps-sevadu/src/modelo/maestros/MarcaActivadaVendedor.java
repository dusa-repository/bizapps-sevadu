package modelo.maestros;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import modelo.pk.MarcaActivadaPK;


/**
 * The persistent class for the marca_activada_vendedor database table.
 * 
 */
@Entity
@Table(name="marca_activada_vendedor")
@NamedQuery(name="MarcaActivadaVendedor.findAll", query="SELECT m FROM MarcaActivadaVendedor m")
public class MarcaActivadaVendedor implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MarcaActivadaPK id;
	
	private Integer marcaA;
	
	private Integer marcaB;
	
	private Integer marcaC;
	
	private Integer marcaD;
	
	private Integer marcaE;
	
	private Integer marcaF;
	
	private Integer marcaG;
	
	private Integer marcaH;
	
	private Integer marcaI;
	
	private Integer marcaJ;
	
	private Integer marcaK;
	
	private Integer marcaL;
	
	private Integer marcaM;
	
	private Integer marcaN;
	
	private Integer marcaO;
	
	private Integer marcaP;
	
	private Integer marcaQ;
	
	private Integer marcaR;
	
	private Integer marcaS;
	
	private Integer marcaT;
	
	private Integer marcaU;
	
	private Integer marcaV;
	
	private Integer marcaW;
	
	private Integer marcaX;
	
	private Integer marcaY;
	
	private Integer marcaZ;
	
	private Integer marcaZA;
	
	private Integer marcaZB;
	
	private Integer marcaZC;
	
	private Integer marcaZD;
	
	private Integer marcaZE;
	
	private Integer marcaZF;
	
	private Integer marcaZG;
	
	private Integer marcaZH;
	
	private Integer marcaZI;
	
	private Integer marcaZJ;
	
	private Integer marcaZK;
	
	private Integer marcaZL;
	
	private Integer marcaZM;
	
	private Integer marcaZN;
	
	private Integer marcaZO;
	
	private Integer marcaZP;
	
	private Integer marcaZQ;
	
	private Integer marcaZR;
	
	private Integer marcaZS;
	
	private Integer marcaZT;
	
	private Integer marcaZU;
	
	private Integer marcaZV;
	
	private Integer marcaZW;
	
	private Integer marcaZX;
	
	private Integer total;
	
	private Float porcentaje;
	
	private String campoA;
	
	private String campoB;
	
	public MarcaActivadaVendedor() {
	}

	public MarcaActivadaPK getId() {
		return id;
	}

	public void setId(MarcaActivadaPK id) {
		this.id = id;
	}

	public Integer getMarcaA() {
		return marcaA;
	}

	public void setMarcaA(Integer marcaA) {
		this.marcaA = marcaA;
	}

	public Integer getMarcaB() {
		return marcaB;
	}

	public void setMarcaB(Integer marcaB) {
		this.marcaB = marcaB;
	}

	public Integer getMarcaC() {
		return marcaC;
	}

	public void setMarcaC(Integer marcaC) {
		this.marcaC = marcaC;
	}

	public Integer getMarcaD() {
		return marcaD;
	}

	public void setMarcaD(Integer marcaD) {
		this.marcaD = marcaD;
	}

	public Integer getMarcaE() {
		return marcaE;
	}

	public void setMarcaE(Integer marcaE) {
		this.marcaE = marcaE;
	}

	public Integer getMarcaF() {
		return marcaF;
	}

	public void setMarcaF(Integer marcaF) {
		this.marcaF = marcaF;
	}

	public Integer getMarcaG() {
		return marcaG;
	}

	public void setMarcaG(Integer marcaG) {
		this.marcaG = marcaG;
	}

	public Integer getMarcaH() {
		return marcaH;
	}

	public void setMarcaH(Integer marcaH) {
		this.marcaH = marcaH;
	}

	public Integer getMarcaI() {
		return marcaI;
	}

	public void setMarcaI(Integer marcaI) {
		this.marcaI = marcaI;
	}

	public Integer getMarcaJ() {
		return marcaJ;
	}

	public void setMarcaJ(Integer marcaJ) {
		this.marcaJ = marcaJ;
	}

	public Integer getMarcaK() {
		return marcaK;
	}

	public void setMarcaK(Integer marcaK) {
		this.marcaK = marcaK;
	}

	public Integer getMarcaL() {
		return marcaL;
	}

	public void setMarcaL(Integer marcaL) {
		this.marcaL = marcaL;
	}

	public Integer getMarcaM() {
		return marcaM;
	}

	public void setMarcaM(Integer marcaM) {
		this.marcaM = marcaM;
	}

	public Integer getMarcaN() {
		return marcaN;
	}

	public void setMarcaN(Integer marcaN) {
		this.marcaN = marcaN;
	}

	public Integer getMarcaO() {
		return marcaO;
	}

	public void setMarcaO(Integer marcaO) {
		this.marcaO = marcaO;
	}

	public Integer getMarcaP() {
		return marcaP;
	}

	public void setMarcaP(Integer marcaP) {
		this.marcaP = marcaP;
	}

	public Integer getMarcaQ() {
		return marcaQ;
	}

	public void setMarcaQ(Integer marcaQ) {
		this.marcaQ = marcaQ;
	}

	public Integer getMarcaR() {
		return marcaR;
	}

	public void setMarcaR(Integer marcaR) {
		this.marcaR = marcaR;
	}

	public Integer getMarcaS() {
		return marcaS;
	}

	public void setMarcaS(Integer marcaS) {
		this.marcaS = marcaS;
	}

	public Integer getMarcaT() {
		return marcaT;
	}

	public void setMarcaT(Integer marcaT) {
		this.marcaT = marcaT;
	}

	public Integer getMarcaU() {
		return marcaU;
	}

	public void setMarcaU(Integer marcaU) {
		this.marcaU = marcaU;
	}

	public Integer getMarcaV() {
		return marcaV;
	}

	public void setMarcaV(Integer marcaV) {
		this.marcaV = marcaV;
	}

	public Integer getMarcaW() {
		return marcaW;
	}

	public void setMarcaW(Integer marcaW) {
		this.marcaW = marcaW;
	}

	public Integer getMarcaX() {
		return marcaX;
	}

	public void setMarcaX(Integer marcaX) {
		this.marcaX = marcaX;
	}

	public Integer getMarcaY() {
		return marcaY;
	}

	public void setMarcaY(Integer marcaY) {
		this.marcaY = marcaY;
	}

	public Integer getMarcaZ() {
		return marcaZ;
	}

	public void setMarcaZ(Integer marcaZ) {
		this.marcaZ = marcaZ;
	}

	public Integer getMarcaZA() {
		return marcaZA;
	}

	public void setMarcaZA(Integer marcaZA) {
		this.marcaZA = marcaZA;
	}

	public Integer getMarcaZB() {
		return marcaZB;
	}

	public void setMarcaZB(Integer marcaZB) {
		this.marcaZB = marcaZB;
	}

	public Integer getMarcaZC() {
		return marcaZC;
	}

	public void setMarcaZC(Integer marcaZC) {
		this.marcaZC = marcaZC;
	}

	public Integer getMarcaZD() {
		return marcaZD;
	}

	public void setMarcaZD(Integer marcaZD) {
		this.marcaZD = marcaZD;
	}

	public Integer getMarcaZE() {
		return marcaZE;
	}

	public void setMarcaZE(Integer marcaZE) {
		this.marcaZE = marcaZE;
	}

	public Integer getMarcaZF() {
		return marcaZF;
	}

	public void setMarcaZF(Integer marcaZF) {
		this.marcaZF = marcaZF;
	}

	public Integer getMarcaZG() {
		return marcaZG;
	}

	public void setMarcaZG(Integer marcaZG) {
		this.marcaZG = marcaZG;
	}

	public Integer getMarcaZH() {
		return marcaZH;
	}

	public void setMarcaZH(Integer marcaZH) {
		this.marcaZH = marcaZH;
	}

	public Integer getMarcaZI() {
		return marcaZI;
	}

	public void setMarcaZI(Integer marcaZI) {
		this.marcaZI = marcaZI;
	}

	public Integer getMarcaZJ() {
		return marcaZJ;
	}

	public void setMarcaZJ(Integer marcaZJ) {
		this.marcaZJ = marcaZJ;
	}

	public Integer getMarcaZK() {
		return marcaZK;
	}

	public void setMarcaZK(Integer marcaZK) {
		this.marcaZK = marcaZK;
	}

	public Integer getMarcaZL() {
		return marcaZL;
	}

	public void setMarcaZL(Integer marcaZL) {
		this.marcaZL = marcaZL;
	}

	public Integer getMarcaZM() {
		return marcaZM;
	}

	public void setMarcaZM(Integer marcaZM) {
		this.marcaZM = marcaZM;
	}

	public Integer getMarcaZN() {
		return marcaZN;
	}

	public void setMarcaZN(Integer marcaZN) {
		this.marcaZN = marcaZN;
	}

	public Integer getMarcaZO() {
		return marcaZO;
	}

	public void setMarcaZO(Integer marcaZO) {
		this.marcaZO = marcaZO;
	}

	public Integer getMarcaZP() {
		return marcaZP;
	}

	public void setMarcaZP(Integer marcaZP) {
		this.marcaZP = marcaZP;
	}

	public Integer getMarcaZQ() {
		return marcaZQ;
	}

	public void setMarcaZQ(Integer marcaZQ) {
		this.marcaZQ = marcaZQ;
	}

	public Integer getMarcaZR() {
		return marcaZR;
	}

	public void setMarcaZR(Integer marcaZR) {
		this.marcaZR = marcaZR;
	}

	public Integer getMarcaZS() {
		return marcaZS;
	}

	public void setMarcaZS(Integer marcaZS) {
		this.marcaZS = marcaZS;
	}

	public Integer getMarcaZT() {
		return marcaZT;
	}

	public void setMarcaZT(Integer marcaZT) {
		this.marcaZT = marcaZT;
	}

	public Integer getMarcaZU() {
		return marcaZU;
	}

	public void setMarcaZU(Integer marcaZU) {
		this.marcaZU = marcaZU;
	}

	public Integer getMarcaZV() {
		return marcaZV;
	}

	public void setMarcaZV(Integer marcaZV) {
		this.marcaZV = marcaZV;
	}

	public Integer getMarcaZW() {
		return marcaZW;
	}

	public void setMarcaZW(Integer marcaZW) {
		this.marcaZW = marcaZW;
	}

	public Integer getMarcaZX() {
		return marcaZX;
	}

	public void setMarcaZX(Integer marcaZX) {
		this.marcaZX = marcaZX;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Float getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Float porcentaje) {
		this.porcentaje = porcentaje;
	}

	public String getCampoA() {
		return campoA;
	}

	public void setCampoA(String campoA) {
		this.campoA = campoA;
	}

	public String getCampoB() {
		return campoB;
	}

	public void setCampoB(String campoB) {
		this.campoB = campoB;
	}
	
	

}