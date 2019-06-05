package exam;

public class Pharmacy {
	private double id;
	private String name;
	private String address;
	private double iva;
	private double cap;
	private double cityCode;
	private String city;
	private double provinceCode;
	private String provinceAbbreviation;
	private String provinceName;
	private double regionCode;
	private String regionName;
	private String beginValidity;
	private String endValidity;
	private double latitude;
	private double longitude;
	private double localize;
	
	public Pharmacy(String city, double regionCode, double latitude, String address, double id, double cap, double iva,
			String provinceName, double longitude, String regionName, String beginValidity, String endValidity,
			String provinceAbbreviation, String name, double localize, double cityCode, double provinceCode) {
		super();
		this.city = city;
		this.regionCode = regionCode;
		this.latitude = latitude;
		this.address = address;
		this.id = id;
		this.cap = cap;
		this.iva = iva;
		this.provinceName = provinceName;
		this.longitude = longitude;
		this.regionName = regionName;
		this.beginValidity = beginValidity;
		this.endValidity = endValidity;
		this.provinceAbbreviation = provinceAbbreviation;
		this.name = name;
		this.localize = localize;
		this.cityCode = cityCode;
		this.provinceCode = provinceCode;
	}

	public double getId() {
		return id;
	}

	public void setId(double id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public double getCap() {
		return cap;
	}

	public void setCap(double cap) {
		this.cap = cap;
	}

	public double getCityCode() {
		return cityCode;
	}

	public void setCityCode(double cityCode) {
		this.cityCode = cityCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(double provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceAbbreviation() {
		return provinceAbbreviation;
	}

	public void setProvinceAbbreviation(String provinceAbbreviation) {
		this.provinceAbbreviation = provinceAbbreviation;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public double getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(double regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getBeginValidity() {
		return beginValidity;
	}

	public void setBeginValidity(String beginValidity) {
		this.beginValidity = beginValidity;
	}

	public String getEndValidity() {
		return endValidity;
	}

	public void setEndValidity(String endValidity) {
		this.endValidity = endValidity;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLocalize() {
		return localize;
	}

	public void setLocalize(double localize) {
		this.localize = localize;
	}
	
}