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

	public Pharmacy(double id, String name, String address, double iva, double cap, double cityCode, String city,
			double provinceCode, String provinceAbbreviation, String provinceName, double regionCode, String regionName,
			String beginValidity, String endValidity, double latitude, double longitude, double localize) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.iva = iva;
		this.cap = cap;
		this.cityCode = cityCode;
		this.city = city;
		this.provinceCode = provinceCode;
		this.provinceAbbreviation = provinceAbbreviation;
		this.provinceName = provinceName;
		this.regionCode = regionCode;
		this.regionName = regionName;
		this.beginValidity = beginValidity;
		this.endValidity = endValidity;
		this.latitude = latitude;
		this.longitude = longitude;
		this.localize = localize;
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

	@Override
	public String toString() {
		return "Pharmacy [id=" + id + ", name=" + name + ", address=" + address + ", iva=" + iva + ", cap=" + cap
				+ ", cityCode=" + cityCode + ", city=" + city + ", provinceCode=" + provinceCode
				+ ", provinceAbbreviation=" + provinceAbbreviation + ", provinceName=" + provinceName + ", regionCode="
				+ regionCode + ", regionName=" + regionName + ", beginValidity=" + beginValidity + ", endValidity="
				+ endValidity + ", latitude=" + latitude + ", longitude=" + longitude + ", localize=" + localize + "]";
	}
	
	
	
}