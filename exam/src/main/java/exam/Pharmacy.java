package exam;

public class Pharmacy {
	private long id;
	private String name;
	private String address;
	private long VATNumber;
	private long CAP;
	private long ISTATCityCode;
	private String city;
	private long ISTATProvinceCode;
	private String provinceAbbreviation;
	private String provinceName;
	private long regionCode;
	private String regionName;
	private String beginningValidity;
	private String endValidity;
	private long latitude;
	private long longitude;
	private long localize;
	
	public Pharmacy(long id, String name, String address, long VATNumber, long CAP, long ISTATCityCode, String city,
			long iSTATProvinceCode, String provinceAbbreviation, String provinceName, long regionCode,
			String regionName, String beginningValidity, String endValidity, long latitude, long longitude,
			long localize) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.VATNumber = VATNumber;
		this.CAP = CAP;
		this.ISTATCityCode = ISTATCityCode;
		this.city = city;
		this.ISTATProvinceCode = iSTATProvinceCode;
		this.provinceAbbreviation = provinceAbbreviation;
		this.provinceName = provinceName;
		this.regionCode = regionCode;
		this.regionName = regionName;
		this.beginningValidity = beginningValidity;
		this.endValidity = endValidity;
		this.latitude = latitude;
		this.longitude = longitude;
		this.localize = localize;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public long getVATNumber() {
		return VATNumber;
	}

	public void setVATNumber(long VATNumber) {
		VATNumber = VATNumber;
	}

	public long getCAP() {
		return CAP;
	}

	public void setCAP(long cAP) {
		CAP = CAP;
	}

	public long getISTATCityCode() {
		return ISTATCityCode;
	}

	public void setISTATCityCode(long iSTATCityCode) {
		ISTATCityCode = ISTATCityCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public long getISTATProvinceCode() {
		return ISTATProvinceCode;
	}

	public void setISTATProvinceCode(long iSTATProvinceCode) {
		ISTATProvinceCode = ISTATProvinceCode;
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

	public long getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(long regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getBeginningValidity() {
		return beginningValidity;
	}

	public void setBeginningValidity(String beginningValidity) {
		this.beginningValidity = beginningValidity;
	}

	public String getEndValidity() {
		return endValidity;
	}

	public void setEndValidity(String endValidity) {
		this.endValidity = endValidity;
	}

	public long getLatitude() {
		return latitude;
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	public long getLongitude() {
		return longitude;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}

	public long getLocalize() {
		return localize;
	}

	public void setLocalize(long localize) {
		this.localize = localize;
	}
	
}
